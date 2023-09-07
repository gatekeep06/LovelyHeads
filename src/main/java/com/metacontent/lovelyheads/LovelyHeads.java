package com.metacontent.lovelyheads;

import com.metacontent.lovelyheads.block.LovelyBlocks;
import com.metacontent.lovelyheads.block.entity.LovelyBlockEntities;
import com.metacontent.lovelyheads.command.LovelyCommands;
import com.metacontent.lovelyheads.item.LovelyItems;
import com.metacontent.lovelyheads.recipe.HeadConstructorRecipe;
import com.metacontent.lovelyheads.screen.LovelyScreens;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LovelyHeads implements ModInitializer {
    public static final String ID = "lovelyheads";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitialize() {
        LovelyItems.registerLovelyItems();
        LovelyBlocks.registerLovelyBlocks();
        LovelyScreens.registerLovelyScreens();
        LovelyCommands.registerLovelyCommands();
        LovelyBlockEntities.registerLovelyBlockEntities();

        Registry.register(Registries.RECIPE_SERIALIZER, HeadConstructorRecipe.Serializer.ID, HeadConstructorRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, new Identifier(ID, HeadConstructorRecipe.Type.ID), HeadConstructorRecipe.Type.INSTANCE);
    }
}
