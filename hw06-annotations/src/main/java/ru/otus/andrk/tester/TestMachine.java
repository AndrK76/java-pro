package ru.otus.andrk.tester;

import ru.otus.andrk.annotations.After;
import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;
import ru.otus.andrk.annotations.TestName;

import java.lang.reflect.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


public class TestMachine {
    public static TestStatistics run(String testClass) {
        try {
            return run(Class.forName(testClass));
        } catch (ClassNotFoundException e) {
            var tm = LocalDateTime.now();
            var startErr = RunOneTestStatistic.createFailure(testClass, tm, tm,
                    new RuntimeException(String.format("Ошибка при поиске класса %s", testClass), e));
            return RunTestStatistics.createWithResult(testClass, startErr);
        }
    }

    public static TestStatistics run(Class<?> testClass) {
        var results = RunTestStatistics.create(testClass.getSimpleName());
        var testMachine = new TestMachine(testClass, results);
        var err = testMachine.parseClass();
        if (err != null) {
            results.addResult(RunOneTestStatistic.createFailure(testClass.getSimpleName(), testMachine.startTime, LocalDateTime.now(),
                    new RuntimeException("Ошибка при разборе класса теста", err)));
            return results;
        }
        for (var testInfo : testMachine.getMethodsForRunWithType(MethodType.TEST))
            results.addResult(testMachine.runTest(testInfo));
        return results;
    }


    private enum MethodType {
        CONSTRUCTOR,
        BEFORE,
        TEST,
        AFTER
    }

    private record MethodInfo(MethodType type, Method method, String name) {
    }

    private final TestStatistics results;
    private final Class<?> testClass;
    private final LocalDateTime startTime;

    private Constructor<?> classConstructor;
    private Collection<MethodInfo> methodsForRun = new ArrayList<>();

    private TestMachine(Class<?> testClass, TestStatistics results) {
        this.testClass = testClass;
        this.startTime = LocalDateTime.now();
        this.results = results;
    }

    private Throwable parseClass() {
        try {
            findClassConstructor();
            findOtherClassMethods();
            return null;
        } catch (ReflectiveOperationException e) {
            return e;
        }
    }

    private OneTestStatistic runTest(MethodInfo test) {
        var startTime = LocalDateTime.now();
        OneTestStatistic res;
        Object instance = null;
        try {
            try {
                instance = classConstructor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Ошибка при создании тестового класса", e);
            }

            try {
                runMethods(getMethodsForRunWithType(MethodType.BEFORE), instance);
            } catch (RunMethodException e) {
                throw new RuntimeException(
                        String.format("Ошибка при инициализации теста в методе %s", e.getRunningMethod()),
                        e.getCause());
            }

            try {
                runMethod(test, instance);
                res = RunOneTestStatistic.createSuccess(test.name, startTime, LocalDateTime.now());
            } catch (RunMethodException e) {
                throw new RuntimeException("Ошибка при выполнении теста", e.getCause());
            }
        } catch (RuntimeException e) {
            res = RunOneTestStatistic.createFailure(test.name, startTime, LocalDateTime.now(), e);
        } finally {
            if (instance != null) {
                try {
                    runMethods(getMethodsForRunWithType(MethodType.AFTER), instance);
                } catch (RunMethodException e) {
                    res = RunOneTestStatistic.createFailure(test.name, startTime, LocalDateTime.now(),
                            new RuntimeException(String.format("Ошибка при завершении теста в методе %s",
                                    e.getRunningMethod()), e.getCause()));
                }
            }
        }

        return res;
    }

    private Collection<MethodInfo> getMethodsForRunWithType(MethodType methodType) {
        return methodsForRun.stream().filter(r -> r.type == methodType).toList();
    }

    private void runMethods(Collection<MethodInfo> methodInfos, Object instance) {
        for (var methodInfo : methodInfos) {
            runMethod(methodInfo, instance);
        }
    }

    private void runMethod(MethodInfo methodInfo, Object instance) {
        try {
            methodInfo.method.invoke(instance);
        } catch (InvocationTargetException ex) {
            throw new RunMethodException(methodInfo.method.getName(), ex.getTargetException());
        }catch (Exception ex) {
            throw new RunMethodException(methodInfo.method.getName(), ex);
        }
    }

    private MethodInfo getInfoForNeededMethod(Executable method, boolean isConstructor)
            throws ReflectiveOperationException {
        if (Modifier.isStatic(method.getModifiers())) {
            return null; //Статические методы не интересны
        }
        boolean hasParams = method.getParameterCount() > 0;
        method.setAccessible(true);
        if (isConstructor) {
            //Для конструктора остальные проверки не нужны
            return hasParams ? null : new MethodInfo(MethodType.CONSTRUCTOR, null, null);
        }
        var methodTypes = new ArrayList<MethodType>();
        if (method.isAnnotationPresent(Before.class)) {
            methodTypes.add(MethodType.BEFORE);
        }
        if (method.isAnnotationPresent(Test.class)) {
            methodTypes.add(MethodType.TEST);
        }
        if (method.isAnnotationPresent(After.class)) {
            methodTypes.add(MethodType.AFTER);
        }

        if (methodTypes.size() == 0) {
            return null; //метод не интересен
        }

        String annotationsAsString = Arrays.stream(method.getDeclaredAnnotations())
                .map(r -> "@" + r.annotationType().getSimpleName()).collect(Collectors.joining(","));

        if (methodTypes.size() > 1) {
            throw new ReflectiveOperationException(
                    String.format("Некорректная аннотация для метода %s [%s]", method.getName(), annotationsAsString));
        }
        if (((Method) method).getReturnType() != void.class) {
            throw new ReflectiveOperationException(
                    String.format("У метода %s %s некорректный возвращаемый тип", annotationsAsString, method.getName())
            );
        }
        if (method.getParameterCount() != 0) {
            throw new ReflectiveOperationException(
                    String.format("У метода %s %s указаны параметры", annotationsAsString, method.getName())
            );
        }

        String methodName;
        if (methodTypes.get(0) == MethodType.TEST && method.isAnnotationPresent(TestName.class)) {
            methodName = method.getAnnotation(TestName.class).value();
        } else {
            methodName = method.getName();
        }
        return new MethodInfo(methodTypes.get(0), (Method) method, methodName);
    }

    private void findClassConstructor() throws ReflectiveOperationException {
        if (!testClass.isAnnotationPresent(Test.class))
            throw new ReflectiveOperationException("Класс не является тестом (нет аннотации @Test");

        for (var constr : testClass.getConstructors()) {
            if (getInfoForNeededMethod(constr, true) != null) {
                classConstructor = constr;
                break;
            }
        }
        if (classConstructor == null) {
            throw new NoSuchMethodException("Не найден конструктор по умолчанию для класса теста");
        }
    }
    private void findOtherClassMethods() throws ReflectiveOperationException {
        for (var method : testClass.getDeclaredMethods()) {
            var methodInfo = getInfoForNeededMethod(method, false);
            if (methodInfo != null) {
                methodsForRun.add(methodInfo);
            }
        }
        if (getMethodsForRunWithType(MethodType.TEST).size() == 0) {
            throw new ReflectiveOperationException("В разбираемом классе тесты не найдены");
        }
    }
}
