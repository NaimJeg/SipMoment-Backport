package com.renyigesai.sip_moment.common.init;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.potion.DrunkMobEffect;
import com.renyigesai.sip_moment.common.potion.EdgerunnersMobEffect;
import com.renyigesai.sip_moment.common.potion.SMMobEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SMMobEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, SipMomentMod.MODID);

    public static final Holder<MobEffect> DRUNK = EFFECTS.register("drunk", DrunkMobEffect::new);

    public static final Holder<MobEffect> EDGERUNNERS = EFFECTS.register("edgerunners", EdgerunnersMobEffect::new);
    public static final Holder<MobEffect> FURIOUS = EFFECTS.register("furious", ()-> new SMMobEffect(MobEffectCategory.BENEFICIAL, 0)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID,"effect.furious.attack_damage"),10.0,  AttributeModifier.Operation.ADD_VALUE)
            .addAttributeModifier(Attributes.MAX_HEALTH, ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID,"effect.furious.max_health"),-5,  AttributeModifier.Operation.ADD_VALUE)
            .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID,"effect.furious.knockback_resistance"),0.5,  AttributeModifier.Operation.ADD_VALUE)
    );
    public static final Holder<MobEffect> SINCERELY_FOR_YOU = EFFECTS.register("sincerely_for_you", ()-> new SMMobEffect(MobEffectCategory.BENEFICIAL, 0));

}
