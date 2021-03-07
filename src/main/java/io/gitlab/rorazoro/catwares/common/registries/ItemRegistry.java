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
        public static final RegistryObject<BlockItem> OAK_PLANKS_MERCHANT_STAND_BLOCK = ITEMS.register(
                        "oak_planks_merchant_stand",
                        () -> new BlockItem(BlockRegistry.OAK_PLANKS_MERCHANT_STAND_BLOCK.get(),
                                        new Item.Properties().group(ItemGroups.MOD_ITEM_GROUP)));
        public static final RegistryObject<BlockItem> SPRUCE_PLANKS_MERCHANT_STAND_BLOCK = ITEMS.register(
                        "spruce_planks_merchant_stand",
                        () -> new BlockItem(BlockRegistry.SPRUCE_PLANKS_MERCHANT_STAND_BLOCK.get(),
                                        new Item.Properties().group(ItemGroups.MOD_ITEM_GROUP)));
        public static final RegistryObject<BlockItem> BIRCH_PLANKS_MERCHANT_STAND_BLOCK = ITEMS.register(
                        "birch_planks_merchant_stand",
                        () -> new BlockItem(BlockRegistry.BIRCH_PLANKS_MERCHANT_STAND_BLOCK.get(),
                                        new Item.Properties().group(ItemGroups.MOD_ITEM_GROUP)));
        public static final RegistryObject<BlockItem> JUNGLE_PLANKS_MERCHANT_STAND_BLOCK = ITEMS.register(
                        "jungle_planks_merchant_stand",
                        () -> new BlockItem(BlockRegistry.JUNGLE_PLANKS_MERCHANT_STAND_BLOCK.get(),
                                        new Item.Properties().group(ItemGroups.MOD_ITEM_GROUP)));
        public static final RegistryObject<BlockItem> ACACIA_PLANKS_MERCHANT_STAND_BLOCK = ITEMS.register(
                        "acacia_planks_merchant_stand",
                        () -> new BlockItem(BlockRegistry.ACACIA_PLANKS_MERCHANT_STAND_BLOCK.get(),
                                        new Item.Properties().group(ItemGroups.MOD_ITEM_GROUP)));
        public static final RegistryObject<BlockItem> DARK_OAK_PLANKS_MERCHANT_STAND_BLOCK = ITEMS.register(
                        "dark_oak_planks_merchant_stand",
                        () -> new BlockItem(BlockRegistry.DARK_OAK_PLANKS_MERCHANT_STAND_BLOCK.get(),
                                        new Item.Properties().group(ItemGroups.MOD_ITEM_GROUP)));
        public static final RegistryObject<BlockItem> CRIMSON_PLANKS_MERCHANT_STAND_BLOCK = ITEMS.register(
                        "crimson_planks_merchant_stand",
                        () -> new BlockItem(BlockRegistry.CRIMSON_PLANKS_MERCHANT_STAND_BLOCK.get(),
                                        new Item.Properties().group(ItemGroups.MOD_ITEM_GROUP)));
        public static final RegistryObject<BlockItem> WARPED_PLANKS_MERCHANT_STAND_BLOCK = ITEMS.register(
                        "warped_planks_merchant_stand",
                        () -> new BlockItem(BlockRegistry.WARPED_PLANKS_MERCHANT_STAND_BLOCK.get(),
                                        new Item.Properties().group(ItemGroups.MOD_ITEM_GROUP)));
}
