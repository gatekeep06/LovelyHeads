package com.metacontent.lovelyheads.enchantment;

import com.metacontent.lovelyheads.LovelyHeads;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LovelyEnchantments {
    public static final Enchantment BEHEADING_ENCHANTMENT = registerEnchantment("beheading", new BeheadingEnchantment());

    public static final Enchantment CLOAKING_ENCHANTMENT = registerEnchantment("cloaking", new CloakingEnchantment());

    private static Enchantment registerEnchantment(String name, Enchantment enchantment) {
        return Registry.register(Registries.ENCHANTMENT, new Identifier(LovelyHeads.ID, name), enchantment);
    }

    public static void registerLovelyEnchantments() {
        LovelyHeads.LOGGER.debug("Registering enchantments for " + LovelyHeads.ID);
    }
}
