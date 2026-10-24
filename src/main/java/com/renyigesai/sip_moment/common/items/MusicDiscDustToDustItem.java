package com.renyigesai.sip_moment.common.items;

import com.renyigesai.sip_moment.SipMomentMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;

public class MusicDiscDustToDustItem extends Item {
    public MusicDiscDustToDustItem(ResourceLocation identifier) {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG,ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID,"dust_to_dust"))));
    }
}
