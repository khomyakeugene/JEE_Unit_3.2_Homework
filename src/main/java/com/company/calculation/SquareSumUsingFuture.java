package com.company.calculation;

import com.company.calculation.CalcSquareSumPart;
import com.company.calculation.SquareSum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Yevgen on 02.04.2016 as a part of the project "JEE_Unit_3.2_Homework".
 */
public class SquareSumUsingFuture implements SquareSum {
    private static final String PROBLEMS_PATTERN =
            "Class name: %s: There were the problems with the calculation of the result, and, unfortunately, the result is unachievable";
    private static final String START_TO_GET_RESULTS_PATTERN = "Start to get result from %d threads ...";

    private boolean executionIllustrate;
    private int sleepingIntervalBound;

    public SquareSumUsingFuture(boolean executionIllustrate, int sleepingIntervalBound) {
        this.executionIllustrate = executionIllustrate;
        this.sleepingIntervalBound = sleepingIntervalBound;
    }

    @Override
    public long getSquareSum(int[] values, int numberOfThreads) {
        int elementQuantity = values.length / numberOfThreads;

        // Add tasks to execute list
        int startIndex = 0;
        List<Callable<Long>> squareSumCalculation = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            int thisPortion = elementQuantity + ((i == 0) ? (values.length % numberOfThreads) : 0);
            squareSumCalculation.add(new CalcSquareSumPart(values, startIndex, thisPortion,
                    executionIllustrate, sleepingIntervalBound));
            startIndex += thisPortion;
        }

        // At first, result is unknown
        long result = -1L;

        // Execute the tasks
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            List<Future<Long>> resultParts;
            try {
                resultParts = executor.invokeAll(squareSumCalculation);
            } catch (InterruptedException e) {
                e.printStackTrace();
                resultParts = null;
            }

            if (executionIllustrate) {
                System.out.println(String.format(START_TO_GET_RESULTS_PATTERN, numberOfThreads));
            }
            // Get and process the results
            if (resultParts != null) {
                result = 0L;
                for (Future<Long> longFuture : resultParts) {
                    try {
                        result += longFuture.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        result = -1L;
                    }
                }
            }

            // Check the result
            if (result == -1L) {
                System.out.println(String.format(PROBLEMS_PATTERN, getClass().getName()));
            }
        } finally {
            executor.shutdown();
        }

        return result;
    }
}
