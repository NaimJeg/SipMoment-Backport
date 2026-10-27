package com.renyigesai.sip_moment.common.client.overlay;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.init.SMMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = SipMomentMod.MODID, value = Dist.CLIENT)
public class EdgerunnersOverlay {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;
        if (!(mc.player.hasEffect(SMMobEffects.EDGERUNNERS))) return;

        GuiGraphics gui = event.getGuiGraphics();
        int w = gui.guiWidth();
        int h = gui.guiHeight();
        int deepBlue = 0x87CEEB;
        int edge = Math.min(w, h) / 4;
        int maxAlpha = 0x48;

        gui.fill(0, 0, w, h, (0x10 << 24) | deepBlue);
        for (int i = 0; i < edge; i++) {
            float t = (float) i / edge;
            int alpha = (int) (maxAlpha * (1.0f - t));
            int color = (alpha << 24) | deepBlue;
            gui.fill(i, i, w - i, i + 1, color);
            gui.fill(i, h - i - 1, w - i, h - i, color);
            gui.fill(i, i, i + 1, h - i, color);
            gui.fill(w - i - 1, i, w - i, h - i, color);
        }
    }
}