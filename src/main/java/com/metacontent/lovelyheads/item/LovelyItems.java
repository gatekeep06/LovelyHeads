package com.metacontent.lovelyheads.item;

import com.metacontent.lovelyheads.LovelyHeads;
import com.metacontent.lovelyheads.block.LovelyBlocks;
import com.metacontent.lovelyheads.block.custom.PlayerTeleportBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LovelyItems {
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

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(LovelyHeads.ID, name), item);
    }

    public static void registerLovelyItems() {
        LovelyHeads.LOGGER.debug("Registering items for " + LovelyHeads.ID);
    }
}
