package com.renyigesai.sip_moment.common.init;

import com.renyigesai.sip_moment.SipMomentMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.logging.Handler;


public class SMSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, SipMomentMod.MODID);

    private static DeferredHolder<SoundEvent,SoundEvent> registerSoundEvents(String name) {
        return REGISTRY.register(name,()-> SoundEvent.createFixedRangeEvent(Identifier.fromNamespaceAndPath(SipMomentMod.MODID,name),16F));
    }

    public static final DeferredHolder<SoundEvent,SoundEvent> POURING_WINE = registerSoundEvents("pouring_wine");
    public static final DeferredHolder<SoundEvent,SoundEvent> EDGERUNNERS = registerSoundEvents("edgerunners");

    public static final DeferredHolder<SoundEvent,SoundEvent> MUSIC_DISC_DUST_TO_DUST = registerSoundEvents("music_disc_dust_to_dust");

}
