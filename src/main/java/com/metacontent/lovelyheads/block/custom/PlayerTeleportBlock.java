package com.metacontent.lovelyheads.block.custom;

import com.metacontent.lovelyheads.block.entity.HeadPedestalBlockEntity;
import com.metacontent.lovelyheads.block.entity.LovelyBlockEntities;
import com.metacontent.lovelyheads.block.entity.PlayerTeleportBlockEntity;
import com.metacontent.lovelyheads.enchantment.LovelyEnchantments;
import com.metacontent.lovelyheads.sound.LovelySounds;
import com.metacontent.lovelyheads.util.InteractingWithPedestal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
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

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class PlayerTeleportBlock extends BlockWithEntity implements InteractingWithPedestal {
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
                ((PlayerTeleportBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos))).timer = itemStack.getNbt().getInt("timer");
            }
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof PlayerTeleportBlockEntity blockEntity) {
            if (!world.isClient() && !player.isCreative()) {
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
            HeadPedestalBlockEntity entity = getPedestalEntity(world, pos);
            if (entity != null) {
                if (teleportToSkullOwner((ServerWorld) world, entity, player)) {
                    world.playSound(player, pos, LovelySounds.MAGIC_EFFECT, SoundCategory.BLOCKS, 0.5F, 5.0F);
                    world.setBlockState(pos, state.with(IS_READY, false));
                }
            }
        }
        else if (player.getStackInHand(hand).getItem() == Items.ECHO_SHARD) {
            if (world.getBlockEntity(pos) instanceof PlayerTeleportBlockEntity entity) {
                world.playSound(null, pos, LovelySounds.MAGIC_EFFECT, SoundCategory.BLOCKS, 0.1F, 2.0F);
                entity.timer += 3000;
                if (!player.isCreative()) {
                    player.getStackInHand(hand).decrement(1);
                }
            }
        }
        else {
            world.playSound(null, pos, LovelySounds.MAGIC_EFFECT_2, SoundCategory.BLOCKS, 0.05F, 2.0F);
            int duration = 6000 - ((PlayerTeleportBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos))).timer;
            int minutes = duration / 1200;
            int seconds = duration / 20 - minutes * 60;
            player.sendMessage(Text.translatable("block.lovelyheads.player_teleport_block.cooldown_message",
                    minutes + ":" + (seconds / 10) + (seconds % 10)));
        }
        return ActionResult.CONSUME;
    }

    private static boolean teleportToSkullOwner(ServerWorld serverWorld, @NotNull HeadPedestalBlockEntity blockEntity, PlayerEntity player) {
        String owner = blockEntity.getSkullOwner();
        if (owner != null /*&& !owner.equals(player.getName().getString())*/) {
            Predicate<? super ServerPlayerEntity> predicate =
                    (serverPlayer) -> Objects.equals(serverPlayer.getName().getString(), owner);

            List<ServerPlayerEntity> list = serverWorld.getPlayers(predicate, 1);

            if (!list.isEmpty()) {
                ServerPlayerEntity targetEntity = list.get(0);
                if (!InteractingWithPedestal.isTargetCloaked(targetEntity)) {
                    serverWorld.playSound(null, targetEntity.getBlockPos(), LovelySounds.MAGIC_EFFECT, SoundCategory.PLAYERS, 0.5F, 5.0F);
                    player.teleport(targetEntity.getX(), targetEntity.getY(), targetEntity.getZ());
                    player.sendMessage(Text.translatable("block.lovelyheads.player_teleport_block.teleport_message", owner));
                    return true;
                }
            }
            else {
                serverWorld.playSound(null, blockEntity.getPos(), LovelySounds.MAGIC_EFFECT_2, SoundCategory.BLOCKS, 0.05F, 2.0F);
                player.sendMessage(Text.translatable("block.lovelyheads.player_teleport_block.other_world_message", owner));
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
