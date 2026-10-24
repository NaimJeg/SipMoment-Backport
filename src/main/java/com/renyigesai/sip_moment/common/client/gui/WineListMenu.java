package com.renyigesai.sip_moment.common.client.gui;

import com.renyigesai.sip_moment.common.data.WineListCatalog;
import com.renyigesai.sip_moment.common.data.WineListIngredient;
import com.renyigesai.sip_moment.common.init.SMAttachments;
import com.renyigesai.sip_moment.common.init.SMMenuType;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

import java.util.List;

@SuppressWarnings("removal")
public class WineListMenu extends AbstractContainerMenu {
    private final IItemHandler playerInventory;
    private final DataSlot currentPage = DataSlot.standalone();
    private static final int[] LINE = new int[]{66,98,130,162};
    protected final int itemsPerPage = 4;
    private int page = 0;
    public WineListMenu(int w, Inventory playerInventory) {
        super(SMMenuType.WINE_LIST_MENU.get(), w);
        this.playerInventory = new InvWrapper(playerInventory);
        int page = playerInventory.player.getData(SMAttachments.WINE_LIST_PAGE);
        this.currentPage.set(page);
        rebuildSlots(0);
    }

    public void rebuildSlots(int newPage) {
        this.page = newPage;
        if (!(page >= 0)){
            return;
        }
        this.slots.removeIf(slot -> slot instanceof WineListSlot);
        int start = page * itemsPerPage;
        int end = Math.min(start + itemsPerPage, WineListCatalog.getWineList().size());
        int line = 0;
        for (int i = start; i < end; i++) {
            WineListIngredient stacks = WineListCatalog.getWineList(true).get(i);
            List<ItemStack> gives = stacks.getGives();

            List<ItemStack> results = stacks.getResults();

            boolean temp1 = false;
            boolean temp2 = false;
            int inx1 = 0;
            for (; inx1 < gives.size(); inx1++) {
                ItemStack give = gives.get(inx1);
                if (!give.isEmpty()) {
                    addSlot(new WineListSlot(toItemHandler(give),0,8 + (inx1 * 32) + 32, LINE[line] - 22));
                    temp1 = true;
                }
            }
            int inx2 = 0;
            for (; inx2 < results.size(); inx2++) {
                ItemStack result = results.get(inx2);
                if (!result.isEmpty()) {
                    addSlot(new WineListSlot(toItemHandler(result),0,96 + (inx2 * 16), LINE[line] - 22));
                    temp2 = true;
                }
            }
            if (temp1 && temp2) {
                line++;
            }
        }
    }

    private ItemStackHandler toItemHandler(ItemStack stack){
        ItemStackHandler itemStackHandler = new ItemStackHandler(1);
        itemStackHandler.setStackInSlot(0,stack);
        return itemStackHandler;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public static WineListMenu create(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        return new WineListMenu(windowId,playerInventory);
    }

    public int getPage() {
        return page;
    }

    public DataSlot getCurrentPage() {
        return currentPage;
    }

    public static class WineListSlot extends SlotItemHandler{

        public WineListSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player playerIn) {
            return false;
        }
    }
}
