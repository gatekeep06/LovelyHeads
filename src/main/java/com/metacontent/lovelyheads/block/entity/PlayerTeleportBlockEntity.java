package com.metacontent.lovelyheads.block.entity;

import com.metacontent.lovelyheads.block.custom.PlayerTeleportBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlayerTeleportBlockEntity extends BlockEntity {
    public int timer = 0;

    public PlayerTeleportBlockEntity(BlockPos pos, BlockState state) {
        super(LovelyBlockEntities.PLAYER_TELEPORT_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.timer = nbt.getInt("timer");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("timer", timer);
        super.writeNbt(nbt);
    }

    public static void tick(World world, BlockPos pos, BlockState state, PlayerTeleportBlockEntity be) {
        if (!state.get(PlayerTeleportBlock.IS_READY)) {
            be.timer++;
            if (be.timer >= PlayerTeleportBlock.CD) {
                world.setBlockState(pos, state.with(PlayerTeleportBlock.IS_READY, true));
                be.timer = 0;
            }
            be.markDirty();
        }
    }

    @Override
    public void setStackNbt(ItemStack stack) {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("timer", timer == 0 ? PlayerTeleportBlock.CD : timer);
        stack.setNbt(nbt);
    }
}
