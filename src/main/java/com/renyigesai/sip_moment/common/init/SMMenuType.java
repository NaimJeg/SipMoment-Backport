package com.renyigesai.sip_moment.common.init;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.client.gui.WineListMenu;
import com.renyigesai.sip_moment.common.client.gui.WineListScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SMMenuType {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, SipMomentMod.MODID);

    public static final Supplier<MenuType<WineListMenu>> WINE_LIST_MENU = REGISTRY.register("wine_list_menu",
            () ->  IMenuTypeExtension.create(WineListMenu::create));

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(WINE_LIST_MENU.get(), WineListScreen::new);
    }
}
