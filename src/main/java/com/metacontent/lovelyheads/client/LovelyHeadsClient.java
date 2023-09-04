package com.metacontent.lovelyheads.client;

import com.metacontent.lovelyheads.screen.ItemTransmitterScreen;
import com.metacontent.lovelyheads.screen.LovelyScreens;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class LovelyHeadsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(LovelyScreens.ITEM_TRANSMITTER_SCREEN_HANDLER, ItemTransmitterScreen::new);
    }
}
