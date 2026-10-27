package com.renyigesai.sip_moment.common.event;

import com.renyigesai.sip_moment.common.data.WineListCatalog;
import com.renyigesai.sip_moment.common.entitys.AfterimageEntity;
import com.renyigesai.sip_moment.common.entitys.ITimeScaledEntity;
import com.renyigesai.sip_moment.common.init.SMEntityTypes;
import com.renyigesai.sip_moment.common.network.SyncWineListPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class SMEvents {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (WineListCatalog.wineListMap != null) {
                PacketDistributor.sendToPlayer(serverPlayer,
                        new SyncWineListPayload(WineListCatalog.wineListMap));
            }
        }
    }

    @SubscribeEvent
    public static void onEntityTickPre(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (entity instanceof Player) return;
        if (!(entity instanceof ITimeScaledEntity ts)) return;

        int lease = ts.getTimeStopLease();
        if (lease > 0) {
            ts.setTimeStopLease(lease - 1);
            if (lease - 1 == 0) {
                ts.setTimeScale(1.0f);   // 租约到期，恢复正常速度
            }
        }
    }

    @SubscribeEvent
    public static void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(SMEntityTypes.AFTERIMAGE.get(), AfterimageEntity.createAttributes().build());
    }
}
