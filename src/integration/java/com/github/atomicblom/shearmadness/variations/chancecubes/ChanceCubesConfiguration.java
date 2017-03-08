package com.github.atomicblom.shearmadness.variations.chancecubes;

import com.github.atomicblom.shearmadness.api.events.ShearMadnessSyncSettingsEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public final class ChanceCubesConfiguration {

    public static Property enabled;
    public static Property distance;

    @SubscribeEvent
    public static void onConfigSync(ShearMadnessSyncSettingsEvent event) {
        Configuration config = event.getConfig();
        String category = Configuration.CATEGORY_GENERAL + ".behaviours.chancecubes";
        enabled = config.get(category, "enabled", false, "Should chance cube sheep cause trouble");
        distance = config.get(category, "distance", 500, "The distance a sheep can affect players.");

    }

    private ChanceCubesConfiguration() {}
}
