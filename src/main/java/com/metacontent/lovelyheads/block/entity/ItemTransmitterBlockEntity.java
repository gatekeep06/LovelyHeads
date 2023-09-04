package com.metacontent.lovelyheads.block.entity;

import com.metacontent.lovelyheads.block.custom.ItemTransmitterBlock;
import com.metacontent.lovelyheads.screen.ItemTransmitterScreenHandler;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class ItemTransmitterBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {
    public int timer = 0;
    public ItemStack headItemStack = ItemStack.EMPTY;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);

    public ItemTransmitterBlockEntity(BlockPos pos, BlockState state) {
        super(LovelyBlockEntities.ITEM_TRANSMITTER_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ItemTransmitterBlockEntity be) {
        if (be.timer < ItemTransmitterBlock.TRANSMISSION_TIME && !be.isEmpty()) {
            be.timer++;
            if (be.timer >= ItemTransmitterBlock.TRANSMISSION_TIME) {
                System.out.println("done but...");
                be.transmitItems();
                be.timer = 0;
            }
        }
        else if (be.timer != 0) {
            be.timer = 0;
        }
    }

    public void transmitItems() {
        this.headItemStack = getPedestalEntity() != null ? getPedestalEntity().getHeadItemStack() : ItemStack.EMPTY;
        if (this.headItemStack.hasNbt() && world instanceof ServerWorld serverWorld) {
            String owner = this.headItemStack.getNbt().getString("SkullOwner");
            Predicate<? super ServerPlayerEntity> predicate =
                    (serverPlayer) -> Objects.equals(serverPlayer.getName().getString(), owner);

            List<ServerPlayerEntity> list = serverWorld.getPlayers(predicate, 1);

            if (!list.isEmpty()) {
                ServerPlayerEntity targetEntity = list.get(0);
                for (ItemStack itemStack : this.inventory) {
                    targetEntity.giveItemStack(itemStack);
                    inventory.set(inventory.indexOf(itemStack), ItemStack.EMPTY);
                }
            }
        }
    }

    @Nullable
    private HeadPedestalBlockEntity getPedestalEntity() {
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
        this.headItemStack = getPedestalEntity() != null ? getPedestalEntity().getHeadItemStack() : ItemStack.EMPTY;
        return new ItemTransmitterScreenHandler(syncId, playerInventory, this, this.headItemStack, timer);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        this.timer = nbt.getInt("timer");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putInt("timer", timer);
        super.writeNbt(nbt);
    }
}
