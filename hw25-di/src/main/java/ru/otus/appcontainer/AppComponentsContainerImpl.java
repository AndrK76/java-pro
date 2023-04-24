package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.config.ConfigurationBuildException;
import ru.otus.config.ConfigurationNoExistComponentException;
import ru.otus.config.ConfigurationTooManyComponentException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.otus.helpers.ReflectionHelper.parseConfigClass;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        var configInfo = parseConfigClass(configClass);
        try {
            var configInstance = configClass.getDeclaredConstructors()[0].newInstance(null);
            configInfo.stream().sorted().forEach(r -> {
                if (appComponentsByName.containsKey(r.name())) {
                    throw new ConfigurationBuildException("Компонент с именем " + r.name() + " уже добавлен в конфигурацию");
                }
                List<Object> pars = new ArrayList<>();
                for (var param : r.method().getParameters()) {
                    pars.add(getAppComponent(param.getType()));
                }
                try {
                    var res = r.method().invoke(configInstance, pars.toArray());
                    appComponents.add(res);
                    appComponentsByName.put(r.name(), res);
                } catch (Exception e) {
                    throw new ConfigurationBuildException(e);
                }
            });
        } catch (Exception e) {
            throw new ConfigurationBuildException(e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        Predicate<? super Object> exactMatch = c -> c.getClass().equals(componentClass);
        Predicate<? super Object> assignable = c -> componentClass.isAssignableFrom(c.getClass());
        for (Predicate<? super Object> filterFunc : new Predicate[]{exactMatch, assignable}) {
            var found = appComponents.stream().filter(filterFunc).collect(Collectors.toList());
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
        var ret = (C) appComponentsByName.get(componentName);
        if (ret==null){
            throw  new ConfigurationNoExistComponentException(componentName);
        }
        return ret;
    }
}
