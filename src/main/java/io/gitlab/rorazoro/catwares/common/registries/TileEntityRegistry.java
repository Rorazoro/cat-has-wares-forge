package io.gitlab.rorazoro.catwares.common.registries;

import io.gitlab.rorazoro.catwares.common.ModConstants;
import io.gitlab.rorazoro.catwares.common.tileentities.MerchantStandTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityRegistry {
        public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister
                        .create(ForgeRegistries.TILE_ENTITIES, ModConstants.MODID);

        public static final RegistryObject<TileEntityType<MerchantStandTileEntity>> MERCHANT_STAND_TILE_ENTITY_TYPE = TILE_ENTITY_TYPES
                        .register("merchant_stand",
                                        () -> TileEntityType.Builder
                                                        .create(MerchantStandTileEntity::new,
                                                                        BlockRegistry.MERCHANT_STAND_BLOCK.get())
                                                        .build(null));
}
