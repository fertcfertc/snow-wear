package com.fertcfertc.snowwear.weather;

public final class SnowMotion {
    private SnowMotion() {}

    public static boolean sheds(float coverage, double travelSquared, boolean sprinting,
                                boolean landed, boolean jumping, long elapsedTicks) {
        if (coverage < .18f || elapsedTicks < 16 || !Double.isFinite(travelSquared)
                || travelSquared > 4) return false;
        return landed || jumping || sprinting && travelSquared > .025;
    }
}
