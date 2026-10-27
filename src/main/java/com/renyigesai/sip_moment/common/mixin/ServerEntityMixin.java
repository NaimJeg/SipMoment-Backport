package com.renyigesai.sip_moment.common.mixin;

import com.renyigesai.sip_moment.common.entitys.ITimeScaledEntity;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {

    @Shadow @Final private Entity entity;

    @Inject(method = "sendChanges", at = @At("HEAD"))
    private void onSendChanges(CallbackInfo ci) {
        if (this.entity instanceof ITimeScaledEntity ts && ts.getTimeScale() < 0.999f) {
            this.entity.hasImpulse = true;
        }
    }
}