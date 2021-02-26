package io.gitlab.rorazoro.catwares.common.tileentities;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.gitlab.rorazoro.catwares.common.blocks.MerchantStandBlock;
import io.gitlab.rorazoro.catwares.common.containers.MerchantStandContainer;
import io.gitlab.rorazoro.catwares.common.registries.TileEntityRegistry;
import io.gitlab.rorazoro.catwares.common.util.Helper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

public class MerchantStandTileEntity extends LockableLootTileEntity {

    public static final int INV_SIZE = 9;
    public static final int FULL_INV_SIZE = INV_SIZE + 2;
    public static final int PRICE_SLOT_INDEX = INV_SIZE;
    public static final int OFFER_SLOT_INDEX = INV_SIZE + 1;
    public static final String OWNER_KEY = "Owner";
    public static final String PRICE_KEY = "Price";
    public static final String OFFER_KEY = "Offer";

    private NonNullList<ItemStack> inventoryContents = NonNullList.withSize(INV_SIZE, ItemStack.EMPTY);
    private ItemStack price = ItemStack.EMPTY;
    private ItemStack offer = ItemStack.EMPTY;
    private IItemHandlerModifiable items = createHandler();
    private LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);
    private UUID owner = null;

    private static final Logger LOGGER = LogManager.getLogger();

    protected int numPlayersUsing;

    public MerchantStandTileEntity(final TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public MerchantStandTileEntity() {
        this(TileEntityRegistry.MERCHANT_STAND_TILE_ENTITY_TYPE.get());
    }

    public void setOwnerId(UUID id) {
        owner = id;
    }

    public UUID getOwnerId() {
        return owner;
    }

    public boolean tryExchangeItems(PlayerEntity player) {
        ItemStack heldItemStack = player.getHeldItemMainhand();
        boolean isPaymentGood = checkPayment(heldItemStack);
        boolean isStockGood = checkStock();
        boolean isStockSlotAvailable = checkStockSlot(price);

        if (isPaymentGood && isStockGood && isStockSlotAvailable) {
            // Has payment and stock, now can actually perform the exchange

            ItemStack paid = heldItemStack.split(price.getCount());

            int toDispense = offer.getCount();

            for (int i = 0; i < INV_SIZE; i++) {
                ItemStack stack = inventoryContents.get(i);
                if (stack.getItem() == offer.getItem() && ItemStack.areItemStackTagsEqual(stack, offer)) {
                    if (stack.getCount() <= toDispense) {
                        inventoryContents.set(i, ItemStack.EMPTY);
                        toDispense -= stack.getCount();

                        if (toDispense <= 0)
                            break;
                    } else {
                        stack.shrink(toDispense);
                        break;
                    }
                }
            }

            insertItemPayment(paid);
            ItemHandlerHelper.giveItemToPlayer(player, offer.copy());
            return true;
        }
        return false;
    }

    private boolean checkPayment(ItemStack payment) {
        return !payment.isEmpty() && payment.getItem() == this.price.getItem()
                && ItemStack.areItemStackTagsEqual(payment, this.price) && payment.getCount() >= this.price.getCount();
    }

    private boolean checkStock() {
        int offerCountLeft = offer.getCount();
        for (ItemStack invStack : inventoryContents) {
            if (invStack.getItem() == this.offer.getItem() && ItemStack.areItemStackTagsEqual(invStack, offer)) {
                offerCountLeft -= invStack.getCount();
                offerCountLeft = Helper.clamp(offerCountLeft, 0, 64);
            }

            if (offerCountLeft == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean checkStockSlot(ItemStack payment) {
        ItemStack simPayment = payment.copy();
        for (int i = 0; i < INV_SIZE; i++) {
            simPayment = items.insertItem(i, simPayment, true);
            if (simPayment.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void insertItemPayment(ItemStack payment) {
        for (int i = 0; i < INV_SIZE; i++) {
            payment = items.insertItem(i, payment, false);
            if (payment.isEmpty()) {
                break;
            }
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (owner == null || owner.equals(player.getUniqueID()) || player.hasPermissionLevel(2)) {
            return super.isUsableByPlayer(player);
        } else {
            return false;
        }
    }

    @Override
    public int getSizeInventory() {
        return FULL_INV_SIZE;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index == PRICE_SLOT_INDEX) {
            price = stack;
            return;
        }
        if (index == OFFER_SLOT_INDEX) {
            offer = stack;
            return;
        }

        this.inventoryContents.set(index, stack);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index == PRICE_SLOT_INDEX)
            return price;
        if (index == OFFER_SLOT_INDEX)
            return offer;
        return this.inventoryContents.get(index);
    }

    // @Override
    // public ItemStack decrStackSize(int index, int count) {
    // if (index == PRICE_SLOT_INDEX) {
    // price.shrink(count);
    // return ItemStack.EMPTY;
    // }
    // if (index == OFFER_SLOT_INDEX) {
    // offer.shrink(count);
    // return ItemStack.EMPTY;
    // }

    // ItemStack stack = this.inventoryContents.get(index);
    // stack.shrink(count);
    // if (!stack.isEmpty())
    // markDirty();

    // return stack;
    // }

    // @Override
    // public ItemStack removeStackFromSlot(int index) {
    // if (index == PRICE_SLOT_INDEX) {
    // price = ItemStack.EMPTY;
    // return ItemStack.EMPTY;
    // }
    // if (index == OFFER_SLOT_INDEX) {
    // offer = ItemStack.EMPTY;
    // return ItemStack.EMPTY;
    // }

    // ItemStack stack = this.inventoryContents.get(index);
    // this.inventoryContents.remove(stack);
    // return stack;
    // }

    // @Override
    // public boolean isEmpty() {
    // return this.inventoryContents.stream().allMatch(ItemStack::isEmpty) &&
    // price.isEmpty() && offer.isEmpty();
    // }

    // @Override
    // public void clear() {
    // this.inventoryContents.clear();
    // price = ItemStack.EMPTY;
    // offer = ItemStack.EMPTY;
    // }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventoryContents;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.inventoryContents = itemsIn;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.merchant_stand");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new MerchantStandContainer(id, player, this);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.inventoryContents);
        }
        if (owner != null) {
            compound.putUniqueId(OWNER_KEY, owner);
            compound.put(PRICE_KEY, price.write(new CompoundNBT()));
            compound.put(OFFER_KEY, offer.write(new CompoundNBT()));
        }
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.inventoryContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.inventoryContents);
        }
        owner = compound.contains(OWNER_KEY) ? compound.getUniqueId(OWNER_KEY) : null;
        price = compound.contains(PRICE_KEY) ? ItemStack.read(compound.getCompound(PRICE_KEY)) : ItemStack.EMPTY;
        offer = compound.contains(OFFER_KEY) ? ItemStack.read(compound.getCompound(OFFER_KEY)) : ItemStack.EMPTY;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    @Override
    public void openInventory(PlayerEntity player) {
        super.openInventory(player);
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.onOpenOrClose();
        }
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            this.onOpenOrClose();
        }
    }

    protected void onOpenOrClose() {
        Block block = this.getBlockState().getBlock();
        if (block instanceof MerchantStandBlock) {
            this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, block);
        }
    }

    public static int getPlayersUsing(IBlockReader reader, BlockPos pos) {
        BlockState blockState = reader.getBlockState(pos);
        if (blockState.hasTileEntity()) {
            TileEntity tileentity = reader.getTileEntity(pos);
            if (tileentity instanceof MerchantStandTileEntity) {
                return ((MerchantStandTileEntity) tileentity).numPlayersUsing;
            }
        }
        return 0;
    }

    public static void swapContents(MerchantStandTileEntity te, MerchantStandTileEntity otherTe) {
        NonNullList<ItemStack> list = te.getItems();
        te.setItems(otherTe.getItems());
        otherTe.setItems(list);
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        if (this.itemHandler != null) {
            this.itemHandler.invalidate();
            this.itemHandler = null;
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    private IItemHandlerModifiable createHandler() {
        return new InvWrapper(this);
    }

    @Override
    public void remove() {
        super.remove();
        if (itemHandler != null) {
            itemHandler.invalidate();
        }
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(getWorld().getBlockState(getPos()), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }
}