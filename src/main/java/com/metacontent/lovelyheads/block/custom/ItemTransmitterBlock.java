package com.metacontent.lovelyheads.block.custom;

import com.metacontent.lovelyheads.block.entity.ItemTransmitterBlockEntity;
import com.metacontent.lovelyheads.block.entity.LovelyBlockEntities;
import com.metacontent.lovelyheads.sound.LovelySounds;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ItemTransmitterBlock extends BlockWithEntity {
    public static final int TRANSMISSION_TIME = 3000;

    public ItemTransmitterBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ItemTransmitterBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }
        else if (player.getStackInHand(hand).getItem() == Items.ECHO_SHARD) {
            if (world.getBlockEntity(pos) instanceof ItemTransmitterBlockEntity entity) {
                if (!entity.isEmpty()) {
                    world.playSound(null, pos, LovelySounds.MAGIC_EFFECT, SoundCategory.BLOCKS, 0.1F, 2.0F);
                    entity.timer += 3000;
                    if (!player.isCreative()) {
                        player.getStackInHand(hand).decrement(1);
                    }
                }
            }
        }
        else {
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.CONSUME;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ItemTransmitterBlockEntity itemTransmitterBlockEntity) {
                ItemScatterer.spawn(world, pos, itemTransmitterBlockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, LovelyBlockEntities.ITEM_TRANSMITTER_BLOCK_ENTITY, ItemTransmitterBlockEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
