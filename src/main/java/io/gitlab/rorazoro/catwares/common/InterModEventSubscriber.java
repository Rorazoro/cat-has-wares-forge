package io.gitlab.rorazoro.catwares.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

@Mod.EventBusSubscriber(modid = "catwares", bus = Mod.EventBusSubscriber.Bus.MOD)
public class InterModEventSubscriber {

    private static final Logger LOGGER = LogManager.getLogger();

    private InterModEventSubscriber() {
        throw new IllegalStateException("Event Subscriber class");
    }

    @SubscribeEvent
    public static void onInterModEnqueue(final InterModEnqueueEvent event) {
        LOGGER.info("Event fired on Inter Mod Enqueue");
    }

    @SubscribeEvent
    public static void onInterModProcess(final InterModProcessEvent event) {
        LOGGER.info("Event fired on Inter Mod Process");
    }
}
