package com.github.atomicblom.shearmadness.configuration;

import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraftforge.common.config.Configuration;

@SuppressWarnings("InnerClassFieldHidesOuterClassField")
public enum Settings
{
    INSTANCE;

    public static final String CATEGORY = Configuration.CATEGORY_GENERAL;

    private static boolean debugModels = false;
    private static boolean debugInvisibleBlocks = false;

    public static boolean debugModels()
    {
        return debugModels;
    }
    public static boolean debugInvisibleBlocks()
    {
        return debugInvisibleBlocks;
    }

    public static void syncConfig(Configuration config)
    {
        debugModels = config.getBoolean("debugModels", CATEGORY, false, Reference.DEBUG_MODELS);
        debugInvisibleBlocks = config.getBoolean("debugInvisibleBlocks", CATEGORY, false, Reference.DEBUG_INVISIBLE_BLOCKS);

        Shearing.syncConfig(config);
        Behaviours.syncConfig(config);
    }

    public enum Shearing
    {
        INSTANCE;

        public static final String CATEGORY = Configuration.CATEGORY_GENERAL + ".shearing";

        @SuppressWarnings("StaticNonFinalField")
        private static ShearBehaviour shearBehaviour = ShearBehaviour.RevertSheep;

        public static ShearBehaviour getBehaviour() { return shearBehaviour; }

        private static void syncConfig(Configuration config) {

            final String[] validNames = new String[ShearBehaviour.values().length];
            for (int i = 0; i < ShearBehaviour.values().length; i++)
            {
                validNames[i] = ShearBehaviour.values()[i].name();
            }

            final String behaviour = config.getString("behaviour", CATEGORY, "RevertSheep", Reference.BEHAVIOUR_COMMENT, validNames);
            shearBehaviour = ShearBehaviour.valueOf(behaviour);
        }
    }

    public enum Behaviours
    {
        INSTANCE;

        public static final String CATEGORY = Configuration.CATEGORY_GENERAL + ".behaviours";

        private static boolean allowGlowstone = false;
        private static boolean allowRedstone = true;
        private static boolean allowCactus = true;
        private static boolean allowTNT = true;
        private static boolean allowFireDamage = true;


        public static boolean allowRedstone() {
            return allowRedstone;
        }
        public static boolean allowGlowstone() {
            return allowGlowstone;
        }
        public static boolean allowCactus() {
            return allowCactus;
        }
        public static boolean allowTNT() {
            return allowTNT;
        }
        public static boolean allowFireDamage() {
            return allowFireDamage;
        }


        private static void syncConfig(Configuration config) {
            allowRedstone = config.getBoolean("allowRedstone", CATEGORY, true, Reference.ALLOW_REDSTONE_COMMENT);
            allowGlowstone = config.getBoolean("allowGlowstone", CATEGORY, false, Reference.ALLOW_GLOWSTONE_COMMENT);
            allowCactus = config.getBoolean("allowCactus", CATEGORY, true, Reference.ALLOW_CACTUS_COMMENT);
            allowTNT = config.getBoolean("allowTNT", CATEGORY, true, Reference.ALLOW_TNT_COMMENT);
            allowFireDamage = config.getBoolean("allowFireDamage", CATEGORY, true, Reference.ALLOW_FIRE_DAMAGE_COMMENT);
        }
    }

}
