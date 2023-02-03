package ru.otus.andrk.annotations;

import java.lang.annotation.*;

/**
 * <p>Аннотация {@code @TestName} позволяет указать
 * дополнительное название теста</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TestName {
    /**
     * Дополнительное название теста
     * @return дополнительное название
     */
    String value();
}
