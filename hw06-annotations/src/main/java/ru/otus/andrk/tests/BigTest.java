package ru.otus.andrk.tests;

import ru.otus.andrk.annotations.After;
import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;
import ru.otus.andrk.annotations.TestName;

@Test
public class BigTest {
    @Before
    private void initTest() {
    }

    @Test
    @TestName("Произвольный тест")
    public void doSameTest() {

    }

    @After
    void finishTest() {

    }

}
