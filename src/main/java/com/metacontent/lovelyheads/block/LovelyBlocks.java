package com.metacontent.lovelyheads.block;

import com.metacontent.lovelyheads.LovelyHeads;
import com.metacontent.lovelyheads.block.custom.HeadPedestalBlock;
import com.metacontent.lovelyheads.block.custom.PlayerTeleportBlock;
import com.metacontent.lovelyheads.block.custom.PolymorphHeadBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LovelyBlocks {
    public static final Block PLAYER_TELEPORT_BLOCK = registerBlock("player_teleport_block",
            new PlayerTeleportBlock(FabricBlockSettings.copyOf(Blocks.STONE)));

    public static final Block HEAD_PEDESTAL_BLOCK = registerBlock("head_pedestal_block",
            new HeadPedestalBlock(FabricBlockSettings.copyOf(Blocks.STONE).nonOpaque()));

    public static final Block POLYMORPH_HEAD_BLOCK = registerBlock("polymorph_head_block",
            new PolymorphHeadBlock(FabricBlockSettings.copyOf(Blocks.SKELETON_SKULL)));

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(LovelyHeads.ID, name), block);
    }

    public static void registerLovelyBlocks() {
        LovelyHeads.LOGGER.debug("Registering blocks for " + LovelyHeads.ID);
    }
}
