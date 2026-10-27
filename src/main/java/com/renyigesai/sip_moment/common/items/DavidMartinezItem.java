package com.renyigesai.sip_moment.common.items;

import com.renyigesai.sip_moment.common.init.SMSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class DavidMartinezItem extends WineItem{

    public DavidMartinezItem(Block block, Properties pProperties, int eatCount, boolean canWine) {
        super(block, pProperties, eatCount, canWine);
    }

    @Override
    public @NonNull ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        Vec3 position = entity.position();
        level.playSound(null,position.x,position.y,position.z, SMSounds.EDGERUNNERS.get(), SoundSource.BLOCKS);
        return super.finishUsingItem(itemStack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, builder, tooltipFlag);
        builder.accept(Component.empty());
        builder.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
        builder.accept(Component.translatable("tooltip.sip_moment:david_martinez_2").withStyle(ChatFormatting.BLUE));
    }
}
