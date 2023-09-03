package com.metacontent.lovelyheads.block.custom;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PolymorphHeadBlock extends Block {
    public PolymorphHeadBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        world.setBlockState(pos, placer != null
                ? Blocks.PLAYER_HEAD.getDefaultState().with(SkullBlock.ROTATION, RotationPropertyHelper.fromYaw(placer.getYaw()))
                : Blocks.PLAYER_HEAD.getDefaultState());
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof SkullBlockEntity skullBlockEntity) {
            GameProfile gameProfile = null;
            if (placer != null) {
                gameProfile = new GameProfile(null, placer.getEntityName());
            }
            skullBlockEntity.setOwner(gameProfile);
        }

    }
}
