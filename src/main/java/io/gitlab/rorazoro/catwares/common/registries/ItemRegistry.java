package io.gitlab.rorazoro.catwares.common.registries;

import io.gitlab.rorazoro.catwares.common.ModConstants;
import io.gitlab.rorazoro.catwares.common.items.ItemGroups;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
                        ModConstants.MODID);

        private ItemRegistry() {
                throw new IllegalStateException("Utility class");
        }

        // Block Items
        public static final RegistryObject<BlockItem> MERCHANT_STAND_BLOCK = ITEMS.register("merchant_stand",
                        () -> new BlockItem(BlockRegistry.MERCHANT_STAND_BLOCK.get(),
                                        new Item.Properties().group(ItemGroups.MOD_ITEM_GROUP)));
}
