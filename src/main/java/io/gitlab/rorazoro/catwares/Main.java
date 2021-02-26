package io.gitlab.rorazoro.catwares;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.gitlab.rorazoro.catwares.common.*;
import io.gitlab.rorazoro.catwares.common.registries.BlockRegistry;
import io.gitlab.rorazoro.catwares.common.registries.ContainerRegistry;
import io.gitlab.rorazoro.catwares.common.registries.ItemRegistry;
import io.gitlab.rorazoro.catwares.common.registries.TileEntityRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModConstants.MODID)
public class Main {
    private static final Logger LOGGER = LogManager.getLogger();

    public Main() {
        LOGGER.info("Khajit has wares, if you have coin.");
        LOGGER.info("Hello from Cat Has Wares!!!");

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        LOGGER.info("Registering Blocks");
        BlockRegistry.BLOCKS.register(modEventBus);

        LOGGER.info("Registering Items");
        ItemRegistry.ITEMS.register(modEventBus);

        LOGGER.info("Register Tile Entities");
        TileEntityRegistry.TILE_ENTITY_TYPES.register(modEventBus);

        LOGGER.info("Registering Containers");
        ContainerRegistry.CONTAINER_TYPES.register(modEventBus);
    }
}
