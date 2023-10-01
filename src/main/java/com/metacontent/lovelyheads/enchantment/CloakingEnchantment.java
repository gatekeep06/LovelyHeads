package com.metacontent.lovelyheads.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class CloakingEnchantment extends Enchantment {
    protected CloakingEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.ARMOR_HEAD, new EquipmentSlot[] {EquipmentSlot.HEAD});
    }

    public int getMinPower(int level) {
        return 25;
    }

    public int getMaxPower(int level) {
        return 50;
    }

    public boolean isTreasure() {
        return true;
    }

    public boolean isCursed() {
        return true;
    }
}
