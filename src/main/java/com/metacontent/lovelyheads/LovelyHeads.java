package com.metacontent.lovelyheads;

import com.metacontent.lovelyheads.block.LovelyBlocks;
import com.metacontent.lovelyheads.block.entity.LovelyBlockEntities;
import com.metacontent.lovelyheads.command.LovelyCommands;
import com.metacontent.lovelyheads.item.LovelyItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LovelyHeads implements ModInitializer {
    public static final String ID = "lovelyheads";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitialize() {
        LovelyItems.registerLovelyItems();
        LovelyBlocks.registerLovelyBlocks();
        LovelyCommands.registerLovelyCommands();
        LovelyBlockEntities.registerLovelyBlockEntities();
    }
}
