package ru.otus.helpers;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionHelper {
    public record AnnotatedMethodInfo(int configOrder, String name, Method method, int order) {
    }


    public static List<AnnotatedMethodInfo> parseConfigClass(Class<?> configClass) {
        var ret = new ArrayList<AnnotatedMethodInfo>();
        var configOrder = configClass.getAnnotation(AppComponentsContainerConfig.class).order();
        Arrays.stream(configClass.getDeclaredMethods())
                .forEach(r -> {
                    if (r.isAnnotationPresent(AppComponent.class)) {
                        var annotation = r.getAnnotation(AppComponent.class);
                        ret.add(new AnnotatedMethodInfo(configOrder, annotation.name(), r, annotation.order()));
                    }
                });
        return ret;
    }
}
