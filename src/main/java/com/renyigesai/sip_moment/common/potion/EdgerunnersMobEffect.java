package com.renyigesai.sip_moment.common.potion;

import com.renyigesai.sip_moment.common.entitys.AfterimageEntity;
import com.renyigesai.sip_moment.common.entitys.ITimeScaledEntity;
import com.renyigesai.sip_moment.common.init.SMEntityTypes;
import com.renyigesai.sip_moment.common.init.SMMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

import java.util.List;

public class EdgerunnersMobEffect extends MobEffect {
    public EdgerunnersMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }


    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
        return amplification >= 0;
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplifier) {
        mob.addEffect(new MobEffectInstance(MobEffects.SPEED,10,2,false,false));
        mob.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST,10,0,false,false));
        final double range = 32.0;
        final double rangeSqr = range * range;
        AABB area = mob.getBoundingBox().inflate(range);

        List<Entity> targets = serverLevel.getEntitiesOfClass(
                Entity.class,
                area,
                e -> e != mob
                        && e.isAlive()
                        && !e.isSpectator()
        );
        for (Entity target : targets) {
            if (target.distanceToSqr(mob) > rangeSqr) {
                continue;
            }
            int timeStopLease = ((ITimeScaledEntity) target).getTimeStopLease();
            ((ITimeScaledEntity)target).setSlow(0.1f,Math.min(2, timeStopLease + 2));
        }

        Vec3 knownSpeed = mob.getKnownSpeed();
        double horizontalSq = knownSpeed.x * knownSpeed.x + knownSpeed.z * knownSpeed.z;
        if (horizontalSq > 1.0e-4) {
            spawnAfterimage(serverLevel, mob);
        }
        return true;
    }

    private static void spawnAfterimage(ServerLevel level, LivingEntity entity) {
        float yawRad = (float) Math.toRadians(entity.getYRot());
        double backX = Math.sin(yawRad) * 0.65;
        double backZ = -Math.cos(yawRad) * 0.65;
        AfterimageEntity ghost = new AfterimageEntity(SMEntityTypes.AFTERIMAGE.get(), level);
        ghost.setOwner(entity);
        ghost.setPos(entity.getX() + backX, entity.getY(), entity.getZ() + backZ);
        ghost.setYRot(entity.getYRot());
        ghost.setXRot(entity.getXRot());
        ghost.setYHeadRot(entity.getYHeadRot());
        ghost.setYBodyRot(entity.yBodyRot);
        ghost.setOldPosAndRot();
        level.addFreshEntity(ghost);
    }

    @EventBusSubscriber
    public static class EdgerunnersPotion{
        @SubscribeEvent
        public static void onEdgerunners(CriticalHitEvent event){
            Entity attacker = event.getEntity();
            Entity entity = event.getTarget();
            if (attacker instanceof LivingEntity livingEntity && livingEntity.hasEffect(SMMobEffects.EDGERUNNERS)){
                RandomSource random = entity.getRandom();
                if (random.nextDouble() < 0.1){
                    if (livingEntity.hasEffect(SMMobEffects.FURIOUS)){
                        int level = livingEntity.getEffect(SMMobEffects.FURIOUS).getAmplifier();
                        if (level + 1 <= 2){
                            level ++;
                        }
                        livingEntity.removeEffect(SMMobEffects.FURIOUS);
                        livingEntity.addEffect(new MobEffectInstance(SMMobEffects.FURIOUS,240,level));
                    }else {
                        livingEntity.addEffect(new MobEffectInstance(SMMobEffects.FURIOUS,240));
                    }
                }
            }
        }

    }
}
