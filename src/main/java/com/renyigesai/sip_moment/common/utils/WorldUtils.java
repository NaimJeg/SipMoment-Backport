package com.renyigesai.sip_moment.common.utils;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class WorldUtils {
    public static void saveAllItems(CompoundTag output, NonNullList<ItemStack> stacks, String name, HolderLookup.Provider registries) {
        ListTag items = new ListTag();
        for (int slot = 0; slot < stacks.size(); slot++) {
            if (!stacks.get(slot).isEmpty()) {
                CompoundTag entry = (CompoundTag) stacks.get(slot).save(registries);
                entry.putByte("Slot", (byte) slot);
                items.add(entry);
            }
        }
        output.put(name, items);
    }

    public static void loadItemsToHandler(CompoundTag input, ItemStackHandler handler, String name, HolderLookup.Provider registries) {
        ListTag items = input.getList(name, Tag.TAG_COMPOUND);
        for (int i = 0; i < items.size(); i++) {
            CompoundTag entry = items.getCompound(i);
            int slot = entry.getByte("Slot") & 255;
            if (slot < handler.getSlots()) {
                handler.setStackInSlot(slot, ItemStack.parseOptional(registries, entry));
            }
        }
    }
}
