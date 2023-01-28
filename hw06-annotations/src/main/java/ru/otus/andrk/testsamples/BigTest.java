package ru.otus.andrk.testsamples;

import ru.otus.andrk.annotations.After;
import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;
import ru.otus.andrk.annotations.TestName;

@Test
public class BigTest {


    @Before
    private void initTest() {
    }
    @Before
    private void initTest2() {
    }

    @Test
    @TestName("Произвольный тест")
    public void doSameTest() {
    }


    @Test
    public void doAnotherTest() {
    }

    @After
    void finishTest() {

    }

}
