package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.andrk.tester.OneTestStatistic;
import ru.otus.andrk.tester.RunOneTestStatistic;
import ru.otus.andrk.tester.RunTestStatistics;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class RunTestStatisticsTest {
    @Test
    @DisplayName("Проверка RunTestStatistics на создание")
    void creationDefault() {
        String testsName = "names";
        var actual = RunTestStatistics.create(testsName);
        assertThat(actual.getTestsName()).isEqualTo(testsName);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
        assertThat(actual.getTestsFailure()).isEqualTo(0);
        var items = new ArrayList<>(actual.getResults());
        assertThat(items)
                .isNotNull()
                .isInstanceOf(Collection.class)
                .asList()
                .hasOnlyElementsOfType(OneTestStatistic.class)
                .size().isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка RunTestStatistics на создание ошибочного результата")
    void creationFailureResult() {
        String testsName = "names";
        var fail = RunOneTestStatistic.createFailure("name", LocalDateTime.now(), LocalDateTime.now(), new RuntimeException());
        var actual = RunTestStatistics.createWithResult(testsName, fail);
        assertThat(actual.getTestsName()).isEqualTo(testsName);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        var items = new ArrayList<>(actual.getResults());
        assertThat(items)
                .isNotNull()
                .isInstanceOf(Collection.class)
                .asList()
                .hasOnlyElementsOfType(OneTestStatistic.class)
                .size().isEqualTo(1);
    }


    @Test
    @DisplayName("Проверка RunTestStatistics на добавление элементов")
    void addValues() {
        var fail = RunOneTestStatistic.createFailure("name", LocalDateTime.now(), LocalDateTime.now(), new RuntimeException());
        var success1 = RunOneTestStatistic.createSuccess("name", LocalDateTime.now(), LocalDateTime.now());
        var success2 = RunOneTestStatistic.createSuccess("name", LocalDateTime.now(), LocalDateTime.now());
        String testsName = "names";
        var actual = RunTestStatistics.create(testsName);
        actual.addResult(success1);
        actual.addResult(fail);
        actual.addResult(success2);
        assertThat(actual.getTestsSuccess()).isEqualTo(2);
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        var items = new ArrayList<>(actual.getResults());
        assertThat(items)
                .isNotNull()
                .isInstanceOf(Collection.class)
                .asList()
                .hasOnlyElementsOfType(OneTestStatistic.class)
                .containsAll(Arrays.asList(success1, success2, fail))
                .size().isEqualTo(3);
    }

    @Test
    @DisplayName("Проверка RunTestStatistics на запрет изменения результатов")
    void testUnmodified() {
        String testsName = "names";
        var actual = RunTestStatistics.create(testsName);
        var success = RunOneTestStatistic.createSuccess("name", LocalDateTime.now(), LocalDateTime.now());
        var fail = RunOneTestStatistic.createFailure("name", LocalDateTime.now(), LocalDateTime.now(), new RuntimeException());
        assertThrows(UnsupportedOperationException.class, () -> actual.getResults().add(fail));
        actual.addResult(fail);
        assertThrows(ClassCastException.class, () ->
                ((List<OneTestStatistic>) actual.getResults()).set(0, success));
        assertThrows(UnsupportedOperationException.class, () -> actual.getResults().clear());
    }
}
