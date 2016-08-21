package com.github.atomicblom.shearmadness.configuration.client;

import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.configuration.ConfigurationHandler;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import java.util.List;

public class ConfigGUI extends GuiConfig
{
    public ConfigGUI(GuiScreen parent)
    {
        super(parent, getConfigElements(), Reference.MOD_ID, false, false,
                GuiConfig.getAbridgedConfigPath(ConfigurationHandler.INSTANCE.getConfig().toString()));
    }

    private static List<IConfigElement> getConfigElements()
    {
        final List<IConfigElement> configElements = Lists.newArrayList();

        final Configuration config = ConfigurationHandler.INSTANCE.getConfig();
        final ConfigElement general = new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL));
        configElements.addAll(general.getChildElements());

        return configElements;
    }
}