package tests.testsamples;

import ru.otus.andrk.annotations.After;
import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;

@Test
public class TestWithInitError {
    @Before
    public void init(){
        throw new RuntimeException("err");
    }

    @Test
    public void doTest(){
    }

    @After
    public void  finish(){
    }


}
