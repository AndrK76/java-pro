package ru.otus.andrk.proxy;

import ru.otus.andrk.testlogging.Log;
import ru.otus.andrk.testlogging.TestLogging;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

class ReflectionHandler {
    Constructor<?> constructor;
    Set<Method> methodsForLog = new HashSet<>();

    public static ReflectionHandler create(Class<?> clazz) throws NoSuchMethodException {
        var ret = new ReflectionHandler();
        if (!TestLogging.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(clazz.getSimpleName() + " не реализует интерфейс TestLogging");
        }
        ret.constructor = clazz.getDeclaredConstructor();
        for (var interfaceMethod : TestLogging.class.getDeclaredMethods()) {
            var implMethod = clazz.getMethod(interfaceMethod.getName(),interfaceMethod.getParameterTypes());
            if (implMethod.isAnnotationPresent(Log.class)) {
                ret.methodsForLog.add(interfaceMethod);
            }
        }
        return ret;
    }

    public Object getInstance() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return constructor.newInstance();
    }

    public Set<Method> getMethodsForLog() {
        return methodsForLog;
    }
}
