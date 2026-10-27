package com.renyigesai.sip_moment.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class OrangeJuiceItem extends DrinkItem {

    public OrangeJuiceItem(Block block, Properties pProperties, int eatCount, boolean customField, boolean canWine) {
        super(block, pProperties, eatCount, customField, canWine);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, builder, tooltipFlag);
        builder.accept(Component.empty());
        builder.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
        builder.accept(Component.translatable("tooltip.sip_moment:orange_juice_2").withStyle(ChatFormatting.BLUE));
    }
}
