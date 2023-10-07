package com.metacontent.lovelyheads.item;

import com.metacontent.lovelyheads.LovelyHeads;
import com.metacontent.lovelyheads.block.LovelyBlocks;
import com.metacontent.lovelyheads.block.custom.PlayerTeleportBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LovelyItems {
    public static final Item POLYMORPH_HEAD_ITEM = registerItem("polymorph_head_block",
            new BlockItem(LovelyBlocks.POLYMORPH_HEAD_BLOCK, new FabricItemSettings().rarity(Rarity.UNCOMMON)) {
                @Override
                public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                    tooltip.add(Text.translatable("item.lovelyheads.polymorph_head_block.tooltip"));
                    super.appendTooltip(stack, world, tooltip, context);
                }
            });

    public static final Item PLAYER_TELEPORT_BLOCK = registerItem("player_teleport_block",
            new BlockItem(LovelyBlocks.PLAYER_TELEPORT_BLOCK, new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC)) {
                @Override
                public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                    if (stack.hasNbt()) {
                        int timer = stack.getNbt().getInt("timer");
                        if (timer >= PlayerTeleportBlock.CD) {
                            tooltip.add(Text.translatable("item.lovelyheads.player_teleport_block.ready_tooltip"));
                        }
                        else {
                            int duration = 6000 - timer;
                            int minutes = duration / 1200;
                            int seconds = duration / 20 - minutes * 60;
                            tooltip.add(Text.translatable("item.lovelyheads.player_teleport_block.cooldown_tooltip",
                                    minutes + ":" + (seconds / 10) + (seconds % 10)));
                        }
                    }
                }

                @Override
                public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
                    if (!stack.hasNbt()) {
                        NbtCompound nbt = new NbtCompound();
                        nbt.putInt("timer", PlayerTeleportBlock.CD);
                        stack.setNbt(nbt);
                    }
                }

                @Override
                public boolean hasGlint(ItemStack stack) {
                    return stack.hasNbt() ? stack.getNbt().getInt("timer") >= PlayerTeleportBlock.CD : false;
                }
            });

    public static final Item HEAD_PEDESTAL_BLOCK = registerItem("head_pedestal_block",
            new BlockItem(LovelyBlocks.HEAD_PEDESTAL_BLOCK, new FabricItemSettings()));

    public static final Item ITEM_TRANSMITTER_BLOCK = registerItem("item_transmitter_block",
            new BlockItem(LovelyBlocks.ITEM_TRANSMITTER_BLOCK, new FabricItemSettings().rarity(Rarity.RARE)));

    public static final Item HEAD_CONSTRUCTOR_BLOCK = registerItem("head_constructor_block",
            new BlockItem(LovelyBlocks.HEAD_CONSTRUCTOR_BLOCK, new FabricItemSettings()));

    public static final Item MOB_LOCATOR_BLOCK = registerItem("mob_locator_block",
            new BlockItem(LovelyBlocks.MOB_LOCATOR_BLOCK, new FabricItemSettings()));

    public static final Item STACKED_SKULLS_BLOCK = registerItem("stacked_skulls_block",
            new BlockItem(LovelyBlocks.STACKED_SKULLS_BLOCK, new FabricItemSettings()));

    public static final Item HEAD_SCHEME_ITEM = registerItem("head_scheme_item",
            new Item(new FabricItemSettings().maxCount(1)) {
                @Override
                public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                    if (stack.hasNbt()) {
                        String owner = "";
                        NbtCompound nbt = stack.getNbt();
                        if (nbt.contains("SkullOwner", 10)) {
                            owner = NbtHelper.toGameProfile(nbt.getCompound("SkullOwner")).getName();
                        }
                        else if (nbt.contains("SkullOwner", 8) && !Util.isBlank(nbt.getString("SkullOwner"))) {
                            owner = nbt.getString("SkullOwner");
                        }
                        tooltip.add(Text.literal(owner).setStyle(Style.EMPTY.withItalic(true)));
                    }
                }
            });

    public static final Item HEAD_BASE_ITEM = registerItem("head_base_item",
            new Item(new FabricItemSettings()));

    public static final Item ACACIA_TROPHY_PLAQUE_BLOCK = registerItem("acacia_trophy_plaque_block",
            new BlockItem(LovelyBlocks.ACACIA_TROPHY_PLAQUE_BLOCK, new FabricItemSettings()));

    public static final Item OAK_TROPHY_PLAQUE_BLOCK = registerItem("oak_trophy_plaque_block",
            new BlockItem(LovelyBlocks.OAK_TROPHY_PLAQUE_BLOCK, new FabricItemSettings()));

    public static final Item SPRUCE_TROPHY_PLAQUE_BLOCK = registerItem("spruce_trophy_plaque_block",
            new BlockItem(LovelyBlocks.SPRUCE_TROPHY_PLAQUE_BLOCK, new FabricItemSettings()));

    public static final Item BIRCH_TROPHY_PLAQUE_BLOCK = registerItem("birch_trophy_plaque_block",
            new BlockItem(LovelyBlocks.BIRCH_TROPHY_PLAQUE_BLOCK, new FabricItemSettings()));

    public static final Item JUNGLE_TROPHY_PLAQUE_BLOCK = registerItem("jungle_trophy_plaque_block",
            new BlockItem(LovelyBlocks.JUNGLE_TROPHY_PLAQUE_BLOCK, new FabricItemSettings()));

    public static final Item DARK_OAK_TROPHY_PLAQUE_BLOCK = registerItem("dark_oak_trophy_plaque_block",
            new BlockItem(LovelyBlocks.DARK_OAK_TROPHY_PLAQUE_BLOCK, new FabricItemSettings()));

    public static final Item MANGROVE_TROPHY_PLAQUE_BLOCK = registerItem("mangrove_trophy_plaque_block",
            new BlockItem(LovelyBlocks.MANGROVE_TROPHY_PLAQUE_BLOCK, new FabricItemSettings()));

    public static final Item CHERRY_TROPHY_PLAQUE_BLOCK = registerItem("cherry_trophy_plaque_block",
            new BlockItem(LovelyBlocks.CHERRY_TROPHY_PLAQUE_BLOCK, new FabricItemSettings()));

    public static final Item CRIMSON_TROPHY_PLAQUE_BLOCK = registerItem("crimson_trophy_plaque_block",
            new BlockItem(LovelyBlocks.CRIMSON_TROPHY_PLAQUE_BLOCK, new FabricItemSettings()));

    public static final Item WARPED_TROPHY_PLAQUE_BLOCK = registerItem("warped_trophy_plaque_block",
            new BlockItem(LovelyBlocks.WARPED_TROPHY_PLAQUE_BLOCK, new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(LovelyHeads.ID, name), item);
    }

    public static void registerLovelyItems() {
        LovelyHeads.LOGGER.debug("Registering items for " + LovelyHeads.ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
            content.addAfter(Items.LODESTONE, PLAYER_TELEPORT_BLOCK);
            content.addAfter(PLAYER_TELEPORT_BLOCK, ITEM_TRANSMITTER_BLOCK);
            content.addAfter(ITEM_TRANSMITTER_BLOCK, MOB_LOCATOR_BLOCK);
            content.addAfter(MOB_LOCATOR_BLOCK,HEAD_PEDESTAL_BLOCK);
            content.addAfter(HEAD_PEDESTAL_BLOCK, HEAD_CONSTRUCTOR_BLOCK);
            content.addAfter(HEAD_CONSTRUCTOR_BLOCK, ACACIA_TROPHY_PLAQUE_BLOCK);
            content.addAfter(ACACIA_TROPHY_PLAQUE_BLOCK, OAK_TROPHY_PLAQUE_BLOCK);
            content.addAfter(OAK_TROPHY_PLAQUE_BLOCK, SPRUCE_TROPHY_PLAQUE_BLOCK);
            content.addAfter(SPRUCE_TROPHY_PLAQUE_BLOCK, BIRCH_TROPHY_PLAQUE_BLOCK);
            content.addAfter(BIRCH_TROPHY_PLAQUE_BLOCK, JUNGLE_TROPHY_PLAQUE_BLOCK);
            content.addAfter(JUNGLE_TROPHY_PLAQUE_BLOCK, DARK_OAK_TROPHY_PLAQUE_BLOCK);
            content.addAfter(DARK_OAK_TROPHY_PLAQUE_BLOCK, MANGROVE_TROPHY_PLAQUE_BLOCK);
            content.addAfter(MANGROVE_TROPHY_PLAQUE_BLOCK, CHERRY_TROPHY_PLAQUE_BLOCK);
            content.addAfter(CHERRY_TROPHY_PLAQUE_BLOCK, CRIMSON_TROPHY_PLAQUE_BLOCK);
            content.addAfter(CRIMSON_TROPHY_PLAQUE_BLOCK, WARPED_TROPHY_PLAQUE_BLOCK);
            content.addAfter(Items.PLAYER_HEAD, POLYMORPH_HEAD_ITEM);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.addAfter(Items.PHANTOM_MEMBRANE, HEAD_BASE_ITEM);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(content -> {
            content.add(STACKED_SKULLS_BLOCK);
        });
    }
}
