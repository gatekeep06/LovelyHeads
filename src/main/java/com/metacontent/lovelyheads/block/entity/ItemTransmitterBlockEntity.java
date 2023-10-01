package com.metacontent.lovelyheads.block.entity;

import com.metacontent.lovelyheads.block.custom.ItemTransmitterBlock;
import com.metacontent.lovelyheads.screen.ItemTransmitterScreenHandler;
import com.metacontent.lovelyheads.util.ImplementedInventory;
import com.metacontent.lovelyheads.util.InteractingWithPedestal;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
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

public class ItemTransmitterBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory, InteractingWithPedestal {
    public int timer = 0;
    public ItemStack headItemStack = ItemStack.EMPTY;
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return timer;
        }

        @Override
        public void set(int index, int value) {
            timer = value;
        }

        @Override
        public int size() {
            return 1;
        }
    };

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);

    public ItemTransmitterBlockEntity(BlockPos pos, BlockState state) {
        super(LovelyBlockEntities.ITEM_TRANSMITTER_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ItemTransmitterBlockEntity be) {
        if (!be.isEmpty() && !world.isClient()) {
            be.timer++;
            if (be.timer >= ItemTransmitterBlock.TRANSMISSION_TIME) {
                be.transmitItems();
                be.timer = 0;
            }
            be.markDirty();
        }
        else if (be.timer != 0) {
            be.timer = 0;
            be.markDirty();
        }
    }

    public void transmitItems() {
        checkHeadItemStack();
        if (this.headItemStack.hasNbt() && world instanceof ServerWorld serverWorld) {
            String owner = this.headItemStack.getNbt().getString("SkullOwner");
            Predicate<? super ServerPlayerEntity> predicate =
                    (serverPlayer) -> Objects.equals(serverPlayer.getName().getString(), owner);

            List<ServerPlayerEntity> list = serverWorld.getPlayers(predicate, 1);

            if (!list.isEmpty()) {
                ServerPlayerEntity targetEntity = list.get(0);
                if (InteractingWithPedestal.isTargetCloaked(targetEntity)) {
                    for (ItemStack itemStack : this.inventory) {
                        targetEntity.getInventory().offerOrDrop(itemStack);
                        inventory.set(inventory.indexOf(itemStack), ItemStack.EMPTY);
                    }
                    targetEntity.sendMessage(Text.translatable("block.lovelyheads.item_transmitter_block.transmission_message"));
                    markDirty();
                }
            }
        }
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
        checkHeadItemStack();
        return new ItemTransmitterScreenHandler(syncId, playerInventory, this, this.headItemStack, propertyDelegate);
    }

    private void checkHeadItemStack() {
        HeadPedestalBlockEntity entity = getPedestalEntity(this.world, this.pos);
        ItemStack itemStack = ItemStack.EMPTY;
        if (entity != null) {
            itemStack = Items.PLAYER_HEAD.getDefaultStack();
            NbtCompound nbt = new NbtCompound();
            nbt.putString("SkullOwner", entity.getSkullOwner());
            itemStack.setNbt(nbt);
        }
        this.headItemStack = itemStack;
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
