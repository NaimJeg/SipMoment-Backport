package com.renyigesai.sip_moment.common.items;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.init.SMAttachments;
import com.renyigesai.sip_moment.common.init.SMDataComponents;
import com.renyigesai.sip_moment.common.init.SMMobEffects;
import com.renyigesai.sip_moment.common.manager.LivingEntityManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Random;
import java.util.function.Consumer;

public class WineItem extends DrinkItem{

    public WineItem(Block block, Properties pProperties, int eatCount,boolean canWine) {
        super(block, pProperties, eatCount,canWine,true);
    }

    public void eat(Level level,LivingEntity living,ItemStack stack){
        if (!level.isClientSide() && living.getData(SMAttachments.CAPACITY_FOR_LIQUOR) < 0){
            int random = new Random().nextInt(0, 9);
            living.setData(SMAttachments.CAPACITY_FOR_LIQUOR,random);
            if (living instanceof Player player){
                MutableComponent mutableComponent = Component.literal(String.valueOf(random + 1)).withStyle(ChatFormatting.GOLD);
                player.displayClientMessage(Component.translatable("tooltip.sip_moment.player_capacity_for_liquor").append(mutableComponent), true);
            }
        }
        if (!level.isClientSide()) {
            try {
                if (living.hasEffect(SMMobEffects.DRUNK) ) {
                    if (living.getEffect(SMMobEffects.DRUNK).getAmplifier() < 9){
                        int amplifier = living.getEffect(SMMobEffects.DRUNK).getAmplifier();
                        living.removeEffect(SMMobEffects.DRUNK);
                        living.addEffect(new MobEffectInstance(SMMobEffects.DRUNK, 600, amplifier + 1,false,true), living);
                    }
                }else {
                    living.addEffect(new MobEffectInstance(SMMobEffects.DRUNK, 600,0,false,true));
                }
            }catch (NullPointerException e){
                SipMomentMod.LOGGER.error("WineItem eat {}", e);
            }
        }
    }
}
