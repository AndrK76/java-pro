package ru.otus.andrk.proxy;

import ru.otus.andrk.testlogging.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

class ReflectionHandler {
    private final Constructor<?> constructor;
    private final Set<Method> methodsForLog = new HashSet<>();

    public ReflectionHandler(Class<?> clazz) throws NoSuchMethodException {
        this.constructor = clazz.getDeclaredConstructor();
        if (!TestLogging.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(clazz.getSimpleName() + " не реализует интерфейс TestLogging");
        }
        for (var interfaceMethod : TestLogging.class.getDeclaredMethods()) {
            var implMethod = clazz.getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
            if (implMethod.isAnnotationPresent(Log.class)) {
                this.methodsForLog.add(interfaceMethod);
            }
        }
    }

    public Object createInstance() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return constructor.newInstance();
    }

    public Set<Method> getMethodsForLog() {
        return methodsForLog;
    }
}
