package com.github.atomicblom.shearmadness.configuration;

import com.github.atomicblom.shearmadness.api.events.ShearMadnessSyncSettingsEvent;
import com.github.atomicblom.shearmadness.utility.Logger;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import com.google.common.base.Objects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static com.google.common.base.Preconditions.*;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "NonSerializableFieldInSerializableClass"})
public enum ConfigurationHandler
{
    INSTANCE;
    private static final String CONFIG_VERSION = "2";
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

        if (!CONFIG_VERSION.equals(config.getLoadedConfigVersion()))
        {
            final File fileBak = new File(fileRef.getAbsolutePath() + '_' + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".old");
            Logger.warning("Your %s config file is out of date and could cause issues. The existing file will be renamed to %s and a new one will be generated.",
                    CommonReference.MOD_NAME, fileBak.getName());
            Logger.warning("%s will attempt to copy your old settings, but custom mod/tree settings will have to be migrated manually.", CommonReference.MOD_NAME);

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

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void syncConfig(boolean skipLoad)
    {
        if (!skipLoad)
        {
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
            convertOldConfig();
        }

        MinecraftForge.EVENT_BUS.post(new ShearMadnessSyncSettingsEvent(config));

        Settings.syncConfig(config);


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
    public void onConfigChanged(OnConfigChangedEvent event)
    {
        if (event.getModID().equalsIgnoreCase(CommonReference.MOD_ID))
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
