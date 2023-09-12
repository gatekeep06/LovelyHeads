package com.metacontent.lovelyheads.status_effect;

import com.metacontent.lovelyheads.LovelyHeads;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

public class BeheadingMarkStatusEffect extends StatusEffect {
    public BeheadingMarkStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0xbb0a1e);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.isRemoved() && entity.getWorld() instanceof ServerWorld serverWorld) {
            ItemStack stack = ItemStack.EMPTY;

            if (entity instanceof PlayerEntity playerEntity) {
                stack = new ItemStack(Items.PLAYER_HEAD);
                NbtCompound nbt = new NbtCompound();
                nbt.putString("SkullOwner", playerEntity.getName().getString());
                stack.setNbt(nbt);
            }
            else if (serverWorld.getGameRules().getBoolean(LovelyHeads.SHOULD_MOBS_DROP_HEADS)) {
                if (entity instanceof ZombieEntity && !(entity instanceof ZombifiedPiglinEntity || entity instanceof ZombieVillagerEntity)) {
                    stack = new ItemStack(Items.ZOMBIE_HEAD);
                }
                else if (entity instanceof SkeletonEntity) {
                    stack = new ItemStack(Items.SKELETON_SKULL);
                }
                else if (entity instanceof CreeperEntity) {
                    stack = new ItemStack(Items.CREEPER_HEAD);
                }
                else if (entity instanceof PiglinEntity || entity instanceof PiglinBruteEntity) {
                    stack = new ItemStack(Items.PIGLIN_HEAD);
                }
                else if (entity instanceof WitherSkeletonEntity && serverWorld.getGameRules().getBoolean(LovelyHeads.SHOULD_WITHER_SKELETONS_DROP_HEADS)) {
                    stack = new ItemStack(Items.WITHER_SKELETON_SKULL);
                }
            }

            entity.dropStack(stack);
        }
    }
}
