package com.company.calculation;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Phaser;

/**
 * Created by Yevgen on 03.04.2016 as a part of the project "JEE_Unit_3.2_Homework".
 */
public class CalcSquareSumPart implements Callable<Long> {
    private static final String DIAGNOSTIC_PATTERN = "Class name: %s, Thread: %s, startIndex: %d, quantity of elements: %d, result: %d";
    private static final String AFTER_PARSING_BARRIER_PATTERN = "Class name: %s, Thread: %s: After parsing barrier";
    public static final String SLEEPING_PATTERN = "Class name: %s, Thread: %s is sleeping for %d ms  ...";

    private int[] values;
    private int startIndex;
    private int elementQuantity;
    private boolean executionIllustrate;
    private int sleepingIntervalBound;
    private Phaser phaser;
    private HashMap<String, Long> resultMap;

    public CalcSquareSumPart(int[] values, int startIndex, int elementQuantity, boolean showDiagnostic,
                             int sleepingIntervalBound, Phaser phaser, HashMap<String, Long> resultMap) {
        this.values = values;
        this.startIndex = startIndex;
        this.elementQuantity = elementQuantity;
        this.executionIllustrate = showDiagnostic;
        this.sleepingIntervalBound = sleepingIntervalBound;
        this.phaser = phaser;
        this.resultMap = resultMap;

        if (phaser != null) {
            // Adds this task as a new unarrived party to this phaser.
            phaser.register();
        }
    }

    public CalcSquareSumPart(int[] values, int startIndex, int elementQuantity,
                             boolean showDiagnostic, int sleepingIntervalBound) {
        this (values, startIndex, elementQuantity, showDiagnostic, sleepingIntervalBound, null, null);
    }

    private long getSquareSum() {
        long result = 0L;

        int upperLimit = startIndex + elementQuantity;
        upperLimit = upperLimit <= values.length ? upperLimit : values.length;

        for (int i = startIndex; i < upperLimit; i++) {
            result += Math.pow(values[i], 2);
        }

        if (executionIllustrate) {
            // To show the thread execute order ...
            int sleepingInterval = (sleepingIntervalBound <= 0) ? sleepingIntervalBound :
                    new Random().nextInt(sleepingIntervalBound);
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

        // Store result if it needs
        if (resultMap != null) {
            resultMap.put(Thread.currentThread().getName(), result);
        }

        if (phaser != null) {
            phaser.arriveAndDeregister();
            if (executionIllustrate)  {
                System.out.println(String.format(AFTER_PARSING_BARRIER_PATTERN, getClass().getName(),
                        Thread.currentThread().getName()));
            }
        }

        return result;
    }
}
