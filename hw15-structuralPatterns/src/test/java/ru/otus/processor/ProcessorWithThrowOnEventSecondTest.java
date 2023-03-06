package ru.otus.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.InvalidTimeException;
import ru.otus.processor.homework.ProcessorWithThrowOnEventSecond;
import ru.otus.processor.homework.TimeProvider;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class ProcessorWithThrowOnEventSecondTest {

    @Test
    @DisplayName("Проверка тестового провайдера времени на корректное возвращение чётных и нечётных секунд")
    void testTestTimeProviderOnEvenSecondsResult()
    {
        var timeProvider = new TestTimeProvider();
        timeProvider.setReturnEvenSecondTime(false);
        assertThat(timeProvider.now().getSecond() % 2).isEqualTo(1);
        timeProvider.setReturnEvenSecondTime(true);
        assertThat(timeProvider.now().getSecond() % 2).isEqualTo(0);
    }


    @Test
    @DisplayName("Проверка процессора на отсутствие ошибки в момент времени с нечётными секундами")
    void testProcessorOnNoErrorInOddSecondTime() {
        var timeProvider = new TestTimeProvider();
        var message = new Message.Builder(4L).build();
        var processor = new ProcessorWithThrowOnEventSecond(timeProvider);
        timeProvider.setReturnEvenSecondTime(false);
        assertThatNoException().isThrownBy(()-> processor.process(message));
    }

    @Test
    @DisplayName("Проверка процессора на возникновение ошибки в момент времени с чётными секундами")
    void testProcessorOnRaiseErrorInEvenSecondTime() {
        var timeProvider = new TestTimeProvider();
        var message = new Message.Builder(4L).build();
        var processor = new ProcessorWithThrowOnEventSecond(timeProvider);
        timeProvider.setReturnEvenSecondTime(true);
        assertThatExceptionOfType(InvalidTimeException.class).isThrownBy(()-> processor.process(message));
    }

}

class TestTimeProvider implements TimeProvider {

    private boolean returnEvenSecondTime = false;

    public void setReturnEvenSecondTime(boolean returnEvenSecondTime) {
        this.returnEvenSecondTime = returnEvenSecondTime;
    }

    @Override
    public LocalDateTime now() {
        LocalDateTime ret = LocalDateTime.now().withNano(0);
        if (returnEvenSecondTime) {
            return ret.withSecond(10);
        } else {
            return ret.withSecond(9);
        }
    }
}