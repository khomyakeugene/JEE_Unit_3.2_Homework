package com.company.implementation;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Phaser;

/**
 * Created by Yevgen on 03.04.2016 as a part of the project "JEE_Unit_3.2_Homework".
 */
public class CalcSquareSumPartCallable implements Callable<Long> {
    private static final int SLEEPING_INTERVAL_BOUND = 5000;
    private static final String DIAGNOSTIC_PATTERN = "Class name: %s, Thread: %s, startIndex: %d, quantity of elements: %d, result: %d";
    private static final String AFTER_PARSING_BARRIER_PATTERN = "Class name: %s, Thread: %s: After parsing barrier";
    public static final String SLEEPING_PATTERN = "Class name: %s, Thread: %s is sleeping for %d ms  ...";

    private int[] values;
    private int startIndex;
    private int elementQuantity;
    private boolean showDiagnostic;
    private Phaser phaser;
    private HashMap<String, Long> resultMap;

    public CalcSquareSumPartCallable(int[] values, int startIndex, int elementQuantity, boolean showDiagnostic,
                                     Phaser phaser, HashMap<String, Long> resultMap) {
        this.values = values;
        this.startIndex = startIndex;
        this.elementQuantity = elementQuantity;
        this.showDiagnostic = showDiagnostic;
        this.phaser = phaser;
        this.resultMap = resultMap;

        if (phaser != null) {
            // Adds this task as a new unarrived party to this phaser.
            phaser.register();
        }
    }

    public CalcSquareSumPartCallable(int[] values, int startIndex, int elementQuantity, boolean showDiagnostic) {
        this (values, startIndex, elementQuantity, showDiagnostic, null, null);
    }

    private long getSquareSum() {
        long result = 0L;

        int upperLimit = startIndex + elementQuantity;
        upperLimit = upperLimit <= values.length ? upperLimit : values.length;
        for (int i = startIndex; i < upperLimit; i++) {
            result += Math.pow(values[i], 2);
        }

        if (showDiagnostic) {
            // To show the thread execute order ...
            int sleepingInterval = new Random().nextInt(SLEEPING_INTERVAL_BOUND);
            if (sleepingInterval > 0) {
                System.out.println(String.format(SLEEPING_PATTERN, getClass().getName(), Thread.currentThread().getName(), sleepingInterval));
                try {
                    Thread.sleep(sleepingInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(String.format(DIAGNOSTIC_PATTERN, getClass().getName(), Thread.currentThread().getName(),
                    startIndex, elementQuantity, result));
        }

        return result;
    }

    @Override
    public Long call() throws Exception {
        Long result = getSquareSum();

        if (phaser != null) {
            phaser.arriveAndAwaitAdvance();
            if (showDiagnostic)  {
                System.out.println(String.format(AFTER_PARSING_BARRIER_PATTERN, getClass().getName(),
                        Thread.currentThread().getName()));
            }
        }

        // Store result if it needs
        if (resultMap != null) {
            resultMap.put(Thread.currentThread().getName(), result);
        }

        return result;
    }
}
