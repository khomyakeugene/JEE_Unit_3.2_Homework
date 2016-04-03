package com.company.implementation;

import java.util.concurrent.Callable;

/**
 * Created by Yevgen on 03.04.2016 as a part of the project "JEE_Unit_3.2_Homework".
 */
public class CalcSquareSumPartCallable extends CalcSquareSumPart implements Callable<Long> {

    public CalcSquareSumPartCallable(int[] values, int startIndex, int elementQuantity, boolean showDiagnostic) {
        super(values, startIndex, elementQuantity, showDiagnostic);
    }

    @Override
    public Long call() throws Exception {
        return getSquareSum();
    }
}
