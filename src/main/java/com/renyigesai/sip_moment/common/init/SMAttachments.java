package com.renyigesai.sip_moment.common.init;

import com.mojang.serialization.Codec;
import com.renyigesai.sip_moment.SipMomentMod;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class SMAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SipMomentMod.MODID);

    public static final Supplier<AttachmentType<Integer>> CAPACITY_FOR_LIQUOR = ATTACHMENT_TYPES.register(
            "capacity_for_liquor", () -> AttachmentType.builder(() -> -1)
                    .serialize(Codec.INT)
                    .sync(ByteBufCodecs.VAR_INT)
                    .build()
    );

    public static final Supplier<AttachmentType<Integer>> WINE_LIST_PAGE = ATTACHMENT_TYPES.register(
            "wine_list_page", () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT)
                    .sync(ByteBufCodecs.VAR_INT)
                    .build()
    );

    public static final Supplier<AttachmentType<Float>> SLOW_RATE =
            ATTACHMENT_TYPES.register("slow_rate", () ->
                    AttachmentType.builder(() -> 1.0f)
                            .sync(ByteBufCodecs.FLOAT) // 启用同步
                            .build()
            );

}
