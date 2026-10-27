package com.renyigesai.sip_moment.common.entitys;

import net.minecraft.world.phys.Vec3;

public interface ITimeScaledEntity {
    float getTimeScale();
    void setTimeScale(float scale);

    int getTimeStopLease();
    void setTimeStopLease(int ticks);

    boolean accumulateAndShouldSkip();

    Vec3 getVisualPos();
    void updateVisualPosition(Vec3 target);

    void setSlow(float scale,int ticks);
}