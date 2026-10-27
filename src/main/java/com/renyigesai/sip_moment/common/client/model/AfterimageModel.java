package com.renyigesai.sip_moment.common.client.model;

import com.renyigesai.sip_moment.common.entitys.AfterimageEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

public class AfterimageModel extends HumanoidModel<AfterimageEntity> {
    public AfterimageModel(ModelPart root) {
        super(root);
        young = false;
    }
}
