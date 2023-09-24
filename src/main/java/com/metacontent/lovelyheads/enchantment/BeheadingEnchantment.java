package com.metacontent.lovelyheads.enchantment;

import com.metacontent.lovelyheads.LovelyHeads;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

public class BeheadingEnchantment extends Enchantment {
    public BeheadingEnchantment() {
        super(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }



    @Override
    public int getMinPower(int level) {
        return 15 + (level - 1) * 9;
    }

    @Override
    public int getMaxPower(int level) {
        return super.getMinPower(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (target instanceof LivingEntity livingTarget) {
            for (int roll = 0; roll < level; roll++) {
                int i = user.getRandom().nextBetween(1, 100);
                if (i <= 5) {
                    livingTarget.addStatusEffect(new StatusEffectInstance(LovelyHeads.BEHEADING_MARK, 100));
                }
            }
        }
    }
}
