package com.renyigesai.sip_moment.common.init;

import com.renyigesai.sip_moment.SipMomentMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class SMCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SipMomentMod.MODID);
    public static final Supplier<CreativeModeTab> SIP_MOMENT_TAB = REGISTER.register(
            "0_sip_moment_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.sip_moment.main_tab"))
                    .icon(() -> SMItems.WHISKY.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(SMItems.HIGHBALL);
                        output.accept(SMItems.BUTTER_BEER);
                        output.accept(SMItems.WHISKY);
                        output.accept(SMItems.DAVID_MARTINEZ);
                        output.accept(SMItems.KYOHO_WINE_CUP);
                        output.accept(SMItems.SHIPWRECK_VINTAGE);
                        output.accept(SMItems.CHAMPAGNE_CUP);
                        output.accept(SMItems.BLACK_STAR_COFFEE);
                        output.accept(SMItems.ORANGE_JUICE);
                        output.accept(SMItems.KYOHO_WINE);
                        output.accept(SMItems.CHAMPAGNE);
                        output.accept(SMItems.GOBLET);
                        output.accept(SMItems.CHAMPAGNE_GLASS);
                        output.accept(SMItems.BAR_TABLE);
                        output.accept(SMItems.BAR_CHAIR);

                        output.accept(SMItems.MUSIC_DISC_DUST_TO_DUST);
                    }
                    ).build());

}
