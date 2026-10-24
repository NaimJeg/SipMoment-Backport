package com.renyigesai.sip_moment.common.network;

import com.mojang.serialization.Codec;
import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.data.WineListIngredient;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public record SyncWineListPayload(Map<ResourceLocation, WineListIngredient> wineMap) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncWineListPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "sync_wine_list"));
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<ResourceLocation, WineListIngredient>> MAP_STREAM_CODEC =
            StreamCodec.of(
                    (buf, map) -> {
                        buf.writeVarInt(map.size());
                        for (var entry : map.entrySet()) {
                            ResourceLocation.STREAM_CODEC.encode(buf, entry.getKey());
                            WineListIngredient.STREAM_CODEC.encode(buf, entry.getValue());
                        }
                    },
                    buf -> {
                        int size = buf.readVarInt();
                        Map<ResourceLocation, WineListIngredient> map = new HashMap<>(size);
                        for (int i = 0; i < size; i++) {
                            ResourceLocation id = ResourceLocation.STREAM_CODEC.decode(buf);
                            WineListIngredient ingredient = WineListIngredient.STREAM_CODEC.decode(buf);
                            map.put(id, ingredient);
                        }
                        return map;
                    }
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncWineListPayload> STREAM_CODEC =
            StreamCodec.composite(
                    MAP_STREAM_CODEC,
                    SyncWineListPayload::wineMap,
                    SyncWineListPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}