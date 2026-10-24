package com.renyigesai.sip_moment.common.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.client.model.SpearLonginusModel;
import com.renyigesai.sip_moment.common.entitys.ThrownSpearLonginus;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SpearLonginusRender extends EntityRenderer<ThrownSpearLonginus> {
    public static final ResourceLocation TRIDENT_LOCATION = ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "textures/entity/spear_longinus.png");
    private final SpearLonginusModel model;

    public SpearLonginusRender(EntityRendererProvider.Context context) {
        super(context);
        model = new SpearLonginusModel(context.bakeLayer(SpearLonginusModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownSpearLonginus entity) {
        return TRIDENT_LOCATION;
    }

    @Override
    public void render(ThrownSpearLonginus entity, float yaw, float partialTick, PoseStack poses, MultiBufferSource buffers, int light) {
        poses.pushPose();
        poses.mulPose(Axis.YP.rotationDegrees(net.minecraft.util.Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90));
        poses.mulPose(Axis.ZP.rotationDegrees(net.minecraft.util.Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90));
        poses.scale(2, 2, 2);
        var vertices = ItemRenderer.getFoilBufferDirect(buffers, model.renderType(TRIDENT_LOCATION), false, entity.isFoil());
        model.renderToBuffer(poses, vertices, light, OverlayTexture.NO_OVERLAY, -1);
        poses.popPose();
        super.render(entity, yaw, partialTick, poses, buffers, light);
    }
}
