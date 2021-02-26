package io.gitlab.rorazoro.catwares.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class PartialGhostInventory implements IInventory {

    private final int divider;
    private NonNullList<ItemStack> inv;

    public PartialGhostInventory(int size, int divider) {
        this.divider = divider;
        inv = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    private boolean isGhost(int slot) {
        return slot >= divider;
    }

    @Override
    public void clear() {
        inv.clear();
    }

    @Override
    public int getSizeInventory() {
        return inv.size();
    }

    @Override
    public boolean isEmpty() {
        return inv.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inv.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (isGhost(index))
            return ItemStack.EMPTY;

        ItemStack stack = inv.get(index);
        stack.shrink(count);
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (isGhost(index))
            return ItemStack.EMPTY;

        ItemStack stack = inv.get(index);
        inv.remove(stack);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inv.set(index, stack);
    }

    @Override
    public void markDirty() {
        // Nothing really to do here
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

}
