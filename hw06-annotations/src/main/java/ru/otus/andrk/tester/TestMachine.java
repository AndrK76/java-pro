package ru.otus.andrk.tester;

import ru.otus.andrk.annotations.After;
import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;
import ru.otus.andrk.annotations.TestName;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;


public class TestMachine {
    public static RunTestStatistics Run(String testClass) {
        try {
            return Run(Class.forName(testClass));
        } catch (ClassNotFoundException e) {
            var tm = LocalDateTime.now();
            var startErr = RunOneTestStatistic.createFailure(testClass, tm, tm,
                    new RuntimeException(String.format("Ошибка при поиске класса %s", testClass), e));
            return RunTestStatistics.createWithFailedResult(testClass, startErr);
        }
    }

    public static RunTestStatistics Run(Class<?> testClass) {
        var results = RunTestStatistics.create(testClass.getSimpleName());
        var test = new TestMachine(testClass, results);
        var err = test.parseClass();
        if (err != null) {
            return results;
        }
        for (var testInfo : test.tests) {
            results.addResult(test.runTest(testInfo));
        }


        return results;
    }

    record TestInfo(Method method, String name) {
    }

    private final RunTestStatistics results;
    private final Class<?> testClass;

    private final LocalDateTime startTime;

    private TestMachine(Class<?> testClass, RunTestStatistics results) {
        this.testClass = testClass;
        this.startTime = LocalDateTime.now();
        this.results = results;
    }

    Constructor<?> constructor;
    ArrayList<Method> initializers = new ArrayList<>();
    ArrayList<TestInfo> tests = new ArrayList<>();
    ArrayList<Method> finalizers = new ArrayList<>();

    private Throwable parseClass() {
        try {
            for (var constr : testClass.getConstructors()) {
                if (constr.getParameterCount() == 0) {
                    constructor = constr;
                }
                if (constructor == null) {
                    throw new NoSuchMethodException("Не найден конструктор по умолчанию для класса теста");
                }
                if (!testClass.isAnnotationPresent(Test.class))
                    throw new ReflectiveOperationException("Класс не является тестом (нет аннотации @Test");
            }
            for (var method : testClass.getDeclaredMethods()) {
                int isBefore = method.isAnnotationPresent(Before.class) ? 1 : 0;
                int isTest = method.isAnnotationPresent(Test.class) ? 1 : 0;
                int isAfter = method.isAnnotationPresent(After.class) ? 1 : 0;
                if (isBefore + isTest + isAfter == 0) {
                    continue;
                }

                String annotations = Arrays.stream(method.getDeclaredAnnotations())
                        .map(r -> "@" + r.annotationType().getSimpleName()).collect(Collectors.joining(","));

                if (isBefore + isTest + isAfter > 1) {
                    throw new ReflectiveOperationException(
                            String.format("Некорректная аннотация для метода %s [%s]", method.getName(), annotations));

                }
                if (method.getReturnType() != void.class) {

                    throw new ReflectiveOperationException(
                            String.format("У метода %s %s некорректный возвращаемый тип", annotations, method.getName())
                    );
                }
                if (method.getParameterCount() != 0) {
                    throw new ReflectiveOperationException(
                            String.format("У метода %s %s указаны параметры", annotations, method.getName())
                    );
                }
                if (isBefore > 0) {
                    initializers.add(method);
                }
                if (isAfter > 0) {
                    finalizers.add(method);
                }
                if (isTest > 0) {
                    String testName;
                    if (method.isAnnotationPresent(TestName.class)) {
                        testName = method.getAnnotation(TestName.class).value();
                    } else {
                        testName = method.getName();
                    }
                    tests.add(new TestInfo(method, testName));
                }
            }
            if (tests.size() == 0) {
                throw new ReflectiveOperationException("В разбираемом классе тесты не найдены");
            }
            return null;
        } catch (Throwable e) {
            results.addResult(RunOneTestStatistic.createFailure(testClass.getSimpleName(), startTime, startTime,
                    new RuntimeException("Ошибка при разборе класса теста", e)));
            return e;
        }
    }

    private RunOneTestStatistic runTest(TestInfo test) {
        var startTime = LocalDateTime.now();
        RunOneTestStatistic res;
        Object instance = null;
        try {
            try {
                instance = constructor.newInstance();
            } catch (Throwable e) {
                throw new RuntimeException("Ошибка при создании тестового класса", e);
            }
            for (var initializer : initializers) {
                try {
                    initializer.setAccessible(true);
                    initializer.invoke(instance);
                } catch (Throwable e) {
                    throw new RuntimeException(String.format("Ошибка при инициализации теста в методе %s", initializer.getName()), e);
                }
            }
            test.method.setAccessible(true);
            test.method.invoke(instance);
            res = RunOneTestStatistic.createSuccess(test.name, startTime, LocalDateTime.now());
        } catch (Throwable e) {
            res = RunOneTestStatistic.createFailure(test.name, startTime, LocalDateTime.now(), e);
        } finally {
            if (instance != null) {
                for (var finalizer : finalizers) {
                    try {
                        finalizer.setAccessible(true);
                        finalizer.invoke(instance);
                    } catch (Throwable e) {
                        res = RunOneTestStatistic.createFailure(test.name, startTime, LocalDateTime.now(),
                                new RuntimeException(String.format("Ошибка при завершении теста в методе %s", finalizer.getName()), e));
                    }
                }
            }
        }
        return res;
    }

}
