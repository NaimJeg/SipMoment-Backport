package com.renyigesai.sip_moment.common.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.renyigesai.sip_moment.common.entitys.ITimeScaledEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V", ordinal = 0))
    private void sip$renderPosition(PoseStack pose, double x, double y, double z, Entity entity,
                                   double renderX, double renderY, double renderZ, float yaw, float partialTick,
                                   PoseStack originalPose, MultiBufferSource buffers, int light) {
        if (!(entity instanceof Player) && entity instanceof ITimeScaledEntity scaled && scaled.getTimeScale() < 0.999F) {
            Vec3 visual = scaled.getVisualPos();
            x += visual.x - Mth.lerp(partialTick, entity.xOld, entity.getX());
            y += visual.y - Mth.lerp(partialTick, entity.yOld, entity.getY());
            z += visual.z - Mth.lerp(partialTick, entity.zOld, entity.getZ());
        }
        pose.translate(x, y, z);
    }
}
