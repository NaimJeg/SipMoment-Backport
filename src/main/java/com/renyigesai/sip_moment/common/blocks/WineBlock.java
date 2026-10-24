package com.renyigesai.sip_moment.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WineBlock extends AbstractPileBlock {

    public static final IntegerProperty PILE;
    public static final VoxelShape BOX = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D);
    public final int maxPile;

    public WineBlock(Properties properties,int maxPile) {
        super(properties);
        this.maxPile = maxPile;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    public Property<Integer> getPileProperty() {
        return PILE;
    }

    @Override
    public int getMaxPile() {
        return maxPile;
    }

    @Override
    public SoundEvent getPlaceSound() {
        return SoundEvents.GLASS_PLACE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, getPileProperty());
    }

    public static class TwoPile extends WineBlock {
        private static final IntegerProperty TWO_PILE = IntegerProperty.create("pile", 1, 2);

        public TwoPile(Properties properties) {
            super(properties, 2);
        }

        @Override
        public Property<Integer> getPileProperty() {
            return TWO_PILE;
        }
    }

    static {
        PILE = IntegerProperty.create("pile",1,4);
    }
}
