package com.github.atomicblom.shearmadness.configuration;

import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraftforge.common.config.Configuration;

@SuppressWarnings("InnerClassFieldHidesOuterClassField")
public enum Settings
{
    INSTANCE;

    public static void syncConfig(Configuration config)
    {
        Shearing.syncConfig(config);
    }

    public enum Shearing
    {
        INSTANCE;

        public static final String CATEGORY = Configuration.CATEGORY_GENERAL + ".shearing";

        @SuppressWarnings("StaticNonFinalField")
        private static ShearBehaviour behaviour = ShearBehaviour.RevertSheep;

        public static ShearBehaviour getBehaviour() { return behaviour; }

        private static void syncConfig(Configuration config) {

            final String[] validNames = new String[ShearBehaviour.values().length];
            for (int i = 0; i < ShearBehaviour.values().length; i++)
            {
                validNames[i] = ShearBehaviour.values()[i].name();
            }

            final String behaviour = config.getString("behaviour", CATEGORY, "RevertSheep", Reference.BEHAVIOUR_COMMENT, validNames);
            Shearing.behaviour = ShearBehaviour.valueOf(behaviour);
        }
    }

    public enum Chiseling
    {
        INSTANCE;

        public static final String CATEGORY = Configuration.CATEGORY_GENERAL + ".chiseling";

        private static boolean allowGlowstone = false;
        private static boolean allowRedstone = true;
        private static boolean allowCactus = true;

        public static boolean allowRedstone() {
            return allowRedstone;
        }
        public static boolean allowGlowstone() {
            return allowGlowstone;
        }
        public static boolean allowCactus() {
            return allowCactus;
        }

        private static void syncConfig(Configuration config) {
            Chiseling.allowRedstone = config.getBoolean("allowRedstone", CATEGORY, true, Reference.ALLOW_REDSTONE_COMMENT);
            Chiseling.allowGlowstone = config.getBoolean("allowGlowstone", CATEGORY, false, Reference.ALLOW_GLOWSTONE_COMMENT);
            Chiseling.allowCactus = config.getBoolean("allowCactus", CATEGORY, true, Reference.ALLOW_CACTUS_COMMENT);
        }
    }

}
