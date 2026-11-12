package com.fertcfertc.snowwear;

import com.fertcfertc.snowwear.config.SnowWearConfig;
import com.fertcfertc.snowwear.weather.SnowTracker;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.platform.Platform;

public final class SnowWear {
    public static final String ID = "snowwear";
    public static final SnowWearConfig CONFIG = new SnowWearConfig(Platform.getConfigFolder().resolve(ID + ".json"));
    public static final SnowTracker TRACKER = new SnowTracker();

    private SnowWear() {}

    public static void initialize() {
        CONFIG.load();
        ClientTickEvent.CLIENT_POST.register(TRACKER::tick);
    }
}
