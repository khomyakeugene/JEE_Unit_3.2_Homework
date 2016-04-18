package com.company.calculation;

import java.util.concurrent.ExecutionException;

/**
 * Created by Yevgen on 02.04.2016 as a part of the project "JEE_Unit_3.2_Homework".
 */
public interface SquareSum {
    long getSquareSum(int[] values, int numberOfThreads) throws InterruptedException, ExecutionException;
}
