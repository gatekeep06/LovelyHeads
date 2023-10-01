package com.metacontent.lovelyheads.util;

import com.metacontent.lovelyheads.block.entity.HeadPedestalBlockEntity;
import com.metacontent.lovelyheads.enchantment.LovelyEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public interface InteractingWithPedestal {
    @Nullable
    default HeadPedestalBlockEntity getPedestalEntity(World world, BlockPos pos) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    BlockPos blockPos = new BlockPos(pos.getX() + i - 1, pos.getY() + j - 1, pos.getZ() + k - 1);
                    if (world.getBlockEntity(blockPos) instanceof HeadPedestalBlockEntity headPedestalBlockEntity) {
                        return headPedestalBlockEntity;
                    }
                }
            }
        }
        return null;
    }

    static boolean isTargetCloaked(LivingEntity targetEntity) {
        Iterator<ItemStack> armor = targetEntity.getArmorItems().iterator();
        while (armor.hasNext()) {
            if (armor.next().getItem() instanceof ArmorItem armorItem) {
                if (armorItem.getType() == ArmorItem.Type.HELMET && armor.next().hasNbt()) {
                    if (EnchantmentHelper.getIdFromNbt(armor.next().getNbt()) == EnchantmentHelper.getEnchantmentId(LovelyEnchantments.CLOAKING_ENCHANTMENT)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
