package com.github.atomicblom.chiselsheep.configuration;

import com.github.atomicblom.chiselsheep.utility.Reference;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import static net.minecraftforge.common.config.Property.Type.STRING;

@SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
public enum Settings
{
    INSTANCE;

    public static void syncConfig(Configuration config)
    {
        Shearing.syncConfig(config);
    }

    private static boolean get(Configuration config, String settingName, String category, boolean defaultValue)
    {
        return config.getBoolean(settingName, category, defaultValue, getLocalizedComment(settingName));
    }

    private static String getLocalizedComment(String settingName)
    {
        return I18n.format("config." + Reference.MOD_ID + ':' + settingName);
    }

    public enum Shearing
    {
        INSTANCE;

        public static final String CATEGORY = Configuration.CATEGORY_GENERAL + ".shearing";

        private static ShearBehaviour behaviour = ShearBehaviour.RevertSheep;

        public static ShearBehaviour getBehaviour() { return behaviour; }

        private static void syncConfig(Configuration config) {

            String[] validNames = new String[ShearBehaviour.values().length];
            for (int i = 0; i < ShearBehaviour.values().length; i++)
            {
                validNames[i] = ShearBehaviour.values()[i].name();
            }

            String behaviour = config.getString("behaviour", CATEGORY, "RevertSheep", getLocalizedComment("behaviour"), validNames);

            Shearing.behaviour = ShearBehaviour.valueOf(behaviour);
        }
    }

    public enum ShearBehaviour {
        RevertSheep,
        ChiselFarm,
        CannotShear
        //FacadeWool,
    }
}
