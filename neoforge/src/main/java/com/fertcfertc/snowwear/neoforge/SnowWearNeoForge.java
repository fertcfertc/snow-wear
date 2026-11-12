package com.fertcfertc.snowwear.neoforge;

import com.fertcfertc.snowwear.SnowWear;
import com.fertcfertc.snowwear.config.SnowWearConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = SnowWear.ID, dist = Dist.CLIENT)
public final class SnowWearNeoForge {
    public SnowWearNeoForge(ModContainer container) {
        SnowWear.initialize();
        container.registerExtensionPoint(IConfigScreenFactory.class,
                (mod, parent) -> SnowWearConfigScreen.create(parent));
    }
}
