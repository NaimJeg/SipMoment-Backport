package com.renyigesai.sip_moment.common.client.renderer.blockentity.mix_block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.renyigesai.sip_moment.common.blocks.mix_block.BeverageDisplayBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec2;

public class BeverageDisplayBlockRender implements BlockEntityRenderer<BeverageDisplayBlockEntity> {
    private final BlockRenderDispatcher blocks;

    public BeverageDisplayBlockRender(BlockEntityRendererProvider.Context context) {
        blocks = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(BeverageDisplayBlockEntity entity, float partialTick, PoseStack poses, MultiBufferSource buffers, int light, int overlay) {
        int count = entity.getInventoryCount();
        if (count == 0) return;
        Direction facing = entity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        int index = 0;
        for (int slot = 0; slot < entity.getInventory().getSlots(); slot++) {
            var stack = entity.getInventory().getStackInSlot(slot);
            if (stack.isEmpty()) continue;
            Vec2 position = transformPositionByDirection(VEC2S[count][index++], facing);
            if (stack.getItem() instanceof BlockItem item) {
                poses.pushPose();
                poses.translate(position.x, 0, position.y);
                poses.mulPose(Axis.YP.rotationDegrees(-facing.toYRot() + 15));
                poses.translate(-0.5, 0, -0.5);
                blocks.renderSingleBlock(item.getBlock().defaultBlockState(), poses, buffers, light, overlay);
                poses.popPose();
            }
        }
    }
    public static final float ADD_SIZE = 0.25f;
    public static final Vec2[][] VEC2S = {
            new Vec2[]{},
            new Vec2[]{new Vec2(0.5f, 0.5f)},
            new Vec2[]{new Vec2(0.5f + ADD_SIZE, 0.5f), new Vec2(0.5f - ADD_SIZE, 0.5f)},
            new Vec2[]{new Vec2(0.5f + ADD_SIZE, 0.5f + ADD_SIZE), new Vec2(0.5f - ADD_SIZE, 0.5f + ADD_SIZE), new Vec2(0.5f, 0.5f - ADD_SIZE)},
            new Vec2[]{new Vec2(0.5f + ADD_SIZE, 0.5f + ADD_SIZE), new Vec2(0.5f - ADD_SIZE, 0.5f + ADD_SIZE), new Vec2(0.5f + ADD_SIZE, 0.5f - ADD_SIZE), new Vec2(0.5f - ADD_SIZE, 0.5f - ADD_SIZE)}
    };
    private Vec2 transformPositionByDirection(Vec2 position, Direction direction) {
        float x = position.x;
        float y = position.y;
        return switch (direction) {
            case NORTH -> new Vec2(1 - x, 1 - y);
            case SOUTH -> new Vec2(x, y);
            case EAST -> new Vec2(y, 1 - x);
            case WEST -> new Vec2(1 - y, x);
            default -> position;
        };
    }
}
