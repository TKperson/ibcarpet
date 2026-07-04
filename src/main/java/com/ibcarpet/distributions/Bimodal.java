package com.ibcarpet.distributions;

import com.ibcarpet.IBCarpetSettings;

public class Bimodal implements Distribution {
    @Override
    public double sample() {
        double alpha = IBCarpetSettings.ibBimodalExtremeness;
        double sign = Long.signum(Distribution.random.nextLong());
        return sign * Math.pow(Distribution.nextDouble(), 1.0 - alpha);
    }
}
