package ru.otus.andrk;

import ru.otus.andrk.tester.TestMachine;
import ru.otus.andrk.testsamples.*;
import ru.otus.andrk.testsamples.InvalidSignatures.*;

public class Main {
    public static void main(String[] args) {

//        System.out.println("***Start \"No Exist Test\"***");
//        var testRes = TestMachine.Run("ru.otus.andrk.tests.BigTest1");
//        System.out.println(testRes);
//        System.out.println();
//
//        System.out.println("***Start class WithoutGenericConstructor***");
//        testRes = TestMachine.Run(TestWithNotDefaultConstructor.class);
//        System.out.println(testRes);
//        System.out.println();
//
//        System.out.println("***Start class WithoutClassAnotations***");
//        testRes = TestMachine.Run(TestWithoutClassAnotation.class);
//        System.out.println(testRes);
//        System.out.println();
//
//        System.out.println("***Start class Test WithInvalidSignature***");
//        testRes = TestMachine.Run(TestWithInvalidSignature1.class);
//        System.out.println(testRes);
//        System.out.println();

        {
            System.out.println("***Start class ru.otus.andrk.tests.BigTest.class***");
            var testRes1 = TestMachine.Run(BigTest.class);
            testRes1 = TestMachine.Run(TestWithInvalidSignature2.class);
            System.out.println(testRes1);
            System.out.println();
        }

    }
}