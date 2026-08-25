package com.renyigesai.sip_moment.common.items;

import com.renyigesai.sip_moment.common.blocks.CupBlock;
import com.renyigesai.sip_moment.common.entitys.PouringWineEntity;
import com.renyigesai.sip_moment.common.init.SMDataComponents;
import com.renyigesai.sip_moment.common.init.SMEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BottledWineItem extends PileItem{

    public final Supplier<Item> cupStack;
    public final Supplier<Item> wineStack;
    private float initY = 0.5f;
    private int color = 16733525;
    private final int maxEatCount;

    public BottledWineItem(Block block, Properties pProperties, Supplier<Item> cupStack, Supplier<Item> wineStack,int maxEatCount,int color,float initY) {
        super(block, pProperties.component(SMDataComponents.EAT_COUNT_MAX,maxEatCount).component(SMDataComponents.EAT_COUNT,maxEatCount).stacksTo(1));
        this.cupStack = cupStack;
        this.wineStack = wineStack;
        this.color = color;
        this.initY = initY;
        this.maxEatCount = maxEatCount;
    }

    public ItemStack getCupStack() {
        return new ItemStack(cupStack.get());
    }

    public ItemStack getWineStack() {
        return new ItemStack(wineStack.get());
    }

    public float getInitY() {
        return initY;
    }

    public int getColor() {
        return color;
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
    public ItemUseAnimation getUseAnimation(ItemStack pStack) {
        return ItemUseAnimation.DRINK;
    }

    public static boolean isRepeatEat(ItemStack stack){
        return stack.has(SMDataComponents.EAT_COUNT_MAX) && stack.has(SMDataComponents.EAT_COUNT);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 5592575;
    }

    public ItemStack onPouringWine(ItemStack stack){
        if (isRepeatEat(stack)){
            int eatCount = stack.getOrDefault(SMDataComponents.EAT_COUNT,-1);
            ItemStack cache = ItemStack.EMPTY;
            if (eatCount - 1 == 0){
                cache = stack.copy();
                stack.shrink(1);
            }else {
                stack.set(SMDataComponents.EAT_COUNT,eatCount - 1);
            }
            return (eatCount - 1 == 0 && cache.getCraftingRemainder() != null) ? cache.getCraftingRemainder().create() : stack;
        }
        return stack;
    }

    public ItemStack createNewWine(ItemStack wine){
        ItemStack newWine = new ItemStack(this);
        if (isRepeatEat(wine)){
            int eatCount = wine.getOrDefault(SMDataComponents.EAT_COUNT, -1);
            newWine.set(SMDataComponents.EAT_COUNT_MAX,this.maxEatCount);
            newWine.set(SMDataComponents.EAT_COUNT,eatCount);
        }
        return newWine;
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
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, builder, tooltipFlag);
        if (isRepeatEat(stack)) {
            int eatCount = stack.getOrDefault(SMDataComponents.EAT_COUNT, -1);
            int eatCountMax = stack.getOrDefault(SMDataComponents.EAT_COUNT_MAX, -1);
            String translatable = "tooltips.sip_moment.repeat_eat_item_drink";
            builder.accept(Component.literal(Component.translatable(translatable).getString() + eatCount + " / " + eatCountMax));
        }

        builder.accept(Component.empty());
        builder.accept(Component.translatable("tooltip." + this.toString()).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
    }
}
