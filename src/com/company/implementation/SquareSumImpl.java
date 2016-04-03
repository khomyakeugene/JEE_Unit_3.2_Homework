package com.company.implementation;

import com.company.interfaces.SquareSum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by Yevgen on 02.04.2016 as a part of the project "JEE_Unit_3.2_Homework".
 */
public class SquareSumImpl implements SquareSum {
    private static final String PROBLEMS_PATTERN =
            "Class name: %s: There were the problems with the calculation of the result, and, unfortunately, the result is unachievable";
    private static final String START_TO_GET_RESULTS_PATTERN = "Start to get result from %d threads ...";
    private static final String PHASER_ARRIVE_AND_AWAIT_ADVANCE_PATTERN = "Phaser %s is going to register another arriving and awaiting ...";

    private boolean showDiagnostic;

    private Phaser phaser;

    public SquareSumImpl(boolean showDiagnostic) {
        this.showDiagnostic = showDiagnostic;
    }

    private class CalcSquareSumPart implements Callable<Long> {
        private static final int SLEEPING_INTERVAL_BOUND = 5000;
        private static final String DIAGNOSTIC_PATTERN = "Class name: %s, Thread: %s, startIndex: %d, quantity of elements: %d, result: %d";
        private static final String SLEEPING_PATTERN = "Class name: %s, Thread: %s has done its work and is sleeping for %d ms  ...";

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
                int sleepingInterval = new Random().nextInt(SLEEPING_INTERVAL_BOUND);
                System.out.println(String.format(SLEEPING_PATTERN, getClass().getName(), Thread.currentThread().getName(), sleepingInterval));
                try {
                    Thread.sleep(sleepingInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(String.format(DIAGNOSTIC_PATTERN, getClass().getName(), Thread.currentThread().getName(), startIndex, elementQuantity, result));
            }

            return result;
        }

        @Override
        public Long call() throws Exception {
            return getSquareSum();
        }
    }

    private void await() {
        if (showDiagnostic) {
            System.out.println(String.format(PHASER_ARRIVE_AND_AWAIT_ADVANCE_PATTERN, phaser.getClass().getName()));
        }
        phaser.arriveAndAwaitAdvance();

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

        long result = 0L;

        // Prepare barrier
        phaser = new Phaser(numberOfThreads);

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
                long results[] = new long[resultParts.size()];
                for (Future<Long> longFuture : resultParts) {
                    try {
                        result += longFuture.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        resultParts = null;
                    }
                }
            }

            // Check the result
            if (resultParts == null) {
                System.out.println(String.format(PROBLEMS_PATTERN, getClass().getName()));
            }
        } finally {
            executor.shutdown();
        }

        return result;
    }
}
