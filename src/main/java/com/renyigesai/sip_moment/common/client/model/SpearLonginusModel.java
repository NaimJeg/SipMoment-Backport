package com.renyigesai.sip_moment.common.client.model;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;

public class SpearLonginusModel<T extends Entity> extends Model {
    private final ModelPart root;

    @Override
    public void renderToBuffer(com.mojang.blaze3d.vertex.PoseStack poses, com.mojang.blaze3d.vertex.VertexConsumer vertices, int light, int overlay, int color) {
        root.render(poses, vertices, light, overlay, color);
    }
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("sip_moment", "textures/entity/spear_longinus.png"), "main");

	public SpearLonginusModel(ModelPart root) {
		super(RenderType::entitySolid);
        this.root = root;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(12, 0).addBox(-0.5F, -24.0F, -0.75F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.15F))
				.texOffs(0, 0).addBox(-0.5F, -18.0F, -0.75F, 1.0F, 13.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(4, 0).addBox(-0.5F, -6.0F, -0.75F, 1.0F, 13.0F, 1.0F, new CubeDeformation(-0.1F))
				.texOffs(4, 0).addBox(-0.5F, 6.75F, -0.75F, 1.0F, 13.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 42.0F, 0.0F));

		PartDefinition cube_r1 = main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -10.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(-0.1F))
				.texOffs(8, 8).addBox(-1.0F, -16.0F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(-0.15F))
				.texOffs(16, 2).addBox(-1.0F, -5.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9523F, -26.4391F, 0.0F, 0.0F, 0.0F, -0.0436F));

		PartDefinition cube_r2 = main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(8, 16).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.3911F, -25.5063F, 0.5F, -0.0873F, 0.0F, 0.8727F));

		PartDefinition cube_r3 = main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(12, 16).addBox(-1.0F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.2539F, -26.3143F, 0.0F, 0.0F, 0.0F, 0.5672F));

		PartDefinition cube_r4 = main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(16, 16).addBox(-3.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.1675F, -28.4041F, 0.0F, 0.0F, 0.0F, -1.309F));

		PartDefinition cube_r5 = main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(16, 7).addBox(0.0F, -5.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(12, 7).addBox(0.0F, -10.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(-0.1F))
				.texOffs(8, 0).addBox(0.0F, -16.0F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(-2.9523F, -26.4391F, -0.5F, 0.0F, 0.0F, 0.0436F));

		PartDefinition cube_r6 = main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(16, 0).addBox(0.0F, -1.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3911F, -25.5063F, -1.0F, 0.0F, -0.0873F, 0.6981F));

		PartDefinition cube_r7 = main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(16, 12).addBox(-3.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1323F, -24.5404F, -0.5F, 0.0F, 0.0F, 1.309F));

		PartDefinition cube_r8 = main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(12, 14).addBox(-1.0F, -1.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2539F, -26.3143F, -0.5F, 0.0F, 0.0F, 1.0036F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

}