package com.fertcfertc.snowwear.mixin;

import com.fertcfertc.snowwear.SnowWear;
import com.fertcfertc.snowwear.render.SnowLayer;
import com.fertcfertc.snowwear.render.SnowRenderState;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("RETURN"))
    private void snowwear$addLayer(CallbackInfo callback) {
        var parent = (RenderLayerParent<AvatarRenderState, PlayerModel>) (Object) this;
        ((LayerAccessor) this).snowwear$addLayer(new SnowLayer(parent));
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
            at = @At("RETURN"))
    private void snowwear$extract(net.minecraft.world.entity.Avatar avatar, AvatarRenderState state,
                                 float partialTick, CallbackInfo callback) {
        ((SnowRenderState) state).snowwear$coverage(SnowWear.TRACKER.coverage(state.id));
    }
}
