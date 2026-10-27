package com.renyigesai.sip_moment.common.client.renderer.entity;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.client.model.AfterimageModel;
import com.renyigesai.sip_moment.common.entitys.AfterimageEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class AfterimageRender extends HumanoidMobRenderer<AfterimageEntity, AfterimageRenderState, AfterimageModel<AfterimageRenderState>> {
    private static final Identifier LOCATION = Identifier.fromNamespaceAndPath(SipMomentMod.MODID,"textures/entity/afterimage.png");

    private static final int[] COLORS = {
            0x6BFF8C,
            0x00BFFF,
            0x1E3A8A,
            0x9400D3
    };

    public AfterimageRender(EntityRendererProvider.Context context) {
        super(context, new AfterimageModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), new AfterimageModel<>(context.bakeLayer(ModelLayers.ZOMBIE_BABY)), 0.0f);
    }

    @Override
    public Identifier getTextureLocation(AfterimageRenderState state) {
        return LOCATION;
    }

    @Override
    public void extractRenderState(AfterimageEntity entity, AfterimageRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

        if (entity.getWalkPos() == 0f && entity.getWalkSpeed() == 0f) {
            entity.getOwnerRef().ifPresent(ref -> {
                LivingEntity owner = ref.getEntity(entity.level(), LivingEntity.class);
                if (owner == null){
                    return;
                }
                entity.setPose(
                        owner.walkAnimation.position(partialTick),
                        owner.walkAnimation.speed(partialTick),
                        owner.yBodyRot,
                        owner.getYHeadRot() - owner.yBodyRot,
                        owner.getXRot()
                );
            });
        }
        state.walkAnimationPos = entity.getWalkPos();
        state.walkAnimationSpeed = entity.getWalkSpeed();
        state.bodyRot = entity.getBodyRot();
        state.isInvisible = false;

        int lifetime = Math.max(1, entity.getLifetime());
        float progress = Mth.clamp((entity.tickCount + partialTick) / lifetime, 0f, 1f);
        int rgb = lerpMultiColor(progress);
        float alphaT = Mth.clamp((progress - 0.3f) / 0.7f, 0f, 1f);
        int a = (int) (0xC0 * (1f - alphaT));
        state.tintColor = (a << 24) | rgb;
    }

    private static int lerpMultiColor(float t) {
        t = Mth.clamp(t, 0f, 1f);
        int segments = COLORS.length - 1;
        float scaled = t * segments;
        int idx = Math.min((int) scaled, segments - 1);
        float local = scaled - idx;

        int c1 = COLORS[idx];
        int c2 = COLORS[idx + 1];

        int r = lerpChannel((c1 >> 16) & 0xFF, (c2 >> 16) & 0xFF, local);
        int g = lerpChannel((c1 >> 8)  & 0xFF, (c2 >> 8)  & 0xFF, local);
        int b = lerpChannel(c1 & 0xFF,         c2 & 0xFF,         local);
        return (r << 16) | (g << 8) | b;
    }


    @Override
    public AfterimageRenderState createRenderState() {
        return new AfterimageRenderState();
    }

    @Override
    protected RenderType getRenderType(AfterimageRenderState state, boolean visible, boolean invisibleToPlayer, boolean glowing) {
        return RenderTypes.eyes(this.getTextureLocation(state));
    }

    @Override
    protected int getModelTint(AfterimageRenderState state) {
        return state.tintColor;
    }

    private static int lerpChannel(int from, int to, float t) {
        return Math.round(from + (to - from) * t);
    }
}
