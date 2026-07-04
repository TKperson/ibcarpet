package com.ibcarpet.distributions;

import com.ibcarpet.IBCarpetSettings;

public class Trimodal implements Distribution {
    @Override
    public double sample() {
        double alpha = IBCarpetSettings.ibTrimodalExtremeness;
        double u = Distribution.nextDouble() * 2.0 - 1.0;
        double u2 = u * u;
        double t = u * u2 * (1.875 * (u2 * u2) - 5.25 * u2 + 4.375);
        double val = (1.0 - alpha) * u + alpha * t;

        // safety clamp bc of cases like 1.0000000002 which is not in [-1, 1)
        return val >= 1.0 ? -1.0 : val;
    }
}
