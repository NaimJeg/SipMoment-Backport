package com.renyigesai.sip_moment.common.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;

public class AfterimageModel<S extends ZombieRenderState> extends HumanoidModel<S> {
    public AfterimageModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(S state) {
        super.setupAnim(state);
    }
}
