package ru.otus.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import static org.assertj.core.api.Assertions.assertThat;

public class ProcessorSwitchFieldsTest {

    @Test
    @DisplayName("Проверка процессора переставляющего field11 и field12")
    void testProcessorSwitchFields(){
        final String value11 = "VALUE11";
        final String value12 = "VALUE12";

        var origMesssage = new Message.Builder(3L).field11(value11).field12(value12).build();
        var processor = new ProcessorSwitchFields();
        var resMessage = processor.process(origMesssage);

        assertThat(origMesssage.getField11()).isEqualTo(value11);
        assertThat(origMesssage.getField12()).isEqualTo(value12);
        assertThat(resMessage.getField11()).isEqualTo(origMesssage.getField12());
        assertThat(resMessage.getField12()).isEqualTo(origMesssage.getField11());
    }
}
