package ru.otus.andrk.testsamples;

import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;

public class TestWithDuplicateAnnotations {
    @Test
    @Before
    public void testMethod() {
    }

}
