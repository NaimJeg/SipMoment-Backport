package com.renyigesai.sip_moment.common.mixin;

import com.renyigesai.sip_moment.common.entitys.IImmunityTimeScaled;
import com.renyigesai.sip_moment.common.entitys.ISlowResistant;
import com.renyigesai.sip_moment.common.entitys.ITimeScaledEntity;
import com.renyigesai.sip_moment.common.init.SMAttachments;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin extends AttachmentHolder implements ITimeScaledEntity {
    @Unique private float sip$accumulator;
    @Unique private int sip$lease;
    @Unique private long sip$lastPacketNanos;
    @Unique private long sip$packetIntervalNanos;
    @Unique private Vec3 sip$visualStart;
    @Unique private Vec3 sip$packetTarget;

    @Override
    public float getTimeScale() {
        return getData(SMAttachments.SLOW_RATE.get());
    }

    @Override
    public void setTimeScale(float scale) {
        scale = Float.isFinite(scale) ? Mth.clamp(scale, 0.0F, 1.0F) : 1.0F;
        if (Math.abs(getTimeScale() - scale) > 0.001F) setData(SMAttachments.SLOW_RATE.get(), scale);
        if (scale >= 0.999F) {
            sip$accumulator = 0.0F;
            sip$visualStart = null;
            sip$packetTarget = null;
            sip$packetIntervalNanos = 0;
        }
    }

    @Override public int getTimeStopLease() { return sip$lease; }
    @Override public void setTimeStopLease(int ticks) { sip$lease = Math.max(0, ticks); }

    @Override
    public boolean accumulateAndShouldSkip() {
        float scale = getTimeScale();
        if (scale >= 0.999F) {
            sip$accumulator = 0.0F;
            return false;
        }
        sip$accumulator += scale;
        if (sip$accumulator >= 1.0F) {
            sip$accumulator -= 1.0F;
            return false;
        }
        return true;
    }

    @Override
    public Vec3 getVisualPos() {
        if (sip$visualStart == null || sip$packetTarget == null) return ((Entity) (Object) this).position();
        float progress = Mth.clamp((Util.getNanos() - sip$lastPacketNanos) / (float) Math.max(1, sip$packetIntervalNanos), 0.0F, 1.0F);
        return sip$visualStart.lerp(sip$packetTarget, progress);
    }

    @Override
    public void updateVisualPosition(Vec3 target) {
        Entity self = (Entity) (Object) this;
        float scale = getTimeScale();
        if (!self.level().isClientSide() || self instanceof Player || scale >= 0.999F) {
            sip$visualStart = null;
            sip$packetTarget = null;
            sip$packetIntervalNanos = 0;
            return;
        }
        if (sip$packetTarget != null && sip$packetTarget.distanceToSqr(target) < 1.0E-6) return;
        long now = Util.getNanos();
        long expected = (long) (50_000_000L / Math.max(0.001F, scale));
        long actual = now - sip$lastPacketNanos;
        if (sip$packetIntervalNanos == 0) sip$packetIntervalNanos = expected;
        else if (actual > 0 && actual < expected * 3) sip$packetIntervalNanos = (sip$packetIntervalNanos * 3 + actual) / 4;
        sip$visualStart = getVisualPos();
        if (sip$visualStart.distanceToSqr(target) > 256.0) sip$visualStart = target;
        sip$packetTarget = target;
        sip$lastPacketNanos = now;
    }

    @Inject(method = "getPosition(F)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private void sip$visualPosition(float partialTick, CallbackInfoReturnable<Vec3> cir) {
        Entity self = (Entity) (Object) this;
        if (self.level().isClientSide() && !(self instanceof Player) && getTimeScale() < 0.999F && sip$packetTarget != null) {
            cir.setReturnValue(getVisualPos());
        }
    }

    @Override
    public void setSlow(float scale, int ticks) {
        if ((Object) this instanceof IImmunityTimeScaled) return;
        float resistance = (Object) this instanceof ISlowResistant resistant ? resistant.getSlowResistance() : 0.0F;
        setTimeStopLease(ticks);
        setTimeScale(scale + (1.0F - scale) * resistance);
    }
}
