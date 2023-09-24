package com.metacontent.lovelyheads.block.custom;

import com.metacontent.lovelyheads.block.entity.HeadPedestalBlockEntity;
import com.metacontent.lovelyheads.util.InteractingWithPedestal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class MobLocatorBlock extends Block implements InteractingWithPedestal {
    public MobLocatorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }
        else {
            HeadPedestalBlockEntity entity = getPedestalEntity(world, pos);
            if (entity != null) {
                Class<? extends LivingEntity> clazz = switch (entity.getSkullType()) {
                    case SKELETON -> SkeletonEntity.class;
                    case WITHER_SKELETON -> WitherSkeletonEntity.class;
                    case ZOMBIE -> ZombieEntity.class;
                    case CREEPER -> CreeperEntity.class;
                    case PIGLIN -> PiglinEntity.class;
                    case PLAYER -> PlayerEntity.class;
                    default -> null;
                };
                if (world instanceof ServerWorld serverWorld) {
                    Entity target = null;
                    if (clazz == PlayerEntity.class) {
                        String owner = entity.getSkullOwner();
                        if (owner != null /*&& !owner.equals(player.getName().getString())*/) {
                            Predicate<? super ServerPlayerEntity> predicate =
                                    (serverPlayer) -> Objects.equals(serverPlayer.getName().getString(), owner);

                            List<ServerPlayerEntity> list = serverWorld.getPlayers(predicate, 1);

                            if (!list.isEmpty()) {
                                target = list.get(0);
                            }
                        }
                    }
                    else if (clazz != null) {
                        target = serverWorld.getClosestEntity(clazz, TargetPredicate.DEFAULT, null, player.getBlockX(), player.getBlockY(), player.getBlockZ(), Box.of(player.getPos(), 200, 200, 200));
                    }
                    if (target != null) {
                        player.sendMessage(Text.translatable("block.lovelyheads.mob_locator_block.locate_message",
                                target.getName().getString(),
                                target.getBlockX() + "x " + target.getBlockY() + "y " + target.getBlockZ() + "z",
                                (int) player.distanceTo(target)
                        ));
                    }
                }
            }
        }

        return ActionResult.CONSUME;
    }
}
