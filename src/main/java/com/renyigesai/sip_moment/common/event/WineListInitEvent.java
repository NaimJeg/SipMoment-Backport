package com.renyigesai.sip_moment.common.event;

import com.renyigesai.sip_moment.common.data.WineListIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WineListInitEvent extends Event {

    private Map<ResourceLocation,WineListIngredient> wineListMap;

    public WineListInitEvent(Map<ResourceLocation,WineListIngredient> wineListMap) {
        this.wineListMap = wineListMap;
    }

    public void addWineList(ResourceLocation id,WineListIngredient wineListIngredient){
        this.wineListMap.put(id,wineListIngredient);
    }

    public void removeWineList(ResourceLocation id){
        if (this.wineListMap.get(id) != null){
            this.wineListMap.remove(id);
        }
    }

    public void removeAll(){
        this.wineListMap.clear();
    }

    public WineListIngredient getWineListIngredient(ResourceLocation id){
        return wineListMap.get(id);
    }

    public List<WineListIngredient> getWineList() {
        return wineListMap.values().stream().toList();
    }

    public Map<ResourceLocation, WineListIngredient> getWineListMap() {
        return wineListMap;
    }
}
