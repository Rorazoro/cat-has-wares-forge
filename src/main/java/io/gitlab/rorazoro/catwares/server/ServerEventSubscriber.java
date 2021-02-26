package io.gitlab.rorazoro.catwares.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

@Mod.EventBusSubscriber(modid = "catwares", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerEventSubscriber {

    private static final Logger LOGGER = LogManager.getLogger();

    private ServerEventSubscriber() {
        throw new IllegalStateException("Event Subscriber class");
    }

    @SubscribeEvent
    public static void onServerSetup(final FMLDedicatedServerSetupEvent event) {
        LOGGER.info("Event fired on Dedicated Server Setup");
    }
}
