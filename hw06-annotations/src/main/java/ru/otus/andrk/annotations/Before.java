package ru.otus.andrk.annotations;

import java.lang.annotation.*;

/**
 * <p>{@code @Before} указывает
 * что метод <em>должен запускаться перед началом теста</em>.</p>
 *
 * Метод {@code @Before}<ol>
 * <li>не должен возвращать никакого значения</li>
 * <li>не имеет аргументов</li></ol>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Before {
}
