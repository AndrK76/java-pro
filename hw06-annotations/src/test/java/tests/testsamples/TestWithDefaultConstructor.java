package tests.testsamples;

import ru.otus.andrk.annotations.After;
import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;

@Test
public class TestWithDefaultConstructor {
    public TestWithDefaultConstructor() {
    }

    @Before
    public void init(){
    }

    @Test
    public void doTest(){
    }

    @After
    public void  finish(){
    }
}
