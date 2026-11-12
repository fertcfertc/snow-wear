package com.fertcfertc.snowwear.fabric;

import com.fertcfertc.snowwear.config.SnowWearConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public final class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SnowWearConfigScreen::create;
    }
}
