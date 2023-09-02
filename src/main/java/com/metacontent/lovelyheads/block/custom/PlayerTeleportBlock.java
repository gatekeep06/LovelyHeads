package com.metacontent.lovelyheads.block.custom;

import com.metacontent.lovelyheads.block.entity.HeadPedestalBlockEntity;
import com.metacontent.lovelyheads.block.entity.LovelyBlockEntities;
import com.metacontent.lovelyheads.block.entity.PlayerTeleportBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class PlayerTeleportBlock extends BlockWithEntity {
    public static final int CD = 6000;
    public static final BooleanProperty IS_READY = BooleanProperty.of("is_ready");

    public PlayerTeleportBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(IS_READY, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IS_READY);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient()) {
            if (!state.get(IS_READY) && itemStack.hasNbt()) {
                ((PlayerTeleportBlockEntity) world.getBlockEntity(pos)).timer = itemStack.getNbt().getInt("timer");
            }
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof PlayerTeleportBlockEntity blockEntity) {
            if (!world.isClient() /*&& !player.isCreative()*/) {
                ItemStack itemStack = new ItemStack(this);
                blockEntity.setStackNbt(itemStack);
                ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            }
        }

        super.onBreak(world, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        if (ctx.getStack().hasNbt()) {
            if (ctx.getStack().getNbt().getInt("timer") >= CD) {
                return getDefaultState().with(IS_READY, true);
            }
        }
        return super.getPlacementState(ctx);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlayerTeleportBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }
        else if (state.get(IS_READY)) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        BlockPos blockPos = new BlockPos(pos.getX() + i - 1, pos.getY() + j - 1, pos.getZ() + k - 1);
                        if (world.getBlockEntity(blockPos) instanceof HeadPedestalBlockEntity headPedestalBlockEntity) {
                            if (teleportToSkullOwner((ServerWorld) world, headPedestalBlockEntity, player)) {
                                world.setBlockState(pos, state.with(IS_READY, false));
                            }
                            return ActionResult.CONSUME;
                        }
                    }
                }
            }
        }
        else {
            int duration = 6000 - ((PlayerTeleportBlockEntity) world.getBlockEntity(pos)).timer;
            int minutes = duration / 1200;
            int seconds = duration / 20 - minutes * 60;
            player.sendMessage(Text.translatable("block.lovelyheads.player_teleport_block.cooldown_message",
                    minutes + ":" + (seconds / 10) + (seconds % 10)));
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

    private static boolean teleportToSkullOwner(ServerWorld serverWorld, @NotNull HeadPedestalBlockEntity blockEntity, PlayerEntity player) {
        String owner = blockEntity.getSkullOwner();
        if (owner != null && !owner.equals(player.getName().getString())) {
            Predicate<? super ServerPlayerEntity> predicate =
                    (serverPlayer) -> Objects.equals(serverPlayer.getName().getString(), owner);

            List<ServerPlayerEntity> list = serverWorld.getPlayers(predicate, 1);

            if (!list.isEmpty()) {
                ServerPlayerEntity targetEntity = list.get(0);
                player.teleport(targetEntity.getX(), targetEntity.getY(), targetEntity.getZ());
                player.sendMessage(Text.translatable("block.lovelyheads.player_teleport_block.teleport_message", owner));
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, LovelyBlockEntities.PLAYER_TELEPORT_BLOCK_ENTITY, PlayerTeleportBlockEntity::tick);
    }
}
