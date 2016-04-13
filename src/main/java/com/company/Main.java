package com.company;

import com.company.test.SquareSumTest;

public class Main {

    public static void main(String[] args) {
        final int SLEEPING_INTERVAL_BOUND = 5000;

        new SquareSumTest().demonstrate(false, SLEEPING_INTERVAL_BOUND);
    }
}
