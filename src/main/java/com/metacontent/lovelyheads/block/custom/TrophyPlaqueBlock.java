package com.metacontent.lovelyheads.block.custom;

import com.metacontent.lovelyheads.block.entity.TrophyPlaqueBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TrophyPlaqueBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.FACING;
    private static final VoxelShape NORTH;
    private static final VoxelShape EAST;
    private static final VoxelShape SOUTH;
    private static final VoxelShape WEST;
    private static final VoxelShape UP;
    private static final VoxelShape DOWN;

    public TrophyPlaqueBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }
        else {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof TrophyPlaqueBlockEntity trophyPlaqueBlockEntity) {
                if (trophyPlaqueBlockEntity.getStack(0).isEmpty()) {
                    ItemStack stack = player.getStackInHand(hand);
                    if (stack.isOf(Items.PLAYER_HEAD) || stack.isOf(Items.SKELETON_SKULL)
                            || stack.isOf(Items.ZOMBIE_HEAD) || stack.isOf(Items.WITHER_SKELETON_SKULL)
                            || stack.isOf(Items.PIGLIN_HEAD) || stack.isOf(Items.CREEPER_HEAD)
                            || stack.isOf(Items.DRAGON_HEAD)) {
                        ItemStack trophy = stack.copy();
                        stack.decrement(1);
                        trophy.setCount(1);
                        trophyPlaqueBlockEntity.setStack(0, trophy);
                        if (state.get(FACING) == Direction.DOWN || state.get(FACING) == Direction.UP) {
                            trophyPlaqueBlockEntity.setStackDirection(player.getHorizontalFacing().getOpposite());
                        }
                        trophyPlaqueBlockEntity.updateListeners();
                    }
                }
            }
            return ActionResult.CONSUME;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getSide());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case UP -> UP;
            case DOWN -> DOWN;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
        };
    }

    static {
        NORTH = Block.createCuboidShape(2, 1, 14, 14, 15, 16);
        EAST = Block.createCuboidShape(0, 1, 2, 2, 15, 14);
        SOUTH = Block.createCuboidShape(2, 1, 0, 14, 15, 2);
        WEST = Block.createCuboidShape(14, 1, 2, 16, 15, 14);
        UP = Block.createCuboidShape(1, 0, 1, 15, 2, 15);
        DOWN = Block.createCuboidShape(1, 14, 1, 15, 16, 15);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrophyPlaqueBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof TrophyPlaqueBlockEntity trophyPlaqueBlockEntity) {
                ItemScatterer.spawn(world, pos, trophyPlaqueBlockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
