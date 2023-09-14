package com.metacontent.lovelyheads.util;

import com.metacontent.lovelyheads.block.entity.HeadPedestalBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
}
