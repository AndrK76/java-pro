package ru.otus.andrk.annotations;

import java.lang.annotation.*;

/**
 * <p>{@code @Test} для метода указывает
 * что метод <em>является тестом</em></p>
 * Метод {@code @Test}<ol>
 * <li>не должен возвращать никакого значения</li>
 * <li>не имеет аргументов</li></ol>
 * <p>{@code @Test} для типа указывает
 * что тип <em>содержит тесты</em></p>
 * <p>Тип {@code @Test}
 * должен содержать конструктор без параметров
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Test {
}
