package com.metacontent.lovelyheads.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class HeadConstructorScreenHandler extends ScreenHandler {
    public final Inventory inventory;

    public HeadConstructorScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(3));
    }

    public HeadConstructorScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(LovelyScreens.HEAD_CONSTRUCTOR_SCREEN_HANDLER, syncId);
        checkSize(inventory, 3);
        this.inventory = inventory;

        inventory.onOpen(playerInventory.player);

        this.addSlot(new Slot(inventory, 0, 33, 41));
        this.addSlot(new Slot(inventory, 1, 80, 20));
        this.addSlot(new Slot(inventory, 2, 127, 41) {
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
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
