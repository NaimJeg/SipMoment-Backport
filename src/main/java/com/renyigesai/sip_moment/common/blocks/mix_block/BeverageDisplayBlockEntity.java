package com.renyigesai.sip_moment.common.blocks.mix_block;

import com.mojang.logging.LogUtils;
import com.renyigesai.sip_moment.common.init.SMBlocks;
import com.renyigesai.sip_moment.common.utils.ItemUtils;
import com.renyigesai.sip_moment.common.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;

import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.slf4j.Logger;
@SuppressWarnings("removal")
public class BeverageDisplayBlockEntity extends BlockEntity {


    private ItemStackHandler inventory;
    private static final Logger LOGGER = LogUtils.getLogger();

    public BeverageDisplayBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(SMBlocks.Entitys.BEVERAGE_DISPLAY_BLOCK_ENTITY.get(), pPos, pBlockState);
        inventory = new ItemStackHandler(4);
    }

    public int getInventoryCount() {
        int count = 0;
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()){
                count ++;
            }
        }
        return count;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public NonNullList<ItemStack> getItems() {
        return com.renyigesai.sip_moment.common.utils.ItemUtils.toNonNullList(this.inventory);
    }

    public boolean isEmpty(){
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()){
                return false;
            }
        }
        return true;
    }

    public boolean stillValid(Player player) {
        return false;
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }


    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void loadAdditional(CompoundTag input, HolderLookup.Provider registries) {
        super.loadAdditional(input, registries);
        this.inventory = new ItemStackHandler(4);
        WorldUtils.loadItemsToHandler(input,this.inventory,"Items", registries);
    }

    @Override
    protected void saveAdditional(CompoundTag output, HolderLookup.Provider registries) {
        super.saveAdditional(output, registries);
        WorldUtils.saveAllItems(output, ItemUtils.toNonNullList(this.inventory),"Items", registries);
    }

    public boolean addItem(ItemStack stack){
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stackInSlot = inventory.getStackInSlot(i);
            if (stackInSlot.isEmpty()){
                inventory.setStackInSlot(i,stack);
                updateBlock();
                return true;
            }
        }
        return false;
    }

    public Level level(){
        return this.level;
    }

    public Vec3 position() {
        return this.getBlockPos().getCenter();
    }

    public float getVisualRotationYInDegrees() {
        return ((Direction)this.getBlockState().getValue(BeverageDisplayBlock.FACING)).getOpposite().toYRot();
    }

    public void updateBlock() {
        if (level == null){
            return;
        }
        BlockState state = level.getBlockState(worldPosition);
        setChanged(level, worldPosition, state);
        level.sendBlockUpdated(worldPosition, state, state, 3);
    }
}
