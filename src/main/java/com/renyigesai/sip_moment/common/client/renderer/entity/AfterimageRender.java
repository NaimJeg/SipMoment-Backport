package com.renyigesai.sip_moment.common.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.client.model.AfterimageModel;
import com.renyigesai.sip_moment.common.entitys.AfterimageEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class AfterimageRender extends EntityRenderer<AfterimageEntity> {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "textures/entity/afterimage.png");
    private static final int[] COLORS = {0x6BFF8C, 0x00BFFF, 0x1E3A8A, 0x9400D3};
    private final AfterimageModel model;

    public AfterimageRender(EntityRendererProvider.Context context) {
        super(context);
        model = new AfterimageModel(context.bakeLayer(ModelLayers.ZOMBIE));
    }

    @Override
    public ResourceLocation getTextureLocation(AfterimageEntity entity) {
        return LOCATION;
    }

    @Override
    public void render(AfterimageEntity entity, float yaw, float partialTick, PoseStack pose, MultiBufferSource buffers, int light) {
        entity.captureOwnerPose(partialTick);
        float progress = Mth.clamp((entity.tickCount + partialTick) / Math.max(1, entity.getLifetime()), 0.0F, 1.0F);
        int alpha = (int) (0xC0 * (1.0F - Mth.clamp((progress - 0.3F) / 0.7F, 0.0F, 1.0F)));
        int tint = (alpha << 24) | lerpMultiColor(progress);
        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(180.0F - entity.getBodyRot()));
        pose.scale(-1.0F, -1.0F, 1.0F);
        pose.translate(0.0F, -1.501F, 0.0F);
        model.setupAnim(entity, entity.getWalkPos(), entity.getWalkSpeed(), entity.tickCount + partialTick, entity.getHeadYaw(), entity.getHeadPitch());
        model.renderToBuffer(pose, buffers.getBuffer(RenderType.entityTranslucent(LOCATION)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, tint);
        pose.popPose();
    }

    private static int lerpMultiColor(float progress) {
        float scaled = Mth.clamp(progress, 0.0F, 1.0F) * (COLORS.length - 1);
        int index = Math.min((int) scaled, COLORS.length - 2);
        float local = scaled - index;
        int from = COLORS[index];
        int to = COLORS[index + 1];
        return (lerpChannel(from >> 16, to >> 16, local) << 16)
                | (lerpChannel(from >> 8, to >> 8, local) << 8)
                | lerpChannel(from, to, local);
    }

    private static int lerpChannel(int from, int to, float progress) {
        return Math.round((from & 255) + ((to & 255) - (from & 255)) * progress);
    }
}
