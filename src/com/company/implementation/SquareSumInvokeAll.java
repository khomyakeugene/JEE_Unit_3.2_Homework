package com.company.implementation;

import com.company.interfaces.SquareSum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Yevgen on 02.04.2016 as a part of the project "JEE_Unit_3.2_Homework".
 */
public class SquareSumInvokeAll implements SquareSum {
    private static final String PROBLEMS_PATTERN =
            "Class name: %s: There were the problems with the calculation of the result, and, unfortunately, the result is unachievable";
    private static final String START_TO_GET_RESULTS_PATTERN = "Start to get result from %d threads ...";

    private boolean showDiagnostic;

    public SquareSumInvokeAll(boolean showDiagnostic) {
        this.showDiagnostic = showDiagnostic;
    }

    @Override
    public long getSquareSum(int[] values, int numberOfThreads) {
        int elementQuantity = values.length / numberOfThreads;

        // Add tasks to execute list
        int startIndex = 0;
        List<Callable<Long>> squareSumCalculation = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            int thisPortion = elementQuantity + ((i == 0) ? (values.length % numberOfThreads) : 0);
            squareSumCalculation.add(new CalcSquareSumPartCallable(values, startIndex, thisPortion, showDiagnostic));
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

            if (showDiagnostic) {
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
