package com.company.implementation;

import com.company.interfaces.SquareSum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Yevgen on 02.04.2016 as a part of the project "JEE_Unit_3.2_Homework".
 */
public class SquareSumImpl implements SquareSum {
    private static final String PROBLEMS_PATTERN =
            "There were the problems with the calculation of the result, and, unfortunately, the result is unachievable";

    private boolean showDiagnostic;

    public SquareSumImpl(boolean showDiagnostic) {
        this.showDiagnostic = showDiagnostic;
    }

    private class CalcSquareSumPart implements Callable<Long> {
        private static final String DIAGNOSTIC_PATTERN = "Class name: %s, Thread: %s, startIndex: %d, quantity of elements: %d, result: %d";

        private int[] values;
        private int startIndex;
        private int elementQuantity;
        boolean showDiagnostic;

        CalcSquareSumPart(int[] values, int startIndex, int elementQuantity, boolean showDiagnostic) {
            this.values = values;
            this.startIndex = startIndex;
            this.elementQuantity = elementQuantity;
            this.showDiagnostic = showDiagnostic;
        }

        long getSquareSum() {
            long result = 0L;

            int upperLimit = startIndex + elementQuantity;
            upperLimit = upperLimit <= values.length ? upperLimit : values.length;
            for (int i = startIndex; i < upperLimit; i++) {
                result += Math.pow(values[i], 2);
            }

            if (showDiagnostic) {
                System.out.println(String.format(DIAGNOSTIC_PATTERN, getClass().getName(), Thread.currentThread().getName(), startIndex, elementQuantity, result));

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            return result;
        }

        @Override
        public Long call() throws Exception {
            return getSquareSum();
        }
    }

    @Override
    public long getSquareSum(int[] values, int numberOfThreads) {
        int elementQuantity = values.length / numberOfThreads;

        // Add tasks to execute list
        int startIndex = 0;
        List<Callable<Long>> squareSumCalculation = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            int thisPortion = elementQuantity + ((i == 0) ? (values.length % numberOfThreads) : 0);
            squareSumCalculation.add(new CalcSquareSumPart(values, startIndex, thisPortion, showDiagnostic));
            startIndex += thisPortion;
        }

        // Execute the tasks
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<Long>> result;
        try {
            result = executor.invokeAll(squareSumCalculation);
        } catch (InterruptedException e) {
            e.printStackTrace();
            result = null;
        }

        // Get the results
        if (result != null) {
            for (Future<Long> longFuture : result) {
                try {
                    System.out.println(longFuture.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    result = null;
                }
            }
        }

        // Check the result
        if (result == null) {
            System.out.println(PROBLEMS_PATTERN);
        }

        return 0L;
    }


}
