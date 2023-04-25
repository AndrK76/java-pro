package ru.otus.helpers;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.exceptions.ConfigurationBuildException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReflectionHelper {
    public record AnnotatedMethodInfo(Object config, String name, Method method, int order)
            implements Comparable<AnnotatedMethodInfo> {
        @Override
        public int compareTo(AnnotatedMethodInfo o) {
            return this.order - o.order;
        }
    }
    record ConfigInfo(int order, Class<?> configClass)
            implements Comparable<ConfigInfo> {
        @Override
        public int compareTo(ConfigInfo o) {
            return this.order - o.order;
        }
    }

    /**
     * Получить список методов помеченных аннотацией {@link AppComponent}
     * @param configClasses из списка конфигураций
     * @return List <{@link AnnotatedMethodInfo}>
     */
    public static List<AnnotatedMethodInfo> getAppComponentMethods(Class<?>... configClasses) {
        Map<Class<?>, Object> knownConfigs = createConfigInstances(configClasses);

        List<AnnotatedMethodInfo> result = new ArrayList<>();
        knownConfigs.keySet().forEach(configClass ->
                Arrays.stream(configClass.getDeclaredMethods())
                        .filter(r -> r.isAnnotationPresent(AppComponent.class))
                        .forEach(method -> {
                            var annotation = method.getAnnotation(AppComponent.class);
                            result.add(new AnnotatedMethodInfo(
                                    knownConfigs.get(configClass), annotation.name(), method, annotation.order()));
                        }));
        return result;
    }

    private static Map<Class<?>, Object> createConfigInstances(Class<?>[] configClasses) {
        Map<Class<?>, Object> knownConfigs = new HashMap<>();
        Arrays.stream(configClasses).map(r ->
        {
            int order = r.getAnnotation(AppComponentsContainerConfig.class).order();
            return new ConfigInfo(order, r);
        }).sorted().forEach(config -> addToKnownConfigs(knownConfigs, config));
        return knownConfigs;
    }

    private static void addToKnownConfigs(Map<Class<?>, Object> knownConfigs, ConfigInfo config) {
        var constructor = getValidConstructor(knownConfigs, config);
        var pars = new ArrayList();
        for (var par : constructor.getParameters()) {
            pars.add(knownConfigs.get(par.getClass()));
        }
        try {
            var configInstance = constructor.newInstance(pars.toArray());
            knownConfigs.put(config.configClass, configInstance);
        } catch (Exception e) {
            throw new ConfigurationBuildException(e);
        }
    }

    private static Constructor<?> getValidConstructor(Map<Class<?>, Object> knownConfigs, ConfigInfo config) {
        var constructors = Arrays.stream(config.configClass.getConstructors())
                .filter(constructor -> isConstructorCanExecute(knownConfigs, constructor)
                ).toList();
        if (constructors.size() == 0)
            throw new ConfigurationBuildException("Не найден подходящий конструктор для класса "
                    + config.configClass().getName());
        return constructors.get(0);
    }

    private static boolean isConstructorCanExecute(Map<Class<?>, Object> knownConfigs, Constructor<?> constructor) {
        if (constructor.getParameterCount() == 0) {
            return true;
        }
        AtomicBoolean ret = new AtomicBoolean(true);
        Arrays.stream(constructor.getParameterTypes())
                .forEach(r -> ret.set(ret.get() && knownConfigs.containsKey(r)));
        return ret.get();
    }
}
