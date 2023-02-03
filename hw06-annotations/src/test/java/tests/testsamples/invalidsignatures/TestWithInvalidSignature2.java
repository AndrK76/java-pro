package tests.testsamples.invalidsignatures;

import ru.otus.andrk.annotations.After;
import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;
@Test
public class TestWithInvalidSignature2 {


    @Before
    public void invalidBefore2(String arg) {
    }

    @Test
    public void doTest(){}
}
