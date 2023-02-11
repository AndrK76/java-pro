package ru.otus.andrk.proxy;

import ru.otus.andrk.testlogging.*;

import java.lang.reflect.*;

public class LoggingProxy {
    public static TestLogging create(Class<?> clazz, Logger logger) {
        try {
            var handler = TestLoggingHandler.create(clazz, logger);
            return (TestLogging) Proxy.newProxyInstance(LoggingProxy.class.getClassLoader(),
                    new Class<?>[]{TestLogging.class}, handler);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
