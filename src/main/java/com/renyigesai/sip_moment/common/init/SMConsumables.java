package com.renyigesai.sip_moment.common.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class SMConsumables {
    public static final FoodProperties HIGHBALL = defaultShake().build();
    public static final FoodProperties DAVID_MARTINEZ = defaultShake()
            .effect(() -> new MobEffectInstance(SMMobEffects.EDGERUNNERS, 6000), 1.0F).build();
    public static final FoodProperties BUTTER_BEER = defaultShake()
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 300, 2), 1.0F).build();
    public static final FoodProperties ORANGE_JUICE = defaultShake()
            .effect(() -> new MobEffectInstance(SMMobEffects.SINCERELY_FOR_YOU, 1200), 1.0F).build();

    public static final FoodProperties SHIPWRECK_VINTAGE = defaultShake()
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 600), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.WITHER, 600), 1.0F).build();

    public static FoodProperties.Builder defaultShake() {
        return new FoodProperties.Builder().nutrition(0).saturationModifier(0).alwaysEdible();
    }
}
