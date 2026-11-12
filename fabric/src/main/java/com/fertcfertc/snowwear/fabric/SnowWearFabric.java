package com.fertcfertc.snowwear.fabric;

import com.fertcfertc.snowwear.SnowWear;
import net.fabricmc.api.ClientModInitializer;

public final class SnowWearFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SnowWear.initialize();
    }
}
