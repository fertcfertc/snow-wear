package com.fertcfertc.snowwear.config;

import com.fertcfertc.snowwear.SnowWear;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;

import java.net.URI;

public final class SnowWearConfigScreen {
    private static final SystemToast.SystemToastId SAVE_ERROR = new SystemToast.SystemToastId();

    private SnowWearConfigScreen() {}

    public static Screen create(Screen parent) {
        var current = SnowWear.CONFIG.get();
        var defaults = SnowWearSettings.defaults();
        var builder = ConfigBuilder.create().setParentScreen(parent).setTitle(text("title")).setDoesConfirmSave(true);
        var entries = builder.entryBuilder();
        var weather = builder.getOrCreateCategory(text("weather"));
        var visibility = builder.getOrCreateCategory(text("visibility"));
        var enabled = entries.startBooleanToggle(text("enabled"), current.enabled())
                .setDefaultValue(defaults.enabled()).setTooltip(text("enabled.tooltip")).build();
        var accumulation = entries.startIntSlider(text("accumulationSeconds"), current.accumulationSeconds(), 2, 120)
                .setDefaultValue(defaults.accumulationSeconds()).setTooltip(text("accumulationSeconds.tooltip")).build();
        var melt = entries.startIntSlider(text("meltSeconds"), current.meltSeconds(), 2, 120)
                .setDefaultValue(defaults.meltSeconds()).setTooltip(text("meltSeconds.tooltip")).build();
        var maximum = entries.startIntSlider(text("maxCoverage"), current.maxCoverage(), 10, 100)
                .setDefaultValue(defaults.maxCoverage()).setTextGetter(value -> Component.literal(value + "%"))
                .setTooltip(text("maxCoverage.tooltip")).build();
        weather.addEntry(enabled).addEntry(accumulation).addEntry(melt).addEntry(maximum);
        var armor = entries.startBooleanToggle(text("armorSnow"), current.armorSnow())
                .setDefaultValue(defaults.armorSnow()).setTooltip(text("armorSnow.tooltip")).build();
        var shedding = entries.startBooleanToggle(text("movementShedding"), current.movementShedding())
                .setDefaultValue(defaults.movementShedding()).setTooltip(text("movementShedding.tooltip")).build();
        weather.addEntry(armor).addEntry(shedding);
        var self = entries.startBooleanToggle(text("showSelf"), current.showSelf())
                .setDefaultValue(defaults.showSelf()).setTooltip(text("showSelf.tooltip")).build();
        var others = entries.startBooleanToggle(text("showOthers"), current.showOthers())
                .setDefaultValue(defaults.showOthers()).setTooltip(text("showOthers.tooltip")).build();
        var distance = entries.startIntSlider(text("renderDistance"), current.renderDistance(), 8, 96)
                .setDefaultValue(defaults.renderDistance()).setTooltip(text("renderDistance.tooltip")).build();
        visibility.addEntry(self).addEntry(others).addEntry(distance);
        weather.addEntry(entries.startTextDescription(text("sponsor").copy().withStyle(style -> style
                        .withColor(0xB9D9F0).withUnderlined(true)
                        .withClickEvent(new ClickEvent.OpenUrl(URI.create("https://ctf.mom")))))
                .setTooltip(text("sponsor.tooltip")).build());
        builder.setSavingRunnable(() -> {
            var next = new SnowWearSettings(enabled.getValue(), accumulation.getValue(), melt.getValue(),
                    maximum.getValue(), self.getValue(), others.getValue(), distance.getValue(),
                    armor.getValue(), shedding.getValue());
            if (!SnowWear.CONFIG.save(next)) {
                SystemToast.add(Minecraft.getInstance().gui.toastManager(), SAVE_ERROR,
                        text("saveError"), text("saveError.detail"));
            }
        });
        return builder.build();
    }

    private static Component text(String key) {
        return Component.translatable("snowwear.config." + key);
    }
}
