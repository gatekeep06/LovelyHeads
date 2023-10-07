package com.metacontent.lovelyheads.block.entity;

import com.metacontent.lovelyheads.LovelyHeads;
import com.metacontent.lovelyheads.block.LovelyBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LovelyBlockEntities {
    public static final BlockEntityType<PlayerTeleportBlockEntity> PLAYER_TELEPORT_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, new Identifier(LovelyHeads.ID, "player_teleport_block_entity"),
            FabricBlockEntityTypeBuilder.create(PlayerTeleportBlockEntity::new, LovelyBlocks.PLAYER_TELEPORT_BLOCK).build()
    );

    public static final BlockEntityType<HeadPedestalBlockEntity> HEAD_PEDESTAL_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, new Identifier(LovelyHeads.ID, "head_pedestal_block_entity"),
            FabricBlockEntityTypeBuilder.create(HeadPedestalBlockEntity::new, LovelyBlocks.HEAD_PEDESTAL_BLOCK).build()
    );

    public static final BlockEntityType<ItemTransmitterBlockEntity> ITEM_TRANSMITTER_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, new Identifier(LovelyHeads.ID, "item_transmitter_block_entity"),
            FabricBlockEntityTypeBuilder.create(ItemTransmitterBlockEntity::new, LovelyBlocks.ITEM_TRANSMITTER_BLOCK).build()
    );

    public static final BlockEntityType<HeadConstructorBlockEntity> HEAD_CONSTRUCTOR_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, new Identifier(LovelyHeads.ID, "head_constructor_block_entity"),
            FabricBlockEntityTypeBuilder.create(HeadConstructorBlockEntity::new, LovelyBlocks.HEAD_CONSTRUCTOR_BLOCK).build()
    );

    public static final BlockEntityType<TrophyPlaqueBlockEntity> TROPHY_PLAQUE_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, new Identifier(LovelyHeads.ID, "trophy_plaque_block_entity"),
            FabricBlockEntityTypeBuilder.create(TrophyPlaqueBlockEntity::new,
                    LovelyBlocks.ACACIA_TROPHY_PLAQUE_BLOCK,
                    LovelyBlocks.OAK_TROPHY_PLAQUE_BLOCK,
                    LovelyBlocks.SPRUCE_TROPHY_PLAQUE_BLOCK,
                    LovelyBlocks.BIRCH_TROPHY_PLAQUE_BLOCK,
                    LovelyBlocks.JUNGLE_TROPHY_PLAQUE_BLOCK,
                    LovelyBlocks.DARK_OAK_TROPHY_PLAQUE_BLOCK,
                    LovelyBlocks.MANGROVE_TROPHY_PLAQUE_BLOCK,
                    LovelyBlocks.CHERRY_TROPHY_PLAQUE_BLOCK,
                    LovelyBlocks.CRIMSON_TROPHY_PLAQUE_BLOCK,
                    LovelyBlocks.WARPED_TROPHY_PLAQUE_BLOCK,
                    LovelyBlocks.GOLDEN_TROPHY_PLAQUE_BLOCK
            ).build()
    );

    public static void registerLovelyBlockEntities() {
        LovelyHeads.LOGGER.debug("Registering block entity types for " + LovelyHeads.ID);
    }
}
