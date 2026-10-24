package com.renyigesai.sip_moment.common.potion;

import com.renyigesai.sip_moment.common.init.SMMobEffects;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

public class EdgerunnersMobEffect extends MobEffect {
    public EdgerunnersMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @EventBusSubscriber
    public static class EdgerunnersPotion{

        @SubscribeEvent
        public static void onEdgerunners(CriticalHitEvent event){
            Entity attacker = event.getEntity();
            Entity entity = event.getTarget();
            if (attacker instanceof LivingEntity livingEntity && livingEntity.hasEffect(SMMobEffects.EDGERUNNERS)){
                RandomSource random = livingEntity.getRandom();
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
