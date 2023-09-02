package com.metacontent.lovelyheads.mixin;

import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = SkullBlock.class)
public abstract class SkullBlockMixin extends AbstractSkullBlock {
    public SkullBlockMixin(SkullBlock.SkullType type, Settings settings) {
        super(type, settings);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        ItemStack stack = new ItemStack(this);
        if (((SkullBlockEntity) world.getBlockEntity(pos)).getOwner() != null) {
            NbtCompound nbt = new NbtCompound();
            String name = ((SkullBlockEntity) world.getBlockEntity(pos)).getOwner().getName();
            nbt.putString("SkullOwner", name);
            stack.setNbt(nbt);
        }
        return stack;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        else if (((SkullBlockEntity) world.getBlockEntity(pos)).getOwner() != null) {
            player.sendMessage(Text.literal(((SkullBlockEntity) world.getBlockEntity(pos)).getOwner().getName()));
            return ActionResult.CONSUME;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }
}
