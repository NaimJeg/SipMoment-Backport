package com.renyigesai.sip_moment.common.network;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.data.WineListCatalog;
import com.renyigesai.sip_moment.common.data.WineListIngredient;
import com.renyigesai.sip_moment.common.init.SMAttachments;
import com.renyigesai.sip_moment.common.utils.ItemUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = SipMomentMod.MODID)
public class NetworkHandler {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(SipMomentMod.MODID).versioned("1.0");

        // 只允许服务端 → 客户端
        registrar.playToClient(
                SyncWineListPayload.TYPE,
                SyncWineListPayload.STREAM_CODEC,
                (payload, context) -> {
                    // 客户端处理：必须在主线程更新列表
                    context.enqueueWork(() -> {
                        WineListCatalog.wineListMap.clear();
                        WineListCatalog.wineListMap.putAll(payload.wineMap());
                    });
                }
        );

        registrar.playToServer(
                SyncPagePayload.TYPE,
                SyncPagePayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        var player = context.player();
                        int wineListPage = player.getData(SMAttachments.WINE_LIST_PAGE);
                        int newWineListPage = Math.max(wineListPage + payload.page(), 0);
                        player.setData(SMAttachments.WINE_LIST_PAGE, newWineListPage);
                    });
                }
        );

        registrar.playToServer(
                SyncGivesPayload.TYPE,
                SyncGivesPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    var player = context.player();
                    WineListIngredient wineListIngredient = WineListCatalog.getWineList(true).get(payload.row());
                    if (wineListIngredient != null){
                        List<ItemStack> gives = wineListIngredient.getGives();
                        if (!hasItems(player,gives)){
                            return;
                        }
                        consumeItems(player, gives);
                        List<ItemStack> results = wineListIngredient.getResults();
                        results.forEach(item-> ItemUtils.givePlayerItem(player,item.copy()));
                    }
                })
        );

    }

    private static boolean hasItems(Player player, List<ItemStack> required) {
        for (ItemStack needed : required) {
            int remaining = needed.getCount();
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                // 只比较物品类型，不考虑NBT；如果需要严格匹配，可使用 ItemStack.isSameItemSameComponents
                if (ItemStack.isSameItemSameComponents(stack,needed)) {
                    remaining -= stack.getCount();
                    if (remaining <= 0) break;
                }
            }
            if (remaining > 0) return false;
        }
        return true;
    }

    private static void consumeItems(Player player, List<ItemStack> required) {
        for (ItemStack needed : required) {
            int toRemove = needed.getCount();
            for (int i = 0; i < player.getInventory().getContainerSize() && toRemove > 0; i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.is(needed.getItem())) {
                    int removeCount = Math.min(toRemove, stack.getCount());
                    stack.shrink(removeCount);
                    toRemove -= removeCount;
                }
            }
        }
    }
}