package com.metacontent.lovelyheads.screen;

import com.metacontent.lovelyheads.LovelyHeads;
import com.metacontent.lovelyheads.block.custom.ItemTransmitterBlock;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemTransmitterScreen extends HandledScreen<ItemTransmitterScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(LovelyHeads.ID, "textures/gui/item_transmitter_gui.png");

    public ItemTransmitterScreen(ItemTransmitterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
        int duration = ItemTransmitterBlock.TRANSMISSION_TIME - handler.getSyncedInt();
        int minutes = duration / 1200;
        int seconds = duration / 20 - minutes * 60;
        context.drawText(textRenderer, Text.translatable("block.lovelyheads.item_transmitter_block.transmission_info", minutes + ":" + (seconds / 10) + (seconds % 10)),
                width / 2 - 80, height / 2 - 44, 0x3f3f3f, false);
    }
}
