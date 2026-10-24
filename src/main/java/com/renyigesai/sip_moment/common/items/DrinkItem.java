package com.renyigesai.sip_moment.common.items;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.init.SMDataComponents;
import com.renyigesai.sip_moment.common.init.SMMobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class DrinkItem extends PileItem{
    private final boolean customField;
    private final boolean canWine;
    public DrinkItem(Block block, Properties pProperties, boolean effectTooltip, boolean customField, boolean canWine) {
        super(block, pProperties, effectTooltip, customField);
        this.customField = customField;
        this.canWine = canWine;
    }

    public DrinkItem(Block block, PileProperties pileProperties, boolean customField, boolean canWine) {
        super(block, pileProperties);
        this.customField = customField;
        this.canWine = canWine;
    }

    public DrinkItem(Block block, Properties pProperties, int eatCount, boolean customField, boolean canWine) {
        super(block, pProperties.component(SMDataComponents.EAT_COUNT_MAX,eatCount).component(SMDataComponents.EAT_COUNT,eatCount));
        this.customField = customField;
        this.canWine = canWine;
    }

    public boolean isExtra(UseOnContext pContext) {
        ItemStack itemInHand = pContext.getItemInHand();
        if (isRepeatEat(itemInHand)){
            int eatCount = itemInHand.getOrDefault(SMDataComponents.EAT_COUNT,-1);
            int eatCountMax = itemInHand.getOrDefault(SMDataComponents.EAT_COUNT_MAX,-1);
            return eatCount == eatCountMax;
        }
        return false;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    public static boolean isRepeatEat(ItemStack stack){
        return stack.has(SMDataComponents.EAT_COUNT_MAX) && stack.has(SMDataComponents.EAT_COUNT);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 5592575;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        // Let vanilla apply food effects and statistics to a copy; only consume one sip.
        super.finishUsingItem(stack.copy(), level, living);
        return onConsume(level, living, stack);
    }

    @Override
    public net.minecraft.sounds.SoundEvent getEatingSound() {
        return net.minecraft.sounds.SoundEvents.GENERIC_DRINK;
    }
    public ItemStack onConsume(Level level, LivingEntity living, ItemStack stack){
        if (isRepeatEat(stack)){
            int eatCount = stack.getOrDefault(SMDataComponents.EAT_COUNT,-1);
            ItemStack cache = ItemStack.EMPTY;
            eat(level, living, stack);
            if (eatCount - 1 == 0){
                cache = stack.copy();
                stack.consume(1,living);
            }else {
                stack.set(SMDataComponents.EAT_COUNT,eatCount - 1);
            }
            return (eatCount - 1 == 0 && !cache.getCraftingRemainingItem().isEmpty()) ? cache.getCraftingRemainingItem() : stack;
        }
        return stack;
    }

    public void eat(Level level,LivingEntity living,ItemStack stack){

    }

    public int getBarWidth(ItemStack stack) {
        if (isRepeatEat(stack)) {
            Integer eatCount = stack.get(SMDataComponents.EAT_COUNT);
            Integer eatCountMax = stack.get(SMDataComponents.EAT_COUNT_MAX);
            if (eatCount != null && eatCountMax != null && eatCountMax > 0) {
                return Mth.clamp(Math.round(13.0F * eatCount / eatCountMax), 0, 13);
            }
        }
        return super.getBarWidth(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if (!isRepeatEat(stack)){
            return false;
        }
        Integer eatCount = stack.get(SMDataComponents.EAT_COUNT);
        Integer eatCountMax = stack.get(SMDataComponents.EAT_COUNT_MAX);
        if (eatCount != null &&  eatCountMax != null){
            return eatCount < eatCountMax;
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, java.util.List<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, builder, tooltipFlag);
        if (this.canWine){
            builder.add(Component.translatable("effect.sip_moment.drunk").append(" (00:30)").withStyle(ChatFormatting.RED));
        }
        if (isRepeatEat(stack)) {
            int eatCount = stack.getOrDefault(SMDataComponents.EAT_COUNT, -1);
            int eatCountMax = stack.getOrDefault(SMDataComponents.EAT_COUNT_MAX, -1);
            String translatable = "tooltips.sip_moment.repeat_eat_item_drink";
            builder.add(Component.literal(Component.translatable(translatable).getString() + eatCount + " / " + eatCountMax));
        }
        if (this.customField) {
            builder.add(Component.empty());
            builder.add(Component.translatable("tooltip." + this.toString()).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }
}
