package ru.otus.andrk.testsamples.InvalidSignatures;

import ru.otus.andrk.annotations.After;
import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;

@Test
public class TestWithInvalidSignature6 {

    @Test
    public void doTest(){}

    @After
    public void invalidAfter2(String arg) {
    }
}
