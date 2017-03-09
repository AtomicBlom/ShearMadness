package com.github.atomicblom.shearmadness.api.events;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ShearMadnessSyncSettingsEvent extends Event {
    private Configuration config;

    public ShearMadnessSyncSettingsEvent(Configuration config) {

        this.config = config;
    }

    public Configuration getConfig() {
        return config;
    }
}
