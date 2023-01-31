package tests.testsamples;

import ru.otus.andrk.annotations.Test;

@Test
public class TestWithMultipleConstructors {
    public TestWithMultipleConstructors(String pars) {
    }

    public TestWithMultipleConstructors() {
    }

    @Test
    void doTest(){}
}
