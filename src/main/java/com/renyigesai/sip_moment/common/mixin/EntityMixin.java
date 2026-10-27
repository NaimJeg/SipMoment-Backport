package com.renyigesai.sip_moment.common.mixin;

import com.renyigesai.sip_moment.common.entitys.ISlowResistant;
import com.renyigesai.sip_moment.common.entitys.ITimeScaledEntity;
import com.renyigesai.sip_moment.common.init.SMAttachments;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Entity.class)
public abstract class EntityMixin extends AttachmentHolder implements ITimeScaledEntity {

    @Shadow public abstract Level level();;
    @Unique private float sip$accumulator = 0f;
    @Unique private int lease = 0;
    @Unique private long lastRealTickNanos = Util.getNanos();
    @Unique private Vec3 visualStartPos = null;
    @Unique private long lastPacketIntervalNanos = 0L;
    @Unique private Vec3 packetTarget = null;

    @Override
    public float getTimeScale() {
        return this.getData(SMAttachments.SLOW_RATE.get());
    }

    @Override
    public void setTimeScale(float scale) {
        scale = Math.max(0f, Math.min(1f, scale));
        if (Math.abs(this.getTimeScale() - scale) > 0.01f) {
            this.setData(SMAttachments.SLOW_RATE.get(), scale);
        }
    }

    @Override public int getTimeStopLease() { return this.lease; }
    @Override public void setTimeStopLease(int t) { this.lease = t; }

    @Override
    public Vec3 getVisualPos() {
        if (this.visualStartPos == null || this.packetTarget == null) {
            Entity self = (Entity) (Object) this;
            return new Vec3(self.getX(), self.getY(), self.getZ());
        }
        long interval = this.lastPacketIntervalNanos;
        if (interval <= 0L) interval = (long) (50_000_000L / this.getTimeScale());
        long elapsed = Util.getNanos() - this.lastRealTickNanos;
        float local = Mth.clamp(elapsed / (float) interval, 0f, 1f);

        return new Vec3(
                Mth.lerp(local, this.visualStartPos.x, this.packetTarget.x),
                Mth.lerp(local, this.visualStartPos.y, this.packetTarget.y),
                Mth.lerp(local, this.visualStartPos.z, this.packetTarget.z)
        );
    }

    @Override
    public boolean accumulateAndShouldSkip() {
        float scale = this.getTimeScale();
        if (scale >= 0.999f) {
            this.sip$accumulator = 0f;
            return false;
        }
        if (scale <= 0.001f) {
            return true;
        }
        this.sip$accumulator += scale;
        if (this.sip$accumulator >= 1.0f) {
            this.sip$accumulator -= 1.0f;
            return false;
        }
        return true;
    }

    @Inject(method = "moveOrInterpolateTo(Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;)V", at = @At("HEAD"))
    private void onMoveOrInterpolateTo(Optional<Vec3> position, Optional<Float> yRot, Optional<Float> xRot, CallbackInfo ci) {
        if (!this.level().isClientSide()) return;
        if ((Object) this instanceof Player) return;

        float scale = this.getTimeScale();
        if (scale >= 0.999f) return;
        if (position.isEmpty()) return;

        Entity self = (Entity) (Object) this;
        Vec3 target = position.get();
        if (this.packetTarget != null
                && this.packetTarget.distanceToSqr(target) < 1.0e-6) {
            return;
        }

        long now = Util.getNanos();
        long expected = (long) (50_000_000L / scale);
        long actual = now - this.lastRealTickNanos;

        if (this.lastPacketIntervalNanos == 0L) {
            this.lastPacketIntervalNanos = expected;
        } else if (actual > 0 && actual < expected * 3) {
            this.lastPacketIntervalNanos =
                    (this.lastPacketIntervalNanos * 3 + actual) / 4;
        }

        long interval = this.lastPacketIntervalNanos;
        float local = Mth.clamp(actual / (float) interval, 0f, 1f);

        if (this.visualStartPos == null) {
            this.visualStartPos = new Vec3(self.getX(), self.getY(), self.getZ());
        } else if (this.packetTarget != null) {
            this.visualStartPos = new Vec3(
                    Mth.lerp(local, this.visualStartPos.x, this.packetTarget.x),
                    Mth.lerp(local, this.visualStartPos.y, this.packetTarget.y),
                    Mth.lerp(local, this.visualStartPos.z, this.packetTarget.z)
            );
        }

        if (this.visualStartPos.distanceToSqr(target) > 16.0 * 16.0) {
            this.visualStartPos = target;
        }

        this.packetTarget = target;
        this.lastRealTickNanos = now;
    }

    @Inject(method = "getPosition(F)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private void onGetPosition(float partialTick, CallbackInfoReturnable<Vec3> cir) {
        if (!this.level().isClientSide()) return;
        if ((Object) this instanceof Player) return;

        float scale = this.getTimeScale();
        if (scale >= 0.999f) return;
        if (this.visualStartPos == null || this.packetTarget == null) return;

        long interval = this.lastPacketIntervalNanos;
        if (interval <= 0L) interval = (long) (50_000_000L / scale);

        long elapsed = Util.getNanos() - this.lastRealTickNanos;
        float local = Mth.clamp(elapsed / (float) interval, 0f, 1f);

        cir.setReturnValue(new Vec3(
                Mth.lerp(local, this.visualStartPos.x, this.packetTarget.x),
                Mth.lerp(local, this.visualStartPos.y, this.packetTarget.y),
                Mth.lerp(local, this.visualStartPos.z, this.packetTarget.z)
        ));
    }

    @Override
    public void setSlow(float scale, int ticks) {
        setTimeStopLease(ticks);
        float resistance = 0;
        if ((Object)this instanceof ISlowResistant iSlowResistant) {
            resistance = iSlowResistant.getSlowResistance();
        }
        float newSpeed = scale + (1.0f - scale) * resistance;
        setTimeScale(newSpeed);
    }
}