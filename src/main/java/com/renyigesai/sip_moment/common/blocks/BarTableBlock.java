package com.renyigesai.sip_moment.common.blocks;

import com.mojang.serialization.MapCodec;
import com.renyigesai.sip_moment.common.client.gui.WineListMenu;
import com.renyigesai.sip_moment.common.init.SMAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BarTableBlock extends HorizontalDirectionalBlock {
    public static final VoxelShape BOX_UP;
    public static final VoxelShape BOX_DOWN;
    public static final VoxelShape BOX;

    public BarTableBlock(ResourceLocation identifier) {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));
    }

    public BarTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(BarTableBlock::new);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            player.setData(SMAttachments.WINE_LIST_PAGE, 0);
            player.openMenu(getMenuProvider());
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected net.minecraft.world.InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            player.setData(SMAttachments.WINE_LIST_PAGE, 0);
            player.openMenu(getMenuProvider());
        }
        return net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide());
    }

    public MenuProvider getMenuProvider() {
        return new SimpleMenuProvider((p_53812_, p_53813_, p_53814_) -> new WineListMenu(p_53812_,p_53813_), Component.translatable("container.bakeries.wine_list").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.ITALIC));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    static {
        BOX_UP = box(0,13,0,16,16,16);
        BOX_DOWN = box(5,0,5,11,13,11);
        BOX = Shapes.or(BOX_UP,BOX_DOWN);
    }
}
