package com.metacontent.lovelyheads.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class HeadPedestalBlockEntity extends BlockEntity {
    public HeadPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(LovelyBlockEntities.HEAD_PEDESTAL_BLOCK_ENTITY, pos, state);
    }

    @Nullable
    public String getSkullOwner() {
        BlockPos skullPos = this.pos.add(0, 1, 0);
        String owner = null;
        if (this.world.getBlockState(skullPos).getBlock() == Blocks.PLAYER_HEAD) {
            if (((SkullBlockEntity) world.getBlockEntity(skullPos)).getOwner() != null) {
                owner = ((SkullBlockEntity) world.getBlockEntity(skullPos)).getOwner().getName();
            }
        }
        return owner;
    }

    public ItemStack getHeadItemStack() {
        BlockPos skullPos = this.pos.add(0, 1, 0);
        ItemStack itemStack = ItemStack.EMPTY;
        if (this.world.getBlockState(skullPos).getBlock() == Blocks.PLAYER_HEAD) {
            itemStack = Items.PLAYER_HEAD.getDefaultStack();
            NbtCompound nbt = new NbtCompound();
            nbt.putString("SkullOwner", ((SkullBlockEntity) this.world.getBlockEntity(skullPos)).getOwner().getName());
            itemStack.setNbt(nbt);
        }
        return itemStack;
    }
}
