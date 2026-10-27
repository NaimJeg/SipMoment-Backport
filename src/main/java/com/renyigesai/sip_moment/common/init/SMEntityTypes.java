package com.renyigesai.sip_moment.common.init;


import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.blocks.sofa.BarChairEntity;
import com.renyigesai.sip_moment.common.client.renderer.entity.AfterimageRender;
import com.renyigesai.sip_moment.common.client.renderer.entity.SpearLonginusRender;
import com.renyigesai.sip_moment.common.entitys.AfterimageEntity;
import com.renyigesai.sip_moment.common.entitys.PouringWineEntity;
import com.renyigesai.sip_moment.common.entitys.ThrownSpearLonginus;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SMEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY =
            DeferredRegister.create(Registries.ENTITY_TYPE, SipMomentMod.MODID);

    public static final Supplier<EntityType<PouringWineEntity>> POURING_WINE = ENTITY.register("pouring_wine", () ->
            EntityType.Builder.of(PouringWineEntity::new, MobCategory.MISC).sized(0.05f, 0.05f).build("sip_moment:pouring_wine"));

    public static final Supplier<EntityType<BarChairEntity>> BAR_CHAIR = ENTITY.register("bar_chair", () ->
            EntityType.Builder.of(BarChairEntity::new, MobCategory.MISC).sized(0.05f, 0.05f).build("sip_moment:bar_chair"));

    public static final Supplier<EntityType<ThrownSpearLonginus>> SPEAR_LONGINUS = ENTITY.register(
            "spear_longinus",
            () -> EntityType.Builder.of(
                            (EntityType<ThrownSpearLonginus> type, Level level) -> new ThrownSpearLonginus(type, level),
                            MobCategory.MISC
                    )

                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("sip_moment:spear_longinus")
    );

    public static final Supplier<EntityType<AfterimageEntity>> AFTERIMAGE = ENTITY.register("afterimage", () ->
            EntityType.Builder.of(AfterimageEntity::new, MobCategory.MISC)
                    .sized(0.6F, 1.95F)
                    .eyeHeight(1.74F)
                    .noSave().fireImmune().build("sip_moment:afterimage"));

    @EventBusSubscriber(modid = SipMomentMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void init(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> EntityRenderers.register(POURING_WINE.get(), PouringWineEntity.PouringWineRender::new));
            event.enqueueWork(() -> EntityRenderers.register(BAR_CHAIR.get(), BarChairEntity.BarChairEntityRender::new));
            event.enqueueWork(() -> EntityRenderers.register(SPEAR_LONGINUS.get(), SpearLonginusRender::new));
            event.enqueueWork(() -> EntityRenderers.register(AFTERIMAGE.get(), AfterimageRender::new));
        }
    }

}
