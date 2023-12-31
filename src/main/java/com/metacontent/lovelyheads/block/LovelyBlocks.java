package com.metacontent.lovelyheads.block;

import com.metacontent.lovelyheads.LovelyHeads;
import com.metacontent.lovelyheads.block.custom.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.ToIntFunction;

public class LovelyBlocks {
    public static final Block PLAYER_TELEPORT_BLOCK = registerBlock("player_teleport_block",
            new PlayerTeleportBlock(FabricBlockSettings.copyOf(Blocks.STONE)));

    public static final Block HEAD_PEDESTAL_BLOCK = registerBlock("head_pedestal_block",
            new HeadPedestalBlock(FabricBlockSettings.copyOf(Blocks.STONE).nonOpaque()));

    public static final Block POLYMORPH_HEAD_BLOCK = registerBlock("polymorph_head_block",
            new PolymorphHeadBlock(FabricBlockSettings.copyOf(Blocks.SKELETON_SKULL).nonOpaque()));

    public static final Block ITEM_TRANSMITTER_BLOCK = registerBlock("item_transmitter_block",
            new ItemTransmitterBlock(FabricBlockSettings.copyOf(Blocks.STONE)));

    public static final Block HEAD_CONSTRUCTOR_BLOCK = registerBlock("head_constructor_block",
            new HeadConstructorBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)));

    public static final Block ACACIA_TROPHY_PLAQUE_BLOCK = registerBlock("acacia_trophy_plaque_block",
            new TrophyPlaqueBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).nonOpaque()));

    public static final Block OAK_TROPHY_PLAQUE_BLOCK = registerBlock("oak_trophy_plaque_block",
            new TrophyPlaqueBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).nonOpaque()));

    public static final Block SPRUCE_TROPHY_PLAQUE_BLOCK = registerBlock("spruce_trophy_plaque_block",
            new TrophyPlaqueBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).nonOpaque()));

    public static final Block BIRCH_TROPHY_PLAQUE_BLOCK = registerBlock("birch_trophy_plaque_block",
            new TrophyPlaqueBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).nonOpaque()));

    public static final Block JUNGLE_TROPHY_PLAQUE_BLOCK = registerBlock("jungle_trophy_plaque_block",
            new TrophyPlaqueBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).nonOpaque()));

    public static final Block DARK_OAK_TROPHY_PLAQUE_BLOCK = registerBlock("dark_oak_trophy_plaque_block",
            new TrophyPlaqueBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).nonOpaque()));

    public static final Block MANGROVE_TROPHY_PLAQUE_BLOCK = registerBlock("mangrove_trophy_plaque_block",
            new TrophyPlaqueBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).nonOpaque()));

    public static final Block CHERRY_TROPHY_PLAQUE_BLOCK = registerBlock("cherry_trophy_plaque_block",
            new TrophyPlaqueBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).nonOpaque()));

    public static final Block CRIMSON_TROPHY_PLAQUE_BLOCK = registerBlock("crimson_trophy_plaque_block",
            new TrophyPlaqueBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).nonOpaque()));

    public static final Block WARPED_TROPHY_PLAQUE_BLOCK = registerBlock("warped_trophy_plaque_block",
            new TrophyPlaqueBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).nonOpaque()));

    public static final Block GOLDEN_TROPHY_PLAQUE_BLOCK = registerBlock("golden_trophy_plaque_block",
            new TrophyPlaqueBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).nonOpaque().luminance(value -> {
                if (value.get(TrophyPlaqueBlock.HAS_HEAD)) {
                    return 4;
                }
                return 0;
            })));

    public static final Block MOB_LOCATOR_BLOCK = registerBlock("mob_locator_block",
            new MobLocatorBlock(FabricBlockSettings.copyOf(Blocks.STONE)));

    public static final Block STACKED_SKULLS_BLOCK = registerBlock("stacked_skulls_block",
            new StackedSkullsBlock(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK)));

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(LovelyHeads.ID, name), block);
    }

    public static void registerLovelyBlocks() {
        LovelyHeads.LOGGER.debug("Registering blocks for " + LovelyHeads.ID);
    }
}
