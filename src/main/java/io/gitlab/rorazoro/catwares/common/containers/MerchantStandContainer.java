package io.gitlab.rorazoro.catwares.common.containers;

import java.util.Objects;

import io.gitlab.rorazoro.catwares.common.registries.BlockRegistry;
import io.gitlab.rorazoro.catwares.common.registries.ContainerRegistry;
import io.gitlab.rorazoro.catwares.common.tileentities.MerchantStandTileEntity;
import io.gitlab.rorazoro.catwares.common.util.Helper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

public class MerchantStandContainer extends Container {

    private final IWorldPosCallable canInteractWithCallable;

    public final MerchantStandTileEntity tileEntity;

    public MerchantStandContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    public MerchantStandContainer(final int windowId, final PlayerInventory playerInventory,
            final MerchantStandTileEntity te) {
        super(ContainerRegistry.MERCHANT_STAND_CONTAINER_TYPE.get(), windowId);
        this.tileEntity = te;
        this.canInteractWithCallable = IWorldPosCallable.of(te.getWorld(), te.getPos());

        int slotSize = 16;
        int slotPadding = 2;

        // Merchant Stand Inventory
        // 0 1 2
        // 3 4 5
        // 6 7 8
        int index = 0;
        int startStandInvX = 62;
        int startStandInvY = 17;
        int maxStandInvRows = 3;
        int maxStandInvCols = 3;
        for (int row = 0; row < maxStandInvRows; ++row) {
            for (int col = 0; col < maxStandInvCols; ++col) {
                int posX = startStandInvX + (col * (slotSize + slotPadding));
                int posY = startStandInvY + (row * (slotSize + slotPadding));
                this.addSlot(new Slot(this.tileEntity, index, posX, posY));
                index++;
            }
        }

        // Merchant Stand Buy
        int standBuyX = 26;
        int standBuyY = 35;
        this.addSlot(new Slot(this.tileEntity, index, standBuyX, standBuyY));
        index++;

        // Merchant Stand Sell
        int standSellX = 134;
        int standSellY = 35;
        this.addSlot(new Slot(this.tileEntity, index, standSellX, standSellY));
        index++;

        // Player Hotbar
        index = 0;
        int startPlayerHotX = 8;
        int startPlayerHotY = 142;
        int maxPlayerHotRows = 1;
        int maxPlayerHotCols = 9;
        for (int row = 0; row < maxPlayerHotRows; ++row) {
            for (int col = 0; col < maxPlayerHotCols; ++col) {
                int posX = startPlayerHotX + (col * (slotSize + slotPadding));
                int posY = startPlayerHotY + (row * (slotSize + slotPadding));
                this.addSlot(new Slot(playerInventory, index, posX, posY));
                index++;
            }
        }

        // Player Inventory

        int startPlayerInvX = 8;
        int startPlayerInvY = 84;
        int maxPlayerInvRows = 3;
        int maxPlayerInvCols = 9;
        for (int row = 0; row < maxPlayerInvRows; ++row) {
            for (int col = 0; col < maxPlayerInvCols; ++col) {
                int posX = startPlayerInvX + (col * (slotSize + slotPadding));
                int posY = startPlayerInvY + (row * (slotSize + slotPadding));
                this.addSlot(new Slot(playerInventory, index, posX, posY));
                index++;
            }
        }

    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        // return isWithinUsableDistance(canInteractWithCallable, playerIn,
        // BlockRegistry.MERCHANT_STAND_BLOCK.get());
        return this.tileEntity.isUsableByPlayer(playerIn);
    }

    private static MerchantStandTileEntity getTileEntity(final PlayerInventory playerInventory,
            final PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tileAtPos = playerInventory.player.world.getTileEntity(data.readBlockPos());
        if (tileAtPos instanceof MerchantStandTileEntity) {
            return (MerchantStandTileEntity) tileAtPos;
        }
        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack newItemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack existingItemStack = slot.getStack();
            newItemStack = existingItemStack.copy();
            if (index < MerchantStandTileEntity.INV_SIZE) {
                if (!this.mergeItemStack(existingItemStack, MerchantStandTileEntity.FULL_INV_SIZE,
                        this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(existingItemStack, 0, MerchantStandTileEntity.INV_SIZE, false)) {
                return ItemStack.EMPTY;
            }

            if (existingItemStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return newItemStack;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        if (slotId == MerchantStandTileEntity.PRICE_SLOT_INDEX || slotId == MerchantStandTileEntity.OFFER_SLOT_INDEX) {
            Slot slot = slotId < 0 ? null : this.inventorySlots.get(slotId);
            if (slot != null) {
                ItemStack playerStack = player.inventory.getItemStack();
                ItemStack newStack = ItemStack.EMPTY;
                if (!checkPriceOfferSame(slot, playerStack)) {
                    ItemStack existingItemStack = slot.getStack();

                    if (existingItemStack.isEmpty()) {
                        ItemStack newGhostStack = playerStack.copy();
                        // if clicking with punch button, set whole stack
                        newGhostStack.setCount(dragType == 0 ? playerStack.getCount() : 1);
                        slot.putStack(newGhostStack);
                        newStack = newGhostStack;
                    } else {
                        if (Container.areItemsAndTagsEqual(existingItemStack, playerStack)) {
                            int newCount = 0;
                            if (dragType == 0) {
                                newCount = playerStack.getCount();
                            } else {
                                newCount = Helper.clamp(existingItemStack.getCount() + 1, 0, 64);
                            }
                            existingItemStack.setCount(newCount);
                        } else {
                            ItemStack newGhostStack = playerStack.copy();
                            newGhostStack.setCount(dragType == 0 ? newGhostStack.getCount() : 1);
                            slot.putStack(newGhostStack);
                        }
                        return existingItemStack;
                    }
                }
                return newStack;
            }
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    private boolean checkPriceOfferSame(Slot slot, ItemStack playerItemStack) {
        Slot otherSlot = null;
        if (slot.getSlotIndex() == MerchantStandTileEntity.PRICE_SLOT_INDEX) {
            otherSlot = this.inventorySlots.get(MerchantStandTileEntity.OFFER_SLOT_INDEX);
        } else {
            otherSlot = this.inventorySlots.get(MerchantStandTileEntity.PRICE_SLOT_INDEX);
        }

        ItemStack slotItemStack = otherSlot.getStack();
        return Container.areItemsAndTagsEqual(slotItemStack, playerItemStack);
    }
}
