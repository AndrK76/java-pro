package ru.otus.helpers;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionHelper {
    public record AnnotatedMethodInfo(int configOrder, String name, Method method, int order)
    implements Comparable<AnnotatedMethodInfo>{
        @Override
        public int compareTo(AnnotatedMethodInfo o) {
            return this.order - o.order;
        }
    }


    public static List<AnnotatedMethodInfo> parseConfigClass(Class<?> configClass) {
        var configOrder = configClass.getAnnotation(AppComponentsContainerConfig.class).order();
        return Arrays.stream(configClass.getDeclaredMethods())
                .filter(r -> r.isAnnotationPresent(AppComponent.class))
                .map(r -> {
                    var annotation = r.getAnnotation(AppComponent.class);
                    return new AnnotatedMethodInfo(configOrder, annotation.name(), r, annotation.order());
                }).collect(Collectors.toList());
    }
}
