package tests.testsamples;

import ru.otus.andrk.annotations.After;
import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;

@Test
public class TestWithFinishError {
    @Before
    public void init(){
    }

    @Test
    public void doTest1(){
    }
    @Test
    public void doTest2(){
    }

    @After
    public void  finish(){
        throw new RuntimeException("err");
    }
}
