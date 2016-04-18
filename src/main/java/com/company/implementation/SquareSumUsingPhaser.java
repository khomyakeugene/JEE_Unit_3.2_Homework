package com.company.implementation;

import com.company.interfaces.SquareSum;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * Created by Yevgen on 03.04.2016 as a part of the project "JEE_Unit_3.2_Homework".
 */
public class SquareSumUsingPhaser implements SquareSum {
    private boolean executionIllustrate;
    private int sleepingIntervalBound;

    public SquareSumUsingPhaser(boolean executionIllustrate, int sleepingIntervalBound) {
        this.executionIllustrate = executionIllustrate;
        this.sleepingIntervalBound = sleepingIntervalBound;
    }

    @Override
    public long getSquareSum(int[] values, int numberOfThreads) {
        //Почему мы передаем 1?
        Phaser phaser = new Phaser(1);

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        HashMap<String, Long> resultMap = new HashMap<>();
        try {
            int startIndex = 0;
            int elementQuantity = values.length / numberOfThreads;
            for (int i = 0; i < numberOfThreads; i++) {
                int thisPortion = elementQuantity + ((i == 0) ? (values.length % numberOfThreads) : 0);
                // Intentionally do not use <Future> here just to demonstrate phaser-mechanism
                executor.submit(new CalcSquareSumPart(values, startIndex, thisPortion, executionIllustrate,
                        sleepingIntervalBound, phaser, resultMap));

                startIndex += thisPortion;
            }

        } finally {
            executor.shutdown();
        }

        // Wait for all parties on this phaser ...
        phaser.arriveAndAwaitAdvance();

        // Sleep for some time to show the order of execution
        if (executionIllustrate) {
            int sleepingInterval = (sleepingIntervalBound <= 0) ? sleepingIntervalBound :
                    new Random().nextInt(sleepingIntervalBound);
            if (sleepingInterval > 0) {
                System.out.println(String.format(CalcSquareSumPart.SLEEPING_PATTERN, getClass().getName(),
                        Thread.currentThread().getName(), sleepingInterval));
                try {
                    Thread.sleep(sleepingInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return resultMap.values().stream().mapToLong(e -> e).sum();
    }
}
