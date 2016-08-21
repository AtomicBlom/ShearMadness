package com.github.atomicblom.chiselsheep.configuration;

import com.github.atomicblom.chiselsheep.utility.Reference;
import com.github.atomicblom.chiselsheep.utility.Logger;
import com.google.common.base.Objects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static com.google.common.base.Preconditions.*;

@SuppressWarnings("NonSerializableFieldInSerializableClass")
public enum ConfigurationHandler
{
    INSTANCE;
    private static final String CONFIG_VERSION = "1";
    private File fileRef = null;
    private Configuration config = null;
    private Optional<Configuration> configOld = Optional.empty();

    public static void init(File configFile)
    {
        INSTANCE.setConfig(configFile);
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    public Configuration getConfig()
    {
        return config;
    }

    private void setConfig(File configFile)
    {
        checkState(config == null, "ConfigurationHandler has been initialized more than once.");

        fileRef = configFile;

        config = new Configuration(configFile, CONFIG_VERSION);

        if (!CONFIG_VERSION.equals(config.getDefinedConfigVersion()))
        {
            final File fileBak = new File(fileRef.getAbsolutePath() + '_' + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".old");
            Logger.warning("Your %s config file is out of date and could cause issues. The existing file will be renamed to %s and a new one will be generated.",
                    Reference.MOD_NAME, fileBak.getName());
            Logger.warning("%s will attempt to copy your old settings, but custom mod/tree settings will have to be migrated manually.", Reference.MOD_NAME);

            final boolean success = fileRef.renameTo(fileBak);
            Logger.warning("Rename %s successful.", success ? "was" : "was not");
            configOld = Optional.of(config);
            config = new Configuration(fileRef, CONFIG_VERSION);
        }

        syncConfig(true);
    }

    void syncConfig()
    {
        syncConfig(false);
    }

    private void syncConfig(boolean skipLoad)
    {
        if (!skipLoad)
        {
            //noinspection OverlyBroadCatchBlock
            try
            {
                config.load();
            } catch (final Exception e)
            {
                final File fileBak = new File(fileRef.getAbsolutePath() + '_' + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".errored");
                Logger.severe("An exception occurred while loading your config file. This file will be renamed to %s and a new config file will be generated.", fileBak.getName());
                Logger.severe("Exception encountered: %s", e.getLocalizedMessage());

                final boolean success = fileRef.renameTo(fileBak);
                Logger.warning("Rename %s successful.", success ? "was" : "was not");

                config = new Configuration(fileRef, CONFIG_VERSION);
            }
        }

        Settings.syncConfig(config);

        convertOldConfig();
        saveConfig();
    }

    private void saveConfig()
    {
        if (config.hasChanged())
        {
            config.save();
        }
    }

    private void convertOldConfig()
    {
        if (configOld.isPresent())
        {
            // Handle old config versions (none yet)

            Settings.syncConfig(config);
            configOld = Optional.empty();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equalsIgnoreCase(Reference.MOD_ID))
        {
            saveConfig();
            syncConfig();
        }
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("fileRef", fileRef)
                .add("config", config)
                .add("configOld", configOld)
                .toString();
    }
}
