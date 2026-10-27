package com.renyigesai.sip_moment.common.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class SMConsumables {
    public static final Consumable HIGHBALL;
    public static final Consumable DAVID_MARTINEZ;
    public static final Consumable BUTTER_BEER;
    public static final Consumable ORANGE_JUICE;
    public static final Consumable SHIPWRECK_VINTAGE;

    static {

        HIGHBALL = Consumables.defaultFood()
                .animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK)
                .hasConsumeParticles(false)
                .build();

        DAVID_MARTINEZ = defaultEffectFood(new MobEffectInstance(SMMobEffects.EDGERUNNERS,6000))
                .animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK)
                .hasConsumeParticles(false)
                .build();

        BUTTER_BEER = defaultEffectFood(new MobEffectInstance(MobEffects.REGENERATION,300,2))
                .animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK)
                .hasConsumeParticles(false)
                .build();

        ORANGE_JUICE = defaultEffectFood(new MobEffectInstance(SMMobEffects.SINCERELY_FOR_YOU,1200))
                .animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK)
                .hasConsumeParticles(false)
                .build();

        SHIPWRECK_VINTAGE = defaultEffectFood(
                new MobEffectInstance(MobEffects.NAUSEA,600)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.WITHER,600)))
                .animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK)
                .hasConsumeParticles(false)
                .build();
    }

    private static Consumable.Builder defaultEffectFood(MobEffectInstance effect) {
        return Consumables.defaultFood()
                .onConsume(new ApplyStatusEffectsConsumeEffect(effect));
    }

    public static Consumable.Builder defaultShake() {
        return Consumable.builder().consumeSeconds(1.6F).animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK).hasConsumeParticles(false);
    }
}