package com.ibcarpet.distributions;

import net.minecraft.util.RandomSource;

public class Uniform implements Distribution {

    Uniform() {}

    @Override
    public double sample() { // [-1, 1)
        return Distribution.nextDouble() * 2.0 - 1.0;
    }
}