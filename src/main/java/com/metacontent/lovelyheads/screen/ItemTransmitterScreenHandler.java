package com.metacontent.lovelyheads.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class ItemTransmitterScreenHandler extends ScreenHandler {
    public final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    public int getSyncedInt() {
        return propertyDelegate.get(0);
    }

    public ItemTransmitterScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(9), ItemStack.EMPTY, new ArrayPropertyDelegate(1));
    }

    public ItemTransmitterScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ItemStack head, PropertyDelegate propertyDelegate) {
        super(LovelyScreens.ITEM_TRANSMITTER_SCREEN_HANDLER, syncId);
        checkSize(inventory, 9);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;

        inventory.onOpen(playerInventory.player);

        this.addProperties(propertyDelegate);

        Inventory headSlot = new SimpleInventory(1);
        headSlot.setStack(0, head);
        this.addSlot(new Slot(headSlot, 0, 152, 8) {
            @Override
            public boolean canBeHighlighted() {
                return false;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return false;
            }
        });

        for (int i = 0; i < inventory.size(); i++) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 52));
        }
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
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size() + 1) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 1, this.inventory.size() + 1, false)) {
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
