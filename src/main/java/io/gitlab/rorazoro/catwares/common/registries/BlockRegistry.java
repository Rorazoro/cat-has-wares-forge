package io.gitlab.rorazoro.catwares.common.registries;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import io.gitlab.rorazoro.catwares.common.ModConstants;
import io.gitlab.rorazoro.catwares.common.blocks.MerchantStandBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
                        ModConstants.MODID);

        private BlockRegistry() {
                throw new IllegalStateException("Utility class");
        }

        public static final RegistryObject<Block> OAK_PLANKS_MERCHANT_STAND_BLOCK = BLOCKS.register(
                        "oak_planks_merchant_stand",
                        () -> new MerchantStandBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS)));
        public static final RegistryObject<Block> SPRUCE_PLANKS_MERCHANT_STAND_BLOCK = BLOCKS.register(
                        "spruce_planks_merchant_stand",
                        () -> new MerchantStandBlock(AbstractBlock.Properties.from(Blocks.SPRUCE_PLANKS)));
        public static final RegistryObject<Block> BIRCH_PLANKS_MERCHANT_STAND_BLOCK = BLOCKS.register(
                        "birch_planks_merchant_stand",
                        () -> new MerchantStandBlock(AbstractBlock.Properties.from(Blocks.BIRCH_PLANKS)));
        public static final RegistryObject<Block> JUNGLE_PLANKS_MERCHANT_STAND_BLOCK = BLOCKS.register(
                        "jungle_planks_merchant_stand",
                        () -> new MerchantStandBlock(AbstractBlock.Properties.from(Blocks.JUNGLE_PLANKS)));
        public static final RegistryObject<Block> ACACIA_PLANKS_MERCHANT_STAND_BLOCK = BLOCKS.register(
                        "acacia_planks_merchant_stand",
                        () -> new MerchantStandBlock(AbstractBlock.Properties.from(Blocks.ACACIA_PLANKS)));
        public static final RegistryObject<Block> DARK_OAK_PLANKS_MERCHANT_STAND_BLOCK = BLOCKS.register(
                        "dark_oak_planks_merchant_stand",
                        () -> new MerchantStandBlock(AbstractBlock.Properties.from(Blocks.DARK_OAK_PLANKS)));
        public static final RegistryObject<Block> CRIMSON_PLANKS_MERCHANT_STAND_BLOCK = BLOCKS.register(
                        "crimson_planks_merchant_stand",
                        () -> new MerchantStandBlock(AbstractBlock.Properties.from(Blocks.CRIMSON_PLANKS)));
        public static final RegistryObject<Block> WARPED_PLANKS_MERCHANT_STAND_BLOCK = BLOCKS.register(
                        "warped_planks_merchant_stand",
                        () -> new MerchantStandBlock(AbstractBlock.Properties.from(Blocks.WARPED_PLANKS)));

}