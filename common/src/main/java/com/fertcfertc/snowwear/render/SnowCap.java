package com.fertcfertc.snowwear.render;

public record SnowCap(float y, float width, float depth, float thickness) {
    public static SnowCap head(boolean armored) {
        return armored ? new SnowCap(-9.55f, 11.3f, 11.3f, .85f)
                : new SnowCap(-8.55f, 6.8f, 6.8f, .65f);
    }

    public static SnowCap shoulder(boolean armored) {
        return armored ? new SnowCap(-3.05f, 6.3f, 6.3f, .65f)
                : new SnowCap(-2.3f, 3.3f, 3.8f, .45f);
    }
}
