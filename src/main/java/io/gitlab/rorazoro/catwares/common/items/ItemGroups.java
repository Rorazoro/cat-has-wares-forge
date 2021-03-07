package io.gitlab.rorazoro.catwares.common.items;

import java.util.function.Supplier;

import io.gitlab.rorazoro.catwares.common.ModConstants;
import io.gitlab.rorazoro.catwares.common.registries.ItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemGroups {
    public static final ItemGroup MOD_ITEM_GROUP = new ModItemGroup(ModConstants.MODID,
            () -> new ItemStack(ItemRegistry.OAK_PLANKS_MERCHANT_STAND_BLOCK.get()));

    private ItemGroups() {
        throw new IllegalStateException("Utility class");
    }

    public static class ModItemGroup extends ItemGroup {

        private final Supplier<ItemStack> iconSupplier;

        public ModItemGroup(final String name, final Supplier<ItemStack> iconSupplier) {
            super(name);
            this.iconSupplier = iconSupplier;
        }

        @Override
        public ItemStack createIcon() {
            return iconSupplier.get();
        }
    }
}