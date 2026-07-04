package com.ibcarpet.distributions;

import com.ibcarpet.IBCarpetSettings;

public class Discrete implements Distribution {
    @Override
    public double sample() {
        long possibleValues = IBCarpetSettings.ibDiscreteCuts + 1;
        long value = Math.abs(Distribution.random.nextLong())  % (possibleValues + 1);
        return (value / (double) possibleValues) * 2.0 - 1.0;
    }
}
