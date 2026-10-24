package com.renyigesai.sip_moment.common.init;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.client.model.SpearLonginusModel;
import com.renyigesai.sip_moment.common.client.particles.DrunkParticle;
import com.renyigesai.sip_moment.common.client.particles.DrunkParticleOptions;
import com.renyigesai.sip_moment.common.client.particles.WineLiquidParticle;
import com.renyigesai.sip_moment.common.client.particles.WineLiquidParticleOptions;
import com.renyigesai.sip_moment.common.client.renderer.blockentity.mix_block.BeverageDisplayBlockRender;
import com.renyigesai.sip_moment.common.manager.LivingEntityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.player.LocalPlayer;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT,modid = SipMomentMod.MODID)
public class SMClientHandler {
    @SubscribeEvent
    public static void registerGeometryLoaders(net.neoforged.neoforge.client.event.ModelEvent.RegisterGeometryLoaders event) {
        event.register(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "rotated_elements"),
                new com.renyigesai.sip_moment.common.client.model.RotatedElementsModel.Loader());
    }

    @SubscribeEvent
    public static void onRenders(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(SMBlocks.Entitys.BEVERAGE_DISPLAY_BLOCK_ENTITY.get(), BeverageDisplayBlockRender::new);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SpearLonginusModel.LAYER_LOCATION, SpearLonginusModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SMParticleTypes.WINE_LIQUID.get(), (spriteSet)-> new ParticleProvider<>() {
            @Override
            public @Nullable Particle createParticle(WineLiquidParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux) {
                return new WineLiquidParticle(level,x,y,z,xAux,yAux,zAux,options,spriteSet);
            }
        });

        event.registerSpriteSet(SMParticleTypes.DRUNK.get(), (spriteSet)-> new ParticleProvider<>() {
            @Override
            public @Nullable Particle createParticle(DrunkParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux) {
                return new DrunkParticle(level,x,y,z,xAux,yAux,zAux,options,spriteSet);
            }
        });
    }

    @EventBusSubscriber(value = Dist.CLIENT, modid = SipMomentMod.MODID)
    public static class CameraEvents {
        @SubscribeEvent
        public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
            Minecraft instance = Minecraft.getInstance();
            LocalPlayer player = instance.player;
            if (player != null) {
                int capacity_for_liquor = player.getData(SMAttachments.CAPACITY_FOR_LIQUOR);
                if (player.hasEffect(SMMobEffects.DRUNK) && player.getEffect(SMMobEffects.DRUNK).getAmplifier() >= capacity_for_liquor) {
                    float time = (player.tickCount + instance.getTimer().getGameTimeDeltaPartialTick(false)) / 20f;
                    int amplifier = player.getEffect(SMMobEffects.DRUNK).getAmplifier();
                    float period = amplifier == capacity_for_liquor ? 5f : 15f;
                    float baseMaxAngle = amplifier == capacity_for_liquor ? 45f : 180f;
                    float effectScale = instance.options.screenEffectScale().get().floatValue();//根据屏幕扭曲效果控制摆动幅度
                    float maxAngle = (float) (baseMaxAngle * effectScale);
                    float rollAngle = maxAngle * (float) Math.sin(2.0 * Math.PI * time / period);
                    event.setRoll(rollAngle);
                }
            }
        }
    }
}
