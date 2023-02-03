package tests.testsamples;

import ru.otus.andrk.annotations.Test;

@Test
public class TestWithAnyModifiers {
    @Test
    public void doTest1() {
    }

    @Test
    void doTest2() {
    }
    @Test
    protected void doTest3() {
    }
    @Test
    private void doTest4() {
    }
}
