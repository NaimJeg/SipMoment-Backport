package com.renyigesai.sip_moment.common.event;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.data.WineListCatalog;
import com.renyigesai.sip_moment.common.entitys.AfterimageEntity;
import com.renyigesai.sip_moment.common.entitys.ITimeScaledEntity;
import com.renyigesai.sip_moment.common.init.SMEntityTypes;
import com.renyigesai.sip_moment.common.network.SyncWineListPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = SipMomentMod.MODID)
public class SMEvents {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new SyncWineListPayload(WineListCatalog.wineListMap));
        }
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        // Advance leases in real world ticks, even when an entity's own tick is skipped.
        for (Entity entity : level.getAllEntities()) {
            if (!(entity instanceof ITimeScaledEntity scaled)) continue;
            int lease = scaled.getTimeStopLease();
            if (lease > 0) {
                scaled.setTimeStopLease(lease - 1);
                if (lease == 1) scaled.setTimeScale(1.0F);
            }
        }
    }

    @EventBusSubscriber(modid = SipMomentMod.MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void onAttributeCreation(EntityAttributeCreationEvent event) {
            event.put(SMEntityTypes.AFTERIMAGE.get(), AfterimageEntity.createAttributes().build());
        }
    }
}
