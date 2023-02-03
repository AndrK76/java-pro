package tests.testsamples;

import ru.otus.andrk.annotations.Test;

@Test
public class TestWithNotDefaultConstructor {
    public TestWithNotDefaultConstructor(String name) {
    }

    @Test
    public void doTest(){}
}
