package com.renyigesai.sip_moment.common.entitys;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class AfterimageEntity extends Mob implements IImmunityTimeScaled{
    private int lifetime = 30;
    private boolean poseCaptured;
    private static final EntityDataAccessor<Float> DATA_WALK_POS =
            SynchedEntityData.defineId(AfterimageEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_WALK_SPEED =
            SynchedEntityData.defineId(AfterimageEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_BODY_ROT =
            SynchedEntityData.defineId(AfterimageEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_HEAD_YAW =
            SynchedEntityData.defineId(AfterimageEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_HEAD_PITCH =
            SynchedEntityData.defineId(AfterimageEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> DATA_OWNER =
            SynchedEntityData.defineId(AfterimageEntity.class, EntityDataSerializers.INT);

    public AfterimageEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
        this.setNoAi(true);
        this.noPhysics = true;
        this.setNoGravity(true);
        this.setInvulnerable(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && this.tickCount >= this.lifetime){
            this.discard();
        }
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_WALK_POS, 0f);
        builder.define(DATA_WALK_SPEED, 0f);
        builder.define(DATA_BODY_ROT, 0f);
        builder.define(DATA_HEAD_YAW, 0f);
        builder.define(DATA_HEAD_PITCH, 0f);
        builder.define(DATA_OWNER, -1);
    }

    public float getWalkPos() { return entityData.get(DATA_WALK_POS); }
    public float getWalkSpeed() { return entityData.get(DATA_WALK_SPEED); }
    public float getBodyRot() { return entityData.get(DATA_BODY_ROT); }

    public void setPose(float walkPos, float walkSpeed, float bodyRot, float headYaw, float headPitch) {
        entityData.set(DATA_WALK_POS, walkPos);
        entityData.set(DATA_WALK_SPEED, walkSpeed);
        entityData.set(DATA_BODY_ROT, bodyRot);
        entityData.set(DATA_HEAD_YAW, headYaw);
        entityData.set(DATA_HEAD_PITCH, headPitch);
    }

    @Nullable
    public LivingEntity getOwner() {
        return level().getEntity(entityData.get(DATA_OWNER)) instanceof LivingEntity owner ? owner : null;
    }

    public float getHeadYaw() { return entityData.get(DATA_HEAD_YAW); }
    public float getHeadPitch() { return entityData.get(DATA_HEAD_PITCH); }

    public void captureOwnerPose(float partialTick) {
        if (poseCaptured) return;
        LivingEntity owner = getOwner();
        if (owner != null) {
            setPose(owner.walkAnimation.position(partialTick), owner.walkAnimation.speed(partialTick),
                    owner.yBodyRot, owner.getYHeadRot() - owner.yBodyRot, owner.getXRot());
            poseCaptured = true;
        }
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.entityData.set(DATA_OWNER, owner == null ? -1 : owner.getId());
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        return false;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }
}
