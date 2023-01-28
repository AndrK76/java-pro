package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.andrk.tester.RunOneTestStatistic;
import ru.otus.andrk.tester.RunTestStatistics;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RunTestStatisticsTest {
    @Test
    @DisplayName("Проверка RunTestStatistics на создание")
    void creationDefault() {
        String testsName = "names";
        var actual = RunTestStatistics.create(testsName);
        assertThat(actual.getTestsName()).isEqualTo(testsName);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
        assertThat(actual.getTestsFailure()).isEqualTo(0);
        assertThat(actual.getResults())
                .isNotNull()
                .isInstanceOf(Collection.class)
                .asList()
                    .hasOnlyElementsOfType(RunOneTestStatistic.class)
                    .size().isEqualTo(0);
    }
    @Test
    @DisplayName("Проверка RunTestStatistics на создание ошибочного результата")
    void creationFailureResult() {
        String testsName = "names";
        var fail = RunOneTestStatistic.createFailure("name", LocalDateTime.now(),LocalDateTime.now(),new RuntimeException());
        var actual = RunTestStatistics.createWithFailedResult(testsName, fail);
        assertThat(actual.getTestsName()).isEqualTo(testsName);
        assertThat(actual.getTestsSuccess()).isEqualTo(0);
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        assertThat(actual.getResults())
                .isNotNull()
                .isInstanceOf(Collection.class)
                .asList()
                    .hasOnlyElementsOfType(RunOneTestStatistic.class)
                    .size().isEqualTo(1);
        //assertThat(actual.getResults().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Проверка RunTestStatistics на добавление элементов")
    void addValues() {
        var fail = RunOneTestStatistic.createFailure("name", LocalDateTime.now(),LocalDateTime.now(),new RuntimeException());
        var success1 = RunOneTestStatistic.createSuccess("name", LocalDateTime.now(),LocalDateTime.now());
        var success2 = RunOneTestStatistic.createSuccess("name", LocalDateTime.now(),LocalDateTime.now());
        String testsName = "names";
        var actual = RunTestStatistics.create(testsName);
        actual.addResult(success1);
        actual.addResult(fail);
        actual.addResult(success2);
        assertThat(actual.getTestsSuccess()).isEqualTo(2);
        assertThat(actual.getTestsFailure()).isEqualTo(1);
        assertThat(actual.getResults())
                .isNotNull()
                .isInstanceOf(Collection.class)
                .asList()
                    .hasOnlyElementsOfType(RunOneTestStatistic.class)
                    .containsAll(Arrays.asList(success1,success2,fail))
                    .size().isEqualTo(3);
    }
}
