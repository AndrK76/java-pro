package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.andrk.tester.OneTestStatistic;
import ru.otus.andrk.tester.TestMachine;
import tests.testsamples.*;
import tests.testsamples.invalidsignatures.*;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestMachineTest {
    @Test
    @DisplayName("Проверка TestMachine на запуск тестов несуществующего класса")
    void noExistClass() {
        var actual = TestMachine.run("ru.otus.andrk.tests.BigTest1");
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск тестов с NonDefault конструктором")
    void testWithNotDefaultConstructor() {
        var actual = TestMachine.run(TestWithNotDefaultConstructor.class);
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск тестов с несколькими конструкторами")
    void testWithMultipleConstructors() {
        var actual = TestMachine.run(TestWithMultipleConstructors.class);
        assertThat(actual.getTestsFailure()).isEqualTo(0);
        assertThat(actual.getTestsSuccess()).isEqualTo(1);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск тестов без аннотации")
    void testWithNotAnnotated() {
        var actual = TestMachine.run(TestWithoutClassAnotation.class);
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск тестов с двойной аннотацией")
    void testWithDuplicateAnnotations() {
        var actual = TestMachine.run(TestWithDuplicateAnnotations.class);
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск тестов без тестов")
    void testWithoutTests() {
        var actual = TestMachine.run(TestWithoutTests.class);
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск тестов с некорректными сигнатурами")
    void testWithInvalidSignatures() {
        Class<?>[] tests = new Class[]{TestWithInvalidSignature1.class, TestWithInvalidSignature2.class,
                TestWithInvalidSignature3.class, TestWithInvalidSignature4.class,
                TestWithInvalidSignature5.class, TestWithInvalidSignature6.class};
        for (var test : tests) {
            var actual = TestMachine.run(test);
            assertThat(actual.getTestsFailure()).isEqualTo(1);
            assertThat(actual.getTestsSuccess()).isEqualTo(0);
        }
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск теста с указанием явного конструктора без параметров")
    void testWithDefaultConstructor() {
        var actual = TestMachine.run(TestWithDefaultConstructor.class);
        assertThat(actual.getTestsFailure()).isEqualTo(0);
        assertThat(actual.getTestsSuccess()).isEqualTo(1);
        assertThat(actual.getResults().size()).isEqualTo(1);
        assertThat(actual.getResults().stream().map(OneTestStatistic::isSuccess).toList().get(0)).isEqualTo(true);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск теста без указания явного конструктора")
    void testWithoutConstructor() {
        var actual = TestMachine.run(TestWithoutConstructor.class);
        assertThat(actual.getTestsFailure()).isEqualTo(0);
        assertThat(actual.getTestsSuccess()).isEqualTo(1);
        assertThat(actual.getResults().size()).isEqualTo(1);
        assertThat(actual.getResults().stream().map(OneTestStatistic::isSuccess).toList().get(0)).isEqualTo(true);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск теста с ошибкой в инициализаторе")
    void testWithInitError() {
        var actual = TestMachine.run(TestWithInitError.class);
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
        //assertThat(actual.getResults().stream().map(x->x.isSuccess()).collect(Collectors.toList()).get(0)).isEqualTo(false);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск теста с ошибкой в финализаторе")
    void testWithFinishError() {
        var actual = TestMachine.run(TestWithFinishError.class);
        assertThat(actual.getTestsFailure()).isEqualTo(2);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск всех тестов включая тесты с ошибками")
    void testMultipleTestContainErrors() {
        var actual = TestMachine.run(TestWithMultipleTests.class);
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        assertThat(actual.getTestsSuccess()).isEqualTo(2);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск всех тестов не взирая на модификатор доступа")
    void testWithAnyModifiers() {
        var actual = TestMachine.run(TestWithAnyModifiers.class);
        assertThat(actual.getTestsFailure()).isEqualTo(0);
        assertThat(actual.getTestsSuccess()).isEqualTo(4);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск каждого теста в отдельном экземпляре класса")
    void testOnEachRunInOtherInstance() {
        SpyWithInstanceLogging.log.clear();
        var actual = TestMachine.run(SpyWithInstanceLogging.class);
        assertThat(actual.getTestsFailure()).isEqualTo(0);
        assertThat(actual.getTestsSuccess()).isEqualTo(2);
        var log = SpyWithInstanceLogging.log;
        assertThat(log.size()).isEqualTo(2);
        var log1 = log.firstEntry().getValue();
        var log2 = log.lastEntry().getValue();
        assertThat(log1.size()).isEqualTo(4);
        assertThat(log2.size()).isEqualTo(4);
        for (int i = 0; i < 4; i++) {
            var val1 = log1.pollFirst();
            var val2 = log2.pollFirst();
            if (i == 2) {
                assert val1 != null;
                assertThat(val1.equals(val2)).isFalse();
                assertThat(val1).startsWith("test");
                assertThat(val2).startsWith("test");
            } else {
                assert val1 != null;
                assertThat(val1.equals(val2)).isTrue();
            }
            if (i == 0) {
                assertThat(val1).isEqualTo("constructor");
            } else if (i == 1) {
                assertThat(val1).isEqualTo("init");
            } else if (i == 3) {
                assertThat(val1).isEqualTo("finish");
            }
        }
    }
}
