package com.company.implementation;

import com.company.interfaces.SquareSum;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * Created by Yevgen on 03.04.2016 as a part of the project "JEE_Unit_3.2_Homework".
 */
public class SquareSumUsingPhaser implements SquareSum {
    private static final int PRE_END_SLEEP_INTERVAL = 3000;

    private boolean showDiagnostic;

    public SquareSumUsingPhaser(boolean showDiagnostic) {
        this.showDiagnostic = showDiagnostic;
    }

    @Override
    public long getSquareSum(int[] values, int numberOfThreads) {
        long result = 0L;

        Phaser phaser = new Phaser();
        // To be on the safe side with the phase-value to check its "readiness" further after submitting all the tasks
        int phase = phaser.getPhase();

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        try {
            int startIndex = 0;
            int elementQuantity = values.length / numberOfThreads;
            for (int i = 0; i < numberOfThreads; i++) {
                int thisPortion = elementQuantity + ((i == 0) ? (values.length % numberOfThreads) : 0);
                // Intentionally do not use <Future> here just to demonstrate phaser-mechanism
                executor.submit(new CalcSquareSumPartCallable(values, startIndex, thisPortion, showDiagnostic, phaser));

                startIndex += thisPortion;
            }

        } finally {
            executor.shutdown();
        }

        // Wait for the end of the all of the tasks
        phaser.awaitAdvance(phase);

        // Sleep for some time to show the order of execution
        if (showDiagnostic) {
            System.out.println(String.format(CalcSquareSumPartCallable.SLEEPING_PATTERN, getClass().getName(),
                    Thread.currentThread().getName(), PRE_END_SLEEP_INTERVAL));
            try {
                Thread.sleep(PRE_END_SLEEP_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(getClass().getName() + ": " + Thread.currentThread().getName() + ": getSquareSum: end of calculation");
        }

        return result;
    }
}
