package io.gitlab.rorazoro.catwares.common.registries;

import io.gitlab.rorazoro.catwares.common.ModConstants;
import io.gitlab.rorazoro.catwares.common.containers.MerchantStandContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistry {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister
            .create(ForgeRegistries.CONTAINERS, ModConstants.MODID);

    private ContainerRegistry() {
        throw new IllegalStateException("Utility class");
    }

    public static final RegistryObject<ContainerType<MerchantStandContainer>> MERCHANT_STAND_CONTAINER_TYPE = CONTAINER_TYPES
            .register("merchant_stand", () -> IForgeContainerType.create(MerchantStandContainer::new));
}
