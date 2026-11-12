package com.fertcfertc.snowwear.weather;

public final class SnowCover {
    private SnowCover() {}

    public static float advance(float current, boolean snowing, boolean soaked, int accumulationSeconds,
                                int meltSeconds, float maximum) {
        if (soaked) return Math.max(0, current - 4f / (meltSeconds * 20));
        if (snowing) return Math.min(maximum, current + 1f / (accumulationSeconds * 20));
        return Math.max(0, Math.min(maximum, current) - 1f / (meltSeconds * 20));
    }
}
