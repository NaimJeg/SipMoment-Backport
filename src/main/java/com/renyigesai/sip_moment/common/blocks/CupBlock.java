package com.renyigesai.sip_moment.common.blocks;

import com.renyigesai.sip_moment.common.entitys.PouringWineEntity;
import com.renyigesai.sip_moment.common.init.SMEntityTypes;
import com.renyigesai.sip_moment.common.items.BottledWineItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CupBlock extends AbstractPileBlock{

    public static final IntegerProperty PILE;
    public static final VoxelShape BOX = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D);
    public final int maxPile;

    public CupBlock(Properties properties,int maxPile) {
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
        builder.add(FACING,PILE);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (canPourWine(state, itemInHand)) {
            if (level instanceof ServerLevel serverLevel) {
                BottledWineItem bottled = (BottledWineItem) itemInHand.getItem();
                PouringWineEntity pouringWineEntity = new PouringWineEntity(SMEntityTypes.POURING_WINE.get(), level);
                pouringWineEntity.setWineStack(bottled.createNewWine(itemStack));
                pouringWineEntity.setWineColor(bottled.getColor());
                pouringWineEntity.setCupPos(pos);
                pouringWineEntity.setInitY(bottled.getInitY());
                pouringWineEntity.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                pouringWineEntity.setOwner(player);
                float yRot = state.getValue(CupBlock.FACING).toYRot();
                pouringWineEntity.setYRot(yRot);
                pouringWineEntity.yRotO = yRot;
                serverLevel.addFreshEntity(pouringWineEntity);
                serverLevel.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS);
                itemInHand.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    private boolean canPourWine(BlockState state, ItemStack stack) {
        if (state.getBlock() instanceof CupBlock cupBlock && stack.getItem() instanceof BottledWineItem bottled) {
            Item cupItem = bottled.getCupStack().getItem();
            if (cupItem instanceof BlockItem blockItem) {
                return blockItem.getBlock() == cupBlock && state.getValue(PILE) == 1;
            }
        }
        return false;
    }

    static {
        PILE = IntegerProperty.create("pile",1,4);
    }
}
