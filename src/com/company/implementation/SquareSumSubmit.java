package com.company.implementation;

import com.company.interfaces.SquareSum;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * Created by Yevgen on 03.04.2016 as a part of the project "JEE_Unit_3.2_Homework".
 */
public class SquareSumSubmit implements SquareSum {
    private static final String PHASER_ARRIVE_AND_AWAIT_ADVANCE_PATTERN = "Phaser %s is going to register another arriving and awaiting ...";
    private static final String TASK_WAS_SUBMITTED_PATTERN = "Task number %d was submitted";

    private boolean showDiagnostic;

    private Phaser phaser;

    public SquareSumSubmit(boolean showDiagnostic) {
        this.showDiagnostic = showDiagnostic;
    }

    private void await() {
        if (showDiagnostic) {
            System.out.println(String.format(PHASER_ARRIVE_AND_AWAIT_ADVANCE_PATTERN, phaser.getClass().getName()));
        }
        phaser.arriveAndAwaitAdvance();

    }

    @Override
    public long getSquareSum(int[] values, int numberOfThreads) {
        long result = 0L;

        //phaser = new Phaser(numberOfThreads);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        try {
            int startIndex = 0;
            int elementQuantity = values.length / numberOfThreads;
            for (int i = 0; i < numberOfThreads; i++) {
                int thisPortion = elementQuantity + ((i == 0) ? (values.length % numberOfThreads) : 0);
                executor.submit(new CalcSquareSumPartCallable(values, startIndex, thisPortion, showDiagnostic));
                if (showDiagnostic) {
                    System.out.println(String.format(TASK_WAS_SUBMITTED_PATTERN, i));
                }

                startIndex += thisPortion;
            }

        } finally {
            executor.shutdown();
        }

        return result;
    }
}
