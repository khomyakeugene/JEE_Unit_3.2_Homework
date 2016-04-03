package com.company.test;

import com.company.implementation.SquareSumSubmit;

import java.util.Random;

/**
 * Created by Yevgen on 02.04.2016 as a part of the project "JEE_Unit_3.2_Homework".
 */
public class SquareSumTest {
    private final static String IMPLEMENTATION_INFORMATION_PATTERN = "The <%s> execution ... ";
    private final static String RESULT_MESSAGE_PATTERN =
            "The result of the square sum of the %d random generating from %d to %d integer elements, " +
                    "calculating by using %d threads, is: %d";

    private final static int ARRAY_SIZE = 100000;
    private final static int ELEMENT_VALUE_ORIGIN = 0;
    private final static int ELEMENT_VALUE_BOUND = 1000;
    private final static int NUMBER_OF_THREADS = 3;

    private int[] generateTestData() {
        return new Random().ints(ARRAY_SIZE, ELEMENT_VALUE_ORIGIN, ELEMENT_VALUE_BOUND).toArray();
    }

    public void demonstrate(boolean showDiagnostic) {
/*
        System.out.println(String.format(IMPLEMENTATION_INFORMATION_PATTERN, SquareSumInvokeAll.class.getName()));
        System.out.println(String.format(RESULT_MESSAGE_PATTERN, ARRAY_SIZE, ELEMENT_VALUE_ORIGIN, ELEMENT_VALUE_BOUND,
                NUMBER_OF_THREADS, new SquareSumInvokeAll(showDiagnostic).getSquareSum(generateTestData(), NUMBER_OF_THREADS)));
*/
        System.out.println(String.format(IMPLEMENTATION_INFORMATION_PATTERN, SquareSumSubmit.class.getName()));
        System.out.println(String.format(RESULT_MESSAGE_PATTERN, ARRAY_SIZE, ELEMENT_VALUE_ORIGIN, ELEMENT_VALUE_BOUND,
                NUMBER_OF_THREADS, new SquareSumSubmit(showDiagnostic).getSquareSum(generateTestData(), NUMBER_OF_THREADS)));

        /*
        System.out.println(String.format(RESULT_MESSAGE_PATTERN, ARRAY_SIZE, ELEMENT_VALUE_ORIGIN, ELEMENT_VALUE_BOUND,
                NUMBER_OF_THREADS, new SquareSumInvokeAll(showDiagnostic).getSquareSum(new int[] {1,2,3,4,5,6,7,8,9, 10, 11}, 3)));
                */

    }
}
