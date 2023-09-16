package com.metacontent.lovelyheads.screen;

import com.metacontent.lovelyheads.item.LovelyItems;
import com.metacontent.lovelyheads.recipe.HeadConstructorRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class HeadConstructorScreenHandler extends ScreenHandler {
    public final Inventory inventory;
    private boolean decrementExtraInput;
    private final World world;

    public HeadConstructorScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(3));
    }

    public HeadConstructorScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(LovelyScreens.HEAD_CONSTRUCTOR_SCREEN_HANDLER, syncId);
        checkSize(inventory, 3);
        this.inventory = inventory;
        this.world = playerInventory.player.getWorld();

        inventory.onOpen(playerInventory.player);

        this.addSlot(new Slot(inventory, 0, 33, 41) {
            @Override
            public void markDirty() {
                HeadConstructorScreenHandler.this.onContentChanged(HeadConstructorScreenHandler.this.inventory);
                super.markDirty();
            }
        });
        this.addSlot(new Slot(inventory, 1, 80, 20) {
            @Override
            public void markDirty() {
                HeadConstructorScreenHandler.this.onContentChanged(HeadConstructorScreenHandler.this.inventory);
                super.markDirty();
            }
        });
        this.addSlot(new Slot(inventory, 2, 127, 41) {
            @Override
            public void markDirty() {
                HeadConstructorScreenHandler.this.onContentChanged(HeadConstructorScreenHandler.this.inventory);
                super.markDirty();
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                HeadConstructorScreenHandler.this.slots.get(0).takeStack(1);
                if (HeadConstructorScreenHandler.this.decrementExtraInput) {
                    HeadConstructorScreenHandler.this.slots.get(1).takeStack(1);
                }
                super.onTakeItem(player, stack);
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }

        this.onContentChanged(inventory);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        ItemStack base = inventory.getStack(0);
        ItemStack extra = inventory.getStack(1);
        ItemStack result = inventory.getStack(2);
        if (result.isEmpty() || !base.isEmpty() && !extra.isEmpty()) {
            if (!base.isEmpty() && !extra.isEmpty()) {
                this.updateResult();
            }
        }
        else {
            this.inventory.removeStack(2);
        }
    }

    private void updateResult() {
        if (!world.isClient()) {
            Optional<HeadConstructorRecipe> match = world.getRecipeManager().getFirstMatch(HeadConstructorRecipe.Type.INSTANCE,
                    this.inventory, this.world);
            if (match.isPresent()) {
                ItemStack result = match.get().craft(this.inventory, world.getRegistryManager());
                if (result.getItem() == LovelyItems.HEAD_SCHEME_ITEM) {
                    if (this.slots.get(0).getStack().hasNbt()) {
                        NbtCompound nbt = this.slots.get(0).getStack().getNbt();
                        result.setNbt(nbt);
                    }
                    else {
                        result = ItemStack.EMPTY;
                    }
                }
                else if (result.getItem() == Items.PLAYER_HEAD) {
                    if (this.slots.get(1).getStack().hasNbt()) {
                        NbtCompound nbt = this.slots.get(1).getStack().getNbt();
                        result.setNbt(nbt);
                    }
                }
                this.inventory.setStack(2, result);
                this.decrementExtraInput = match.get().isDecrementExtraInput();
            }
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size() - 1) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (invSlot == 2) {
                return ItemStack.EMPTY;
            }
            else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            }
            else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.removeStack(2);
    }
}
