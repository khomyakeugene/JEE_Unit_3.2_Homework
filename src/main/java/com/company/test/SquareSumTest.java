package com.company.test;

import com.company.calculation.SquareSumUsingFuture;
import com.company.calculation.SquareSumUsingPhaser;

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
    private final static int NUMBER_OF_THREADS = 4;

    private int[] generateTestData() {
        return new Random().ints(ARRAY_SIZE, ELEMENT_VALUE_ORIGIN, ELEMENT_VALUE_BOUND).toArray();
    }

    public void demonstrate(boolean executionIllustrate, int sleepingIntervalBound) {
        int[] testData = generateTestData();

        System.out.println(String.format(IMPLEMENTATION_INFORMATION_PATTERN, SquareSumUsingFuture.class.getName()));
        System.out.println(String.format(RESULT_MESSAGE_PATTERN, testData.length, ELEMENT_VALUE_ORIGIN, ELEMENT_VALUE_BOUND,
                NUMBER_OF_THREADS, new SquareSumUsingFuture(executionIllustrate, sleepingIntervalBound).
                        getSquareSum(testData, NUMBER_OF_THREADS)));

        System.out.println(String.format(IMPLEMENTATION_INFORMATION_PATTERN, SquareSumUsingPhaser.class.getName()));
        System.out.println(String.format(RESULT_MESSAGE_PATTERN, testData.length, ELEMENT_VALUE_ORIGIN, ELEMENT_VALUE_BOUND,
                NUMBER_OF_THREADS, new SquareSumUsingPhaser(executionIllustrate, sleepingIntervalBound).
                        getSquareSum(testData, NUMBER_OF_THREADS)));
    }
}
