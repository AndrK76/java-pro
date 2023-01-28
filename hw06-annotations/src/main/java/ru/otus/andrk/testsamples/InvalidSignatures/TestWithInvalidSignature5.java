package ru.otus.andrk.testsamples.InvalidSignatures;

import ru.otus.andrk.annotations.After;
import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;

@Test
public class TestWithInvalidSignature5 {

    @After
    public int invalidAfter1() {
        return 1;
    }

    @Test
    public void doTest(){}

}
