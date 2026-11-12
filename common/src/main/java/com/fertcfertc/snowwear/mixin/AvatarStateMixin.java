package com.fertcfertc.snowwear.mixin;

import com.fertcfertc.snowwear.render.SnowRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public class AvatarStateMixin implements SnowRenderState {
    @Unique private float snowwear$coverage;

    @Override
    public float snowwear$coverage() { return snowwear$coverage; }

    @Override
    public void snowwear$coverage(float value) { snowwear$coverage = value; }
}
