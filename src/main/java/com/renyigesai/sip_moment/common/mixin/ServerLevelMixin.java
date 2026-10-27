package com.renyigesai.sip_moment.common.mixin;

import com.renyigesai.sip_moment.common.entitys.IImmunityTimeScaled;
import com.renyigesai.sip_moment.common.entitys.ITimeScaledEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Inject(method = "tickNonPassenger", at = @At("HEAD"), cancellable = true)
    private void sip$slowEntityTick(Entity entity, CallbackInfo ci) {
        if (sip$shouldSkip(entity)) ci.cancel();
    }

    @Inject(method = "tickPassenger", at = @At("HEAD"), cancellable = true)
    private void sip$slowPassengerTick(Entity vehicle, Entity passenger, CallbackInfo ci) {
        if (sip$shouldSkip(passenger)) ci.cancel();
    }

    private static boolean sip$shouldSkip(Entity entity) {
        if (entity instanceof IImmunityTimeScaled || !(entity instanceof ITimeScaledEntity scaled)) return false;
        if (scaled.getTimeScale() >= 0.999F) return false;
        if (entity instanceof LivingEntity living) living.invulnerableTime = 0;
        if (!scaled.accumulateAndShouldSkip()) return false;
        entity.setOldPosAndRot();
        entity.tickCount++;
        return true;
    }
}
