package com.metacontent.lovelyheads;

import com.metacontent.lovelyheads.block.LovelyBlocks;
import com.metacontent.lovelyheads.block.entity.LovelyBlockEntities;
import com.metacontent.lovelyheads.command.LovelyCommands;
import com.metacontent.lovelyheads.enchantment.BeheadingEnchantment;
import com.metacontent.lovelyheads.enchantment.LovelyEnchantments;
import com.metacontent.lovelyheads.item.LovelyItems;
import com.metacontent.lovelyheads.recipe.HeadConstructorRecipe;
import com.metacontent.lovelyheads.screen.LovelyScreens;
import com.metacontent.lovelyheads.sound.LovelySounds;
import com.metacontent.lovelyheads.status_effect.BeheadingMarkStatusEffect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LovelyHeads implements ModInitializer {
    public static final String ID = "lovelyheads";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    public static StatusEffect BEHEADING_MARK = new BeheadingMarkStatusEffect();

    public static final Identifier ANCIENT_CITY_CHEST_LOOT_TABLE_ID = LootTables.ANCIENT_CITY_CHEST;

    public static final GameRules.Key<GameRules.BooleanRule> SHOULD_MOBS_DROP_HEADS =
            GameRuleRegistry.register("shouldMobsDropHeads", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));

    public static final GameRules.Key<GameRules.BooleanRule> SHOULD_WITHER_SKELETONS_DROP_HEADS =
            GameRuleRegistry.register("shouldWitherSkeletonsDropHeads", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));

    @Override
    public void onInitialize() {
        LovelyItems.registerLovelyItems();
        LovelySounds.registerLovelySounds();
        LovelyBlocks.registerLovelyBlocks();
        LovelyScreens.registerLovelyScreens();
        LovelyCommands.registerLovelyCommands();
        LovelyEnchantments.registerLovelyEnchantments();
        LovelyBlockEntities.registerLovelyBlockEntities();

        Registry.register(Registries.STATUS_EFFECT, new Identifier(ID, "beheading_mark"), BEHEADING_MARK);

        Registry.register(Registries.RECIPE_SERIALIZER, HeadConstructorRecipe.Serializer.ID, HeadConstructorRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, new Identifier(ID, HeadConstructorRecipe.Type.ID), HeadConstructorRecipe.Type.INSTANCE);

        LootTableEvents.MODIFY.register(((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin() && ANCIENT_CITY_CHEST_LOOT_TABLE_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder().with(ItemEntry.builder(LovelyItems.POLYMORPH_HEAD_ITEM));
                tableBuilder.pool(poolBuilder);
            }
        }));
    }
}
