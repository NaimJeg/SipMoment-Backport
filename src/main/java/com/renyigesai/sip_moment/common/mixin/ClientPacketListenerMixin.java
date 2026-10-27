package com.renyigesai.sip_moment.common.mixin;

import com.renyigesai.sip_moment.common.entitys.ITimeScaledEntity;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    // Capture packets here: LivingEntity overrides Entity.lerpTo in 1.21.1.
    @Redirect(method = {"handleMoveEntity", "handleTeleportEntity"}, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;lerpTo(DDDFFI)V"))
    private void sip$interpolate(Entity entity, double x, double y, double z, float yaw, float pitch, int steps) {
        ((ITimeScaledEntity) entity).updateVisualPosition(new Vec3(x, y, z));
        entity.lerpTo(x, y, z, yaw, pitch, steps);
    }
}
