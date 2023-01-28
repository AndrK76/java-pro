package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.andrk.tester.RunOneTestStatistic;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RunOneTestStatisticTest {

    @Test
    @DisplayName("Проверка RunOneTestStatistic на создание успешного результата")
    void creationSuccessResult() {
        //given
        String expectedName = "name test";
        LocalDateTime dt1 = LocalDateTime.of(2000, Month.JANUARY,1,0,0,0);
        LocalDateTime dt2 = LocalDateTime.now();
        var ret = RunOneTestStatistic.createSuccess(expectedName,dt1,dt2);
        assertThat(ret.isSuccess()).isEqualTo(true);
        assertThat(ret.getNameTest()).isEqualTo(expectedName);
        assertThat(ret.getStartTime()).isEqualTo(dt1);
        assertThat(ret.getEndTime()).isEqualTo(dt2);
    }

    @Test
    @DisplayName("Проверка RunOneTestStatistic на создание ошибочного результата")
    void creationFailureResult() {
        //given
        String expectedName = "name test";
        LocalDateTime dt1 = LocalDateTime.of(2000, Month.JANUARY,1,0,0,0);
        LocalDateTime dt2 = LocalDateTime.now();
        NullPointerException innerEx = new NullPointerException("inner");
        IllegalArgumentException outerEx = new IllegalArgumentException("outer",innerEx);
        var actual = RunOneTestStatistic.createFailure(expectedName,dt1,dt2,outerEx);
        assertThat(actual.isSuccess()).isEqualTo(false);
        assertThat(actual.getNameTest()).isEqualTo(expectedName);
        assertThat(actual.getStartTime()).isEqualTo(dt1);
        assertThat(actual.getEndTime()).isEqualTo(dt2);
        assertThat(actual.getError()).isNotNull().isInstanceOf(IllegalArgumentException.class);
        assertThat(actual.getError().getMessage()).isEqualTo("outer");
        assertThat(actual.getError().getCause()).isNotNull().isInstanceOf(NullPointerException.class);
        assertThat(actual.getError().getCause().getMessage()).isEqualTo("inner");
    }

}
