package tests.testsamples;

import ru.otus.andrk.annotations.After;
import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;

@Test
public class TestWithMultipleTests {
    @Before
    public void init() {
    }

    @Test
    public void doTest1() {
    }

    @Test
    public void doTest2() {
        throw new RuntimeException("test2");
    }
    @Test
    public void doTest3() {
    }

    @After
    public void finish() {
    }
}
