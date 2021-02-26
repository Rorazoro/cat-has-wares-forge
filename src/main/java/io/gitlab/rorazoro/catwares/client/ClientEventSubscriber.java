package io.gitlab.rorazoro.catwares.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.gitlab.rorazoro.catwares.client.gui.MerchantStandScreen;
import io.gitlab.rorazoro.catwares.client.renderers.MerchantStandRenderer;
import io.gitlab.rorazoro.catwares.common.registries.ContainerRegistry;
import io.gitlab.rorazoro.catwares.common.registries.TileEntityRegistry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = "catwares", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventSubscriber {

    private static final Logger LOGGER = LogManager.getLogger();

    private ClientEventSubscriber() {
        throw new IllegalStateException("Event Subscriber class");
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("Event fired on Client Setup");
        ScreenManager.registerFactory(ContainerRegistry.MERCHANT_STAND_CONTAINER_TYPE.get(), MerchantStandScreen::new);

        ClientRegistry.bindTileEntityRenderer(TileEntityRegistry.MERCHANT_STAND_TILE_ENTITY_TYPE.get(),
                MerchantStandRenderer::new);
    }
}
