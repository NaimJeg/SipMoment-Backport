package com.renyigesai.sip_moment.common.network;

import com.renyigesai.sip_moment.SipMomentMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncGivesPayload(int row) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncGivesPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SipMomentMod.MODID, "sync_gives"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncGivesPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    SyncGivesPayload::row,
                    SyncGivesPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
