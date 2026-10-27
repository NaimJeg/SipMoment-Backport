package com.renyigesai.sip_moment.common.entitys;

import net.minecraft.world.phys.Vec3;

public interface IEntityRenderStateOffset {
    void setVisualWorldPos(Vec3 pos);
    Vec3 getVisualWorldPos();
}
