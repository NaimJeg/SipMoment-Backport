package com.renyigesai.sip_moment.common.network;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.data.WineListIngredient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncPagePayload(int page) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncPagePayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "sync_page"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPagePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    SyncPagePayload::page,
                    SyncPagePayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
