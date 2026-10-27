package com.renyigesai.sip_moment.common.utils;
import com.renyigesai.sip_moment.SipMomentMod;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.ItemStackHandler;

public class WorldUtils {

    public static void saveAllItems(ValueOutput output, NonNullList<ItemStack> itemStacks,String name) {
        ValueOutput.TypedOutputList<ItemStackWithSlot> itemsOutput = output.list(name, ItemStackWithSlot.CODEC);

        for (int i = 0; i < itemStacks.size(); i++) {
            ItemStack itemStack = itemStacks.get(i);
            if (!itemStack.isEmpty()) {
                itemsOutput.add(new ItemStackWithSlot(i, itemStack));
            }
        }
    }

    public static void loadAllItems(ValueInput input, NonNullList<ItemStack> itemStacks,String name) {
        for (ItemStackWithSlot item : input.listOrEmpty(name, ItemStackWithSlot.CODEC)) {
            if (item.isValidInContainer(itemStacks.size())) {
                itemStacks.set(item.slot(), item.stack());
            }
        }
    }

    @SuppressWarnings("removal")
    public static void loadItemsToHandler(ValueInput input, ItemStackHandler handler, String key) {
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(handler.getSlots(),ItemStack.EMPTY);
        loadAllItems(input, nonNullList, key);
        for (int i = 0; i < nonNullList.size(); i++) {
            handler.setStackInSlot(i, nonNullList.get(i));
        }
    }

    @SuppressWarnings("removal")
    public static void saveItemsToHandler(ValueOutput output, ItemStackHandler handler, String key) {
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(handler.getSlots(),ItemStack.EMPTY);
        saveAllItems(output, nonNullList, key);
        for (int i = 0; i < nonNullList.size(); i++) {
            handler.setStackInSlot(i, nonNullList.get(i));
        }
    }

}
