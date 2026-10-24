package com.renyigesai.sip_moment.common.blocks;
import com.renyigesai.sip_moment.common.utils.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AbstractPileBlock extends Block {

    public static final EnumProperty<Direction> FACING;
    public AbstractPileBlock(Properties properties) {
        super(properties);
    }

    public abstract Property<Integer> getPileProperty();

    public abstract int getMaxPile();

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isShiftKeyDown()) return InteractionResult.PASS;
        if (!level.isClientSide()) take(state, level, pos, player);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()){
            return ItemInteractionResult.SUCCESS;
        }
        if (!player.isShiftKeyDown() && player.getItemInHand(hand).isEmpty()){
            return take(state, level, pos, player);
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    public ItemInteractionResult take(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer){
        int i =  pState.getValue(getPileProperty());
        if (i == 1){
            pLevel.removeBlock(pPos,false);
        }else {
            pLevel.setBlock(pPos,pState.setValue(getPileProperty(),i-1),3);
        }
        ItemUtils.givePlayerItem(pPlayer,new ItemStack(this.asItem()));
        pLevel.playSound(null,pPos,getTakeSound(), SoundSource.BLOCKS);
        return ItemInteractionResult.SUCCESS;
    }

    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    public abstract SoundEvent getPlaceSound();

    public SoundEvent getTakeSound(){
        return SoundEvents.ITEM_FRAME_REMOVE_ITEM;
    }

    static {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
    }
}
