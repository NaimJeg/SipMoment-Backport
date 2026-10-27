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
        if (!(entity instanceof ITimeScaledEntity ts)){
            return;
        }
        if (entity instanceof IImmunityTimeScaled){
            return;
        }
        float scale = ts.getTimeScale();
        if (scale >= 0.999f){
            return;
        }
        if (entity instanceof LivingEntity living){
            living.invulnerableTime = 0;
        }
        if (ts.accumulateAndShouldSkip()) {
            entity.tickCount++;
            ci.cancel();
        }
    }
}