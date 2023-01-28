package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.andrk.tester.TestMachine;
import ru.otus.andrk.testsamples.*;
import ru.otus.andrk.testsamples.InvalidSignatures.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestMachineTest {
    @Test
    @DisplayName("Проверка TestMachine на запуск тестов несуществующего класса")
    void noExistClass() {
        var actual = TestMachine.Run("ru.otus.andrk.tests.BigTest1");
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
    }
    @Test
    @DisplayName("Проверка TestMachine на запуск тестов с NonDefault конструктором")
    void  testWithNotDefaultConstructor(){
        var actual = TestMachine.Run(TestWithNotDefaultConstructor.class);
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск тестов без аннотации")
    void  testWithNotAnnotated(){
        var actual = TestMachine.Run(TestWithoutClassAnotation.class);
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск тестов с двойной аннотацией")
    void  testWithDuplicateAnnotations(){
        var actual = TestMachine.Run(TestWithDuplicateAnnotations.class);
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
    }
    @Test
    @DisplayName("Проверка TestMachine на запуск тестов без тестов")
    void  testWithoutTests(){
        var actual = TestMachine.Run(TestWithoutTests.class);
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка TestMachine на запуск тестов с некорректными сигнатурами")
    void  testWithInvalidSignatures(){
        Class<?>[] tests = new Class[]{TestWithInvalidSignature1.class,TestWithInvalidSignature2.class,
                TestWithInvalidSignature3.class,TestWithInvalidSignature4.class,
                TestWithInvalidSignature5.class,TestWithInvalidSignature6.class};
        for (var test: tests             )
        {
            var actual = TestMachine.Run(test);
            assertThat(actual.getTestsFailure()).isEqualTo(1);
            assertThat(actual.getTestsSuccess()).isEqualTo(0);
        }
    }
}
