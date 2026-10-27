package com.renyigesai.sip_moment;

import com.renyigesai.sip_moment.common.init.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SipMomentMod.MODID)
public class SipMomentMod {
    public static final String MODID = "sip_moment";
    public static final Logger LOGGER = LogUtils.getLogger();
    public SipMomentMod(IEventBus modEventBus) {
        SMBlocks.BLOCKS.register(modEventBus);
        SMBlocks.Entitys.REGISTER.register(modEventBus);
        SMDataComponents.DATA_COMPONENT_TYPE.register(modEventBus);
        SMItems.ITEMS.register(modEventBus);
        SMCreativeModeTabs.REGISTER.register(modEventBus);
        SMEntityTypes.ENTITY.register(modEventBus);
        SMMenuType.REGISTRY.register(modEventBus);
        SMAttachments.ATTACHMENT_TYPES.register(modEventBus);
        SMMobEffects.EFFECTS.register(modEventBus);
        SMParticleTypes.PARTICLE_TYPE.register(modEventBus);
        SMSounds.REGISTRY.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }
}
