package com.metacontent.lovelyheads.item;

import com.metacontent.lovelyheads.LovelyHeads;
import com.metacontent.lovelyheads.block.LovelyBlocks;
import com.metacontent.lovelyheads.block.custom.PlayerTeleportBlock;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LovelyItems {
    public static final Item POLYMORPH_HEAD_ITEM = registerItem("polymorph_head_block",
            new BlockItem(LovelyBlocks.POLYMORPH_HEAD_BLOCK, new FabricItemSettings()) {
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
            new BlockItem(LovelyBlocks.ITEM_TRANSMITTER_BLOCK, new FabricItemSettings()));

    public static final Item HEAD_CONSTRUCTOR_BLOCK = registerItem("head_constructor_block",
            new BlockItem(LovelyBlocks.HEAD_CONSTRUCTOR_BLOCK, new FabricItemSettings()));

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
                        tooltip.add(Text.literal(owner));
                    }
                }
            });

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(LovelyHeads.ID, name), item);
    }

    public static void registerLovelyItems() {
        LovelyHeads.LOGGER.debug("Registering items for " + LovelyHeads.ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
            content.addAfter(Items.LODESTONE, PLAYER_TELEPORT_BLOCK);
            content.addAfter(PLAYER_TELEPORT_BLOCK, ITEM_TRANSMITTER_BLOCK);
            content.addAfter(ITEM_TRANSMITTER_BLOCK,HEAD_PEDESTAL_BLOCK);
            content.addAfter(HEAD_PEDESTAL_BLOCK, HEAD_CONSTRUCTOR_BLOCK);
            content.addAfter(Items.PLAYER_HEAD, POLYMORPH_HEAD_ITEM);
        });
    }
}
