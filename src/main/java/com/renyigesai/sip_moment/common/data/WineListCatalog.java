package com.renyigesai.sip_moment.common.data;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.event.WineListInitEvent;
import com.renyigesai.sip_moment.common.init.SMItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.*;

@EventBusSubscriber(modid = SipMomentMod.MODID)
public class WineListCatalog {
    public static Map<ResourceLocation,WineListIngredient> wineListMap = new HashMap<>();

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {

        Map<ResourceLocation, WineListIngredient> wineListIngredients = new HashMap<>();
        wineListIngredients.put(ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "kyoho_wine_and_goblet"), new WineListIngredient(WineListIngredient.toArr(new ItemStack(Items.EMERALD, 16)), WineListIngredient.toArr(new ItemStack(SMItems.KYOHO_WINE.get()), new ItemStack(SMItems.GOBLET.get()))));
        wineListIngredients.put(ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "champagne_and_champagne_glass"), new WineListIngredient(WineListIngredient.toArr(new ItemStack(Items.EMERALD, 16)), WineListIngredient.toArr(new ItemStack(SMItems.CHAMPAGNE.get()), new ItemStack(SMItems.CHAMPAGNE_GLASS.get()))));
        wineListIngredients.put(ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "highball"), new WineListIngredient(WineListIngredient.toArr(new ItemStack(Items.EMERALD, 6)), WineListIngredient.toArr(new ItemStack(SMItems.HIGHBALL.get()))));
        wineListIngredients.put(ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "whisky"), new WineListIngredient(WineListIngredient.toArr(new ItemStack(Items.EMERALD, 6)), WineListIngredient.toArr(new ItemStack(SMItems.WHISKY.get()))));
        wineListIngredients.put(ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "david_martinez"), new WineListIngredient(WineListIngredient.toArr(new ItemStack(Items.EMERALD, 8)), WineListIngredient.toArr(new ItemStack(SMItems.DAVID_MARTINEZ.get()))));
        wineListIngredients.put(ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "butter_beer"), new WineListIngredient(WineListIngredient.toArr(new ItemStack(Items.EMERALD, 10)), WineListIngredient.toArr(new ItemStack(SMItems.BUTTER_BEER.get()))));
        wineListIngredients.put(ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "black_star_coffee"), new WineListIngredient(WineListIngredient.toArr(new ItemStack(Items.EMERALD, 6)), WineListIngredient.toArr(new ItemStack(SMItems.BLACK_STAR_COFFEE.get()))));
        wineListIngredients.put(ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "orange_juice"), new WineListIngredient(WineListIngredient.toArr(new ItemStack(Items.EMERALD, 5)), WineListIngredient.toArr(new ItemStack(SMItems.ORANGE_JUICE.get()))));
        wineListIngredients.put(ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "music_disc_dust_to_dust"), new WineListIngredient(WineListIngredient.toArr(new ItemStack(Items.EMERALD, 36)), WineListIngredient.toArr(new ItemStack(SMItems.MUSIC_DISC_DUST_TO_DUST.get()))));

        WineListInitEvent wineListInitEvent = new WineListInitEvent(wineListIngredients);
        NeoForge.EVENT_BUS.post(wineListInitEvent);

        WineListCatalog.wineListMap.clear();
        WineListCatalog.wineListMap.putAll(wineListInitEvent.getWineListMap());

    }

    public static List<WineListIngredient> getWineList(){
        return wineListMap.values().stream().toList();
    }

    public static List<WineListIngredient> getWineList(boolean sorted){
        return wineListMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(ResourceLocation::getPath)))
                .map(Map.Entry::getValue)
                .toList();
    }
}
