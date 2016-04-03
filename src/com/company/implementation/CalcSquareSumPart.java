package com.company.implementation;

import java.util.Random;

/**
 * Created by Yevgen on 03.04.2016 as a part of the project "JEE_Unit_3.2_Homework".
 */
public class CalcSquareSumPart {
    private static final int SLEEPING_INTERVAL_BOUND = 5000;
    private static final String DIAGNOSTIC_PATTERN = "Class name: %s, Thread: %s, startIndex: %d, quantity of elements: %d, result: %d";
    private static final String SLEEPING_PATTERN = "Class name: %s, Thread: %s has done its work and is sleeping for %d ms  ...";

    private int[] values;
    private int startIndex;
    private int elementQuantity;
    private boolean showDiagnostic;

    public CalcSquareSumPart(int[] values, int startIndex, int elementQuantity, boolean showDiagnostic) {
        this.values = values;
        this.startIndex = startIndex;
        this.elementQuantity = elementQuantity;
        this.showDiagnostic = showDiagnostic;
    }

    public CalcSquareSumPart() {
        this (new int[] {}, 0, 0, false);
    }

    public Long getSquareSum() {
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
}
