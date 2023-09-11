package com.metacontent.lovelyheads.client;

import com.metacontent.lovelyheads.block.LovelyBlocks;
import com.metacontent.lovelyheads.block.entity.LovelyBlockEntities;
import com.metacontent.lovelyheads.block.renderer.TrophyPlaqueBlockEntityRenderer;
import com.metacontent.lovelyheads.screen.HeadConstructorScreen;
import com.metacontent.lovelyheads.screen.ItemTransmitterScreen;
import com.metacontent.lovelyheads.screen.LovelyScreens;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class LovelyHeadsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(LovelyScreens.ITEM_TRANSMITTER_SCREEN_HANDLER, ItemTransmitterScreen::new);
        HandledScreens.register(LovelyScreens.HEAD_CONSTRUCTOR_SCREEN_HANDLER, HeadConstructorScreen::new);

        BlockEntityRendererFactories.register(LovelyBlockEntities.TROPHY_PLAQUE_BLOCK_ENTITY,
                TrophyPlaqueBlockEntityRenderer::new);
    }
}
