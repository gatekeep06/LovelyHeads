package com.metacontent.lovelyheads.block.entity;

import com.metacontent.lovelyheads.screen.HeadConstructorScreenHandler;
import com.metacontent.lovelyheads.util.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class HeadConstructorBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    public HeadConstructorBlockEntity(BlockPos pos, BlockState state) {
        super(LovelyBlockEntities.HEAD_CONSTRUCTOR_BLOCK_ENTITY, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new HeadConstructorScreenHandler(syncId, playerInventory, this);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }
}
