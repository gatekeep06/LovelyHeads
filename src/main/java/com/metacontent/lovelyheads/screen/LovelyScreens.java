package com.metacontent.lovelyheads.screen;

import com.metacontent.lovelyheads.LovelyHeads;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class LovelyScreens {
    public static final ScreenHandlerType<ItemTransmitterScreenHandler> ITEM_TRANSMITTER_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerSimple(new Identifier(LovelyHeads.ID, "item_transmitter_block"), ItemTransmitterScreenHandler::new);

    public void registerLovelyScreens() {

    }
}
