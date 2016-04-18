package com.company;

import com.company.test.SquareSumTest;

public class Main {
//замечания по построению иерархии классов, пакетов те же, что и в модуле 3.1
    public static void main(String[] args) {
        final int SLEEPING_INTERVAL_BOUND = 5000;

        new SquareSumTest().demonstrate(false, SLEEPING_INTERVAL_BOUND);
    }
}
