package com.fertcfertc.snowwear.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;

final class SnowCrust {
    private SnowCrust() {}

    static void patch(PoseStack.Pose pose, VertexConsumer vertices, int light,
                      float x, float y, float z, float width, float depth, float height) {
        float x0=(x-width/2)/16,x1=(x+width/2)/16,z0=(z-depth/2)/16,z1=(z+depth/2)/16;
        float top=(y-height)/16,bottom=y/16;
        face(pose,vertices,light,new float[][]{{x0,top,z0},{x1,top,z0},{x1,top,z1},{x0,top,z1}},0,-1,0,255);
        face(pose,vertices,light,new float[][]{{x0,bottom,z0},{x0,top,z0},{x0,top,z1},{x0,bottom,z1}},-1,0,0,210);
        face(pose,vertices,light,new float[][]{{x1,bottom,z1},{x1,top,z1},{x1,top,z0},{x1,bottom,z0}},1,0,0,210);
        face(pose,vertices,light,new float[][]{{x1,bottom,z0},{x1,top,z0},{x0,top,z0},{x0,bottom,z0}},0,0,-1,230);
        face(pose,vertices,light,new float[][]{{x0,bottom,z1},{x0,top,z1},{x1,top,z1},{x1,bottom,z1}},0,0,1,230);
    }
    private static void face(PoseStack.Pose pose,VertexConsumer vertices,int light,float[][] points,float nx,float ny,float nz,int shade){
        for(int index=0;index<4;index++)vertices.addVertex(pose,points[index][0],points[index][1],points[index][2])
                .setColor(shade,shade,255,255).setUv(index<2?0:1,index==0||index==3?0:1)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,nx,ny,nz);
    }
}
