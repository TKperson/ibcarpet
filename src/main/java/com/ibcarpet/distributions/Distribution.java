package com.ibcarpet.distributions;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.Xoroshiro128PlusPlus;

public interface Distribution {

    // MurmurHash3
    private static long mix64(long x) {
        x ^= x >>> 33;
        x *= 0xff51afd7ed558ccdL;
        x ^= x >>> 33;
        x *= 0xc4ceb9fe1a85ec53L;
        x ^= x >>> 33;
        return x;
    }

    public static Distribution distribution = new Uniform();
    public static Xoroshiro128PlusPlus random = new Xoroshiro128PlusPlus(new RandomSupport.Seed128bit(
            System.currentTimeMillis(),
            mix64(System.currentTimeMillis())
    ));

    public static double nextDouble() { // range is [0, 1)
        return (random.nextLong() >>> 11) / (double)(1L << 53);
    }

    public static Distribution getCurrentDistribution() {
        return distribution;
    }

    public double sample();
}
