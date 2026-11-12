package com.fertcfertc.snowwear.render;

import com.fertcfertc.snowwear.SnowWear;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public final class SnowLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private static final Identifier SNOW = Identifier.parse("minecraft:textures/block/snow.png");
    private static final Identifier[] TEXTURES = new Identifier[8];
    static {
        for (int index = 0; index < TEXTURES.length; index++) {
            TEXTURES[index] = Identifier.fromNamespaceAndPath(SnowWear.ID, "textures/entity/snow_" + (index + 1) + ".png");
        }
    }

    public SnowLayer(RenderLayerParent<AvatarRenderState, PlayerModel> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector collector, int light, AvatarRenderState state,
                       float yRot, float xRot) {
        float coverage = ((SnowRenderState) state).snowwear$coverage();
        if (coverage <= 0.01f || state.isInvisible || state.isSpectator || !SnowWear.CONFIG.get().enabled()) return;
        int stage = Math.min(TEXTURES.length - 1, (int) (coverage * TEXTURES.length));
        collector.order(1).submitModel(getParentModel(), state, pose, RenderTypes.entityCutoutZOffset(TEXTURES[stage]),
                light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF, null, state.outlineColor);
        if (coverage < .12f) return;
        var model=getParentModel();
        model.setupAnim(state);
        boolean helmet = !state.headEquipment.isEmpty();
        boolean chest = !state.chestEquipment.isEmpty();
        if (!helmet || SnowWear.CONFIG.get().armorSnow()) {
            SnowCap cap = SnowCap.head(helmet);
            crust(pose,collector,model.head,light,coverage,0,cap.y(),cap.width(),cap.depth(),cap.thickness());
        }
        if (!chest || SnowWear.CONFIG.get().armorSnow()) {
            SnowCap cap = SnowCap.shoulder(chest);
            crust(pose,collector,model.rightArm,light,coverage,-1,cap.y(),cap.width(),cap.depth(),cap.thickness());
            crust(pose,collector,model.leftArm,light,coverage,1,cap.y(),cap.width(),cap.depth(),cap.thickness());
        }
    }

    private static void crust(PoseStack poses,SubmitNodeCollector collector,ModelPart part,int light,float coverage,
                              float x,float y,float width,float depth,float thickness){
        poses.pushPose();
        part.translateAndRotate(poses);
        float spread=.35f+.65f*coverage,height=thickness*coverage;
        collector.order(2).submitCustomGeometry(poses,RenderTypes.entityCutout(SNOW),(pose,vertices)->{
            SnowCrust.patch(pose,vertices,light,x,y,0,width*spread,depth*spread,height);
            SnowCrust.patch(pose,vertices,light,x-width*.3f,y,-depth*.18f,width*.35f*spread,depth*.65f*spread,height*.65f);
            SnowCrust.patch(pose,vertices,light,x+width*.28f,y,depth*.23f,width*.4f*spread,depth*.45f*spread,height*.5f);
        });
        poses.popPose();
    }
}
