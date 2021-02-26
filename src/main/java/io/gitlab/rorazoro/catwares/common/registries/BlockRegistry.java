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

    public static final RegistryObject<Block> MERCHANT_STAND_BLOCK = BLOCKS.register("merchant_stand",
            () -> new MerchantStandBlock(AbstractBlock.Properties.from(Blocks.OAK_WOOD)));

}