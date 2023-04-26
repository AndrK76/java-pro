package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.exceptions.ConfigurationBuildException;
import ru.otus.appcontainer.exceptions.ConfigurationNoExistComponentException;
import ru.otus.appcontainer.exceptions.ConfigurationTooManyComponentException;
import ru.otus.helpers.ReflectionHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static ru.otus.helpers.ReflectionHelper.getAppComponentMethods;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    static final Logger log = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(String packageName) {
        var reflections = new Reflections(packageName);
        var configClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
        processConfig(configClasses.stream().toArray(Class<?>[]::new));
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        processConfig(initialConfigClasses);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        log.debug("start get component by class {}", componentClass.getName());
        Predicate<? super Object> exactMatch = c -> c.getClass().equals(componentClass);
        Predicate<? super Object> assignable = c -> componentClass.isAssignableFrom(c.getClass());
        for (Predicate<? super Object> filterFunc : new Predicate[]{exactMatch, assignable}) {
            var found = appComponents.stream().filter(filterFunc).toList();
            if (found.size() == 1) {
                return (C) found.get(0);
            } else if (found.size() > 1) {
                throw new ConfigurationTooManyComponentException(componentClass);
            }
        }
        throw new ConfigurationNoExistComponentException(componentClass);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        log.debug("start get component by name {}", componentName);
        var ret = (C) appComponentsByName.get(componentName);
        if (ret == null) {
            throw new ConfigurationNoExistComponentException(componentName);
        }
        return ret;
    }

    private void processConfig(Class<?>... configClasses) {
        checkConfigClass(configClasses);
        var configInfo = getAppComponentMethods(configClasses);
        configInfo.stream().sorted().forEach(this::createAppComponent);
    }

    private void createAppComponent(ReflectionHelper.AnnotatedMethodInfo info) {
        if (appComponentsByName.containsKey(info.name())) {
            throw new ConfigurationBuildException("Компонент с именем " + info.name() + " уже добавлен в конфигурацию");
        }
        List<Object> pars = new ArrayList<>();
        for (var param : info.method().getParameters()) {
            pars.add(getAppComponent(param.getType()));
        }
        try {
            log.debug("start create {}", info.name());
            var res = info.method().invoke(info.config(), pars.toArray());
            appComponents.add(res);
            appComponentsByName.put(info.name(), res);
        } catch (Exception e) {
            throw new ConfigurationBuildException(e);
        }
    }

    private void checkConfigClass(Class<?>... configClasses) {
        for (var configClass : configClasses) {
            if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
                throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
            }
        }
    }

}
