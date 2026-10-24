package com.renyigesai.sip_moment.common.utils;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.component.ItemAttributeModifiers;



import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class TextUtils {

//    public static MutableComponent getTranslation(String key, Object... args) {
//        return Component.translatable(BakeriesMod.MODID + "." + key, args);
//    }

    //By Farmersdelight
    public static void addFoodEffectTooltip(ItemStack stack, Consumer<Component> tooltipAdder, float durationFactor, float tickRate) {
        if (!stack.has(DataComponents.FOOD)){
            return;
        }
        net.minecraft.world.food.FoodProperties consumable = stack.get(DataComponents.FOOD);
        if (consumable != null) {
            List<MobEffectInstance> effectList = new java.util.ArrayList<>(List.of());
            consumable.effects().forEach(effect -> effectList.add(effect.effect()));

            List<Pair<Holder<Attribute>, AttributeModifier>> attributeList = Lists.newArrayList();
            MutableComponent mutableComponent;
            Iterator var8;
            MobEffect effect;
            if (effectList.isEmpty()) {
//                tooltipAdder.accept(NO_EFFECTS);
            } else {
                for(var8 = effectList.iterator(); var8.hasNext(); tooltipAdder.accept(mutableComponent.withStyle(effect.getCategory().getTooltipFormatting()))) {
                    MobEffectInstance instance = (MobEffectInstance)var8.next();
                    mutableComponent = Component.translatable(instance.getDescriptionId());
                    effect = (MobEffect)instance.getEffect().value();
                    effect.createModifiers(instance.getAmplifier(), (attributeHolder, attributeModifier) -> {
                        attributeList.add(new Pair<>(attributeHolder, attributeModifier));
                    });
                    if (instance.getAmplifier() > 0) {
                        mutableComponent = Component.translatable("potion.withAmplifier", new Object[]{mutableComponent, Component.translatable("potion.potency." + instance.getAmplifier())});
                    }

                    if (instance.getDuration() > 20) {
                        mutableComponent = Component.translatable("potion.withDuration", new Object[]{mutableComponent, MobEffectUtil.formatDuration(instance, durationFactor, tickRate)});
                    }
                }
            }

            if (!attributeList.isEmpty()) {
                tooltipAdder.accept(CommonComponents.EMPTY);
                tooltipAdder.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
                var8 = attributeList.iterator();

                while(var8.hasNext()) {
                    Pair<Holder<Attribute>, AttributeModifier> pair = (Pair)var8.next();
                    AttributeModifier attributemodifier = (AttributeModifier)pair.getSecond();
                    double amount = attributemodifier.amount();
                    double formattedAmount;
                    if (attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE && attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                        formattedAmount = attributemodifier.amount();
                    } else {
                        formattedAmount = attributemodifier.amount() * 100.0;
                    }

                    if (amount > 0.0) {
                        tooltipAdder.accept(Component.translatable("attribute.modifier.plus." + attributemodifier.operation().id(), new Object[]{ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount), Component.translatable(((Attribute)((Holder)pair.getFirst()).value()).getDescriptionId())}).withStyle(ChatFormatting.BLUE));
                    } else if (amount < 0.0) {
                        formattedAmount *= -1.0;
                        tooltipAdder.accept(Component.translatable("attribute.modifier.take." + attributemodifier.operation().id(), new Object[]{ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount), Component.translatable(((Attribute)((Holder)pair.getFirst()).value()).getDescriptionId())}).withStyle(ChatFormatting.RED));
                    }
                }
            }

        }
    }

}
