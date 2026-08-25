package com.renyigesai.sip_moment.common.client.gui;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.data.WineListCatalog;
import com.renyigesai.sip_moment.common.network.SyncGivesPayload;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.awt.*;

@SuppressWarnings("removal")
public class WineListScreen extends AbstractContainerScreen<WineListMenu> {
    public final Player player;
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(SipMomentMod.MODID, "textures/gui/wine_list.png");
    private static final int[] LINE = new int[]{59,91,123,155};
    public WineListScreen(WineListMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
        this.player = p_97742_.player;
        this.titleLabelY += 16;
        this.titleLabelX -= (width - 139) / 2;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int x = (width - 139) / 2;
        int y = (height - 187) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED,TEXTURE, x, y, 0, 0, 139, 187,256,256);
        int page = menu.getPage();
        int start = page * menu.itemsPerPage;
        int end = Math.min(start + menu.itemsPerPage, WineListCatalog.getWineList().size());
        for (int i = start, row = 0; i < end; i++, row++) {
            graphics.blit(RenderPipelines.GUI_TEXTURED,TEXTURE,x + 117,y + LINE[row],0,187,10,10,256,256);
        }

        int totalItems = WineListCatalog.getWineList().size();
        int totalPages = Math.max(1, (totalItems + menu.itemsPerPage - 1) / menu.itemsPerPage);
        int currentDisplayPage = page + 1; // 转为1基显示
        Component pageText = Component.literal(currentDisplayPage + "/" + totalPages);
        int textX = x + 110;
        int textY = y + 171;
        graphics.text(this.font, pageText, textX, textY, -12566464, false);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
        graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0) {
            int guiX = (width - 139) / 2;
            int guiY = (height - 187) / 2;
            int page = menu.getPage();
            int start = page * menu.itemsPerPage;
            int end = Math.min(start + menu.itemsPerPage, WineListCatalog.getWineList().size());
            double mouseX = event.x();
            double mouseY = event.y();

            for (int i = start, row = 0; i < end; i++, row++) {
                int buttonX = guiX + 117;
                int buttonY = guiY + LINE[row];
                int buttonWidth = 10;
                int buttonHeight = 10;
                if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
                    ClientPacketDistributor.sendToServer(new SyncGivesPayload(i));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.0f));
                    return true;
                }
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double scrollX, double scrollY) {
        int x = this.leftPos;
        int y = this.topPos;
        if (pMouseX >= x && pMouseX <= x + 104 && pMouseY >= y + 26 && pMouseY <= y + 195) {
            int totalItems = WineListCatalog.getWineList().size();
            int itemsPerPage = menu.itemsPerPage;
            int totalPages = Math.max(1, (totalItems + itemsPerPage - 1) / itemsPerPage);

            int currentPage = menu.getPage();
            int newPage;
            if (scrollY > 0) {
                newPage = Math.max(currentPage - 1, 0);
            } else {
                newPage = Math.min(currentPage + 1, totalPages - 1);
            }

            if (newPage != currentPage) {
                menu.rebuildSlots(newPage);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_HAT, 2.0F));
            }

        }
        return super.mouseScrolled(pMouseX, pMouseY, scrollX, scrollY);
    }
}
