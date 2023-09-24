package com.metacontent.lovelyheads.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SkullBlock;
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

    public SkullBlock.Type getSkullType() {
        BlockPos skullPos = this.pos.add(0, 1, 0);
        SkullBlock.Type skullType = null;
        BlockState state = this.world.getBlockState(skullPos);
        if (state.getBlock() == Blocks.PLAYER_HEAD) {
            skullType = SkullBlock.Type.PLAYER;
        }
        else if (state.getBlock() == Blocks.SKELETON_SKULL) {
            skullType = SkullBlock.Type.SKELETON;
        }
        else if (state.getBlock() == Blocks.WITHER_SKELETON_SKULL) {
            skullType = SkullBlock.Type.WITHER_SKELETON;
        }
        else if (state.getBlock() == Blocks.ZOMBIE_HEAD) {
            skullType = SkullBlock.Type.ZOMBIE;
        }
        else if (state.getBlock() == Blocks.CREEPER_HEAD) {
            skullType = SkullBlock.Type.CREEPER;
        }
        else if (state.getBlock() == Blocks.PIGLIN_HEAD) {
            skullType = SkullBlock.Type.PIGLIN;
        }

        return skullType;
    }
}
