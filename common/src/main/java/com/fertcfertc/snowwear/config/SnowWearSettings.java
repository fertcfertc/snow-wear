package com.fertcfertc.snowwear.config;

public record SnowWearSettings(boolean enabled, int accumulationSeconds, int meltSeconds,
                               int maxCoverage, boolean showSelf, boolean showOthers, int renderDistance,
                               boolean armorSnow, boolean movementShedding) {
    public static SnowWearSettings defaults() {
        return new SnowWearSettings(true, 30, 20, 100, true, true, 48, true, true);
    }

    public SnowWearSettings normalized() {
        return new SnowWearSettings(enabled, clamp(accumulationSeconds, 2, 120), clamp(meltSeconds, 2, 120),
                clamp(maxCoverage, 10, 100), showSelf, showOthers, clamp(renderDistance, 8, 96), armorSnow, movementShedding);
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
