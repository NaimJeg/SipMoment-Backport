package com.renyigesai.sip_moment.common.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.IModelBuilder;
import net.neoforged.neoforge.client.model.QuadTransformers;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.SimpleUnbakedGeometry;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/** Bakes the original artwork's arbitrary element rotations on Minecraft 1.21.1. */
public class RotatedElementsModel extends SimpleUnbakedGeometry<RotatedElementsModel> {
    private final List<Element> elements;

    private RotatedElementsModel(List<Element> elements) {
        this.elements = elements;
    }

    @Override
    protected void addQuads(IGeometryBakingContext context, IModelBuilder<?> builder, ModelBaker baker,
                            Function<Material, TextureAtlasSprite> sprites, ModelState state) {
        Transformation blockRotation = state.getRotation().applyOrigin(new Vector3f(0.5F));
        for (Element entry : elements) {
            BlockElement element = entry.element();
            Transformation transform = blockRotation.compose(context.getRootTransform()).compose(entry.rotation());
            var transformer = QuadTransformers.applying(transform);
            for (Direction direction : element.faces.keySet()) {
                var face = element.faces.get(direction);
                var sprite = sprites.apply(context.getMaterial(face.texture()));
                BakedQuad quad = BlockModel.bakeFace(element, face, sprite, direction, BlockModelRotation.X0_Y0);
                transformer.processInPlace(quad);
                // Recompute the facing used for lighting after rotating the vertices and normals.
                builder.addUnculledFace(new BakedQuad(quad.getVertices(), quad.getTintIndex(),
                        FaceBakery.calculateFacing(quad.getVertices()), sprite, quad.isShade(), quad.hasAmbientOcclusion()));
            }
        }
    }

    private record Element(BlockElement element, Transformation rotation) {}

    public static class Loader implements IGeometryLoader<RotatedElementsModel> {
        @Override
        public RotatedElementsModel read(JsonObject json, JsonDeserializationContext context) {
            List<Element> elements = new ArrayList<>();
            for (JsonElement value : GsonHelper.getAsJsonArray(json, "sip_elements")) {
                JsonObject element = value.getAsJsonObject().deepCopy();
                Transformation transform = Transformation.identity();
                if (element.has("rotation")) {
                    JsonObject rotation = element.remove("rotation").getAsJsonObject();
                    var originArray = GsonHelper.getAsJsonArray(rotation, "origin");
                    Vector3f origin = new Vector3f(originArray.get(0).getAsFloat(),
                            originArray.get(1).getAsFloat(), originArray.get(2).getAsFloat()).div(16F);
                    float x = GsonHelper.getAsFloat(rotation, "x", 0);
                    float y = GsonHelper.getAsFloat(rotation, "y", 0);
                    float z = GsonHelper.getAsFloat(rotation, "z", 0);
                    if (rotation.has("axis")) {
                        float angle = GsonHelper.getAsFloat(rotation, "angle");
                        switch (GsonHelper.getAsString(rotation, "axis")) {
                            case "x" -> x = angle;
                            case "y" -> y = angle;
                            case "z" -> z = angle;
                            default -> throw new com.google.gson.JsonParseException("Invalid rotation axis");
                        }
                    }
                    float radians = (float) Math.PI / 180F;
                    Quaternionf quaternion = new Quaternionf().rotationZYX(z * radians, y * radians, x * radians);
                    transform = new Transformation(new Matrix4f().translation(origin).rotate(quaternion)
                            .translate(-origin.x, -origin.y, -origin.z));
                }
                elements.add(new Element(context.deserialize(element, BlockElement.class), transform));
            }
            return new RotatedElementsModel(elements);
        }
    }
}
