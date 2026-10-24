package com.renyigesai.sip_moment.common.client.particles;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.renyigesai.sip_moment.common.init.SMParticleTypes;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

public class DrunkParticleOptions extends ScalableParticleOptionsBase {

    public static final MapCodec<DrunkParticleOptions> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                             SCALE.fieldOf("scale").forGetter(ScalableParticleOptionsBase::getScale)
                    )
                    .apply(i, DrunkParticleOptions::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, DrunkParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale, DrunkParticleOptions::new
    );

    public DrunkParticleOptions(float scale) {
        super(scale);
    }


    @Override
    public ParticleType<?> getType() {
        return SMParticleTypes.DRUNK.get();
    }
}
