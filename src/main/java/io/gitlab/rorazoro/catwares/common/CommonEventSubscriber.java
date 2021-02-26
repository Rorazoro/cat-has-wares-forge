package io.gitlab.rorazoro.catwares.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = "catwares", bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEventSubscriber {

    private static final Logger LOGGER = LogManager.getLogger();

    private CommonEventSubscriber() {
        throw new IllegalStateException("Event Subscriber class");
    }

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Event fired on Common Setup");
    }
}
