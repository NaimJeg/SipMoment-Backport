package com.renyigesai.sip_moment.common.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.renyigesai.sip_moment.common.entitys.IEntityRenderStateOffset;
import com.renyigesai.sip_moment.common.entitys.ITimeScaledEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    @Inject(method = "extractEntity", at = @At("RETURN"))
    private <E extends Entity> void onExtractEntity(E entity, float partialTicks, CallbackInfoReturnable<EntityRenderState> cir) {
        EntityRenderState state = cir.getReturnValue();
        if (state == null) return;

        IEntityRenderStateOffset accessor = (IEntityRenderStateOffset) state;

        if (!(entity instanceof ITimeScaledEntity ts) || ts.getTimeScale() >= 0.999f) {
            accessor.setVisualWorldPos(null);
            return;
        }

        accessor.setVisualWorldPos(ts.getVisualPos());
    }

    @Redirect(method = "submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lnet/minecraft/client/renderer/state/level/CameraRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V", ordinal = 0))
    private void onSubmit(PoseStack poseStack, double x, double y, double z, EntityRenderState state, @Local(argsOnly = true) CameraRenderState camera) {
        IEntityRenderStateOffset acc = (IEntityRenderStateOffset) state;
        Vec3 visual = acc.getVisualWorldPos();
        if (visual == null) {
            poseStack.translate(x, y, z);
            return;
        }

        Vec3 camPos = getCameraPos(camera);
        poseStack.translate(
                visual.x - camPos.x,
                visual.y - camPos.y,
                visual.z - camPos.z
        );
    }

    private static Vec3 getCameraPos(CameraRenderState camera) {
        return Minecraft.getInstance().gameRenderer.getMainCamera().position();
    }
}