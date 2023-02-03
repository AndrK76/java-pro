package tests.testsamples;

import ru.otus.andrk.annotations.After;
import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;

import java.util.*;


@Test
public class SpyWithInstanceLogging {
    public static TreeMap<Integer, LinkedList<String>> log = new TreeMap<>();

    public SpyWithInstanceLogging() {
        LinkedList<String> data = new LinkedList<>(List.of("constructor"));
        log.put(idObjet.hashCode(), data);
    }

    private Object idObjet = new Object();

    private LinkedList<String> getCurrLog() {
        return log.get(idObjet.hashCode());
    }

    @Before
    void init() {
        getCurrLog().add("init");
    }

    @Test
    void test1() {
        getCurrLog().add("test1");
    }

    @Test
    void test2() {
        getCurrLog().add("test2");
    }

    @After
    void finish() {
        getCurrLog().add("finish");
    }

}
