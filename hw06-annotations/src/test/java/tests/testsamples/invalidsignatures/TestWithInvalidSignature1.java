package tests.testsamples.invalidsignatures;

import ru.otus.andrk.annotations.After;
import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;

@Test
public class TestWithInvalidSignature1 {

    @Before
    public int invalidBefore1() {
        return 1;
    }

    @Test
    public void doTest(){}

}
