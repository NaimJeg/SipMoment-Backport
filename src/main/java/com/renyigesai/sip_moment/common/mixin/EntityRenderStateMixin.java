package com.renyigesai.sip_moment.common.mixin;

import com.renyigesai.sip_moment.common.entitys.IEntityRenderStateOffset;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public abstract class EntityRenderStateMixin implements IEntityRenderStateOffset {

    @Unique
    private Vec3 visualWorldPos = null;

    @Override
    public void setVisualWorldPos(Vec3 pos) {
        this.visualWorldPos = pos;
    }

    @Override
    public Vec3 getVisualWorldPos() {
        return this.visualWorldPos;
    }
}