package ru.otus.andrk.proxy;

import ru.otus.andrk.testlogging.Logger;
import ru.otus.andrk.testlogging.TestLogging;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class TestLoggingHandler implements InvocationHandler {
    private final TestLogging obj;
    private ReflectionHandler objInfo;
    private Logger logger;

    public static TestLoggingHandler create(Class<?> clazz, Logger logger) throws ReflectiveOperationException {
        var objInfo = new ReflectionHandler(clazz);
        var ret = new TestLoggingHandler((TestLogging) objInfo.createInstance());
        ret.objInfo = objInfo;
        ret.logger = logger;
        return ret;
    }
    private TestLoggingHandler(TestLogging obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (this.objInfo.getMethodsForLog().contains(method)) {
            StringBuilder log = new StringBuilder("executed method: " + method.getName() + ", parameters: ");
            if (args == null || args.length == 0) {
                log.append("empty");
            } else {
                logArgs(args, log);
            }
            logger.add(log.toString());
        }
        return method.invoke(obj, args);
    }

    private static void logArgs(Object args, StringBuilder log) {
        if (args.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(args); i++) {
                logArgs(Array.get(args, i), log);
            }
        } else {
            log.append(String.format("{type=%s, value=%s}, ", args.getClass().getSimpleName(), args));
        }
    }

}
