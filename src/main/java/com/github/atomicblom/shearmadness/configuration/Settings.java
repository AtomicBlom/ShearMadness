package com.github.atomicblom.shearmadness.configuration;

import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraftforge.common.config.Configuration;

@SuppressWarnings({"InnerClassFieldHidesOuterClassField", "BooleanMethodNameMustStartWithQuestion"})
public enum Settings
{
    INSTANCE;

    public static final String CATEGORY = Configuration.CATEGORY_GENERAL;

    private boolean debugModels = false;
    private boolean debugInvisibleBlocks = false;

    public static final String IS_CI_BUILD = "@CI_BUILD@";

    public static boolean isReleaseBuild() { return Boolean.parseBoolean(IS_CI_BUILD); }

    public static boolean debugModels()
    {
        return INSTANCE.debugModels;
    }
    public static boolean debugInvisibleBlocks()
    {
        return INSTANCE.debugInvisibleBlocks;
    }

    public static void syncConfig(Configuration config)
    {
        INSTANCE.debugModels = config.getBoolean("debugModels", CATEGORY, false, Reference.DEBUG_MODELS);
        INSTANCE.debugInvisibleBlocks = config.getBoolean("debugInvisibleBlocks", CATEGORY, false, Reference.DEBUG_INVISIBLE_BLOCKS);

        Shearing.syncConfig(config);
        Behaviours.syncConfig(config);
    }

    public enum Shearing
    {
        INSTANCE;

        public static final String CATEGORY = Configuration.CATEGORY_GENERAL + ".shearing";

        private ShearBehaviour shearBehaviour = ShearBehaviour.RevertSheep;

        public static ShearBehaviour getBehaviour() { return INSTANCE.shearBehaviour; }

        private static void syncConfig(Configuration config) {

            final String[] validNames = new String[ShearBehaviour.values().length];
            for (int i = 0; i < ShearBehaviour.values().length; i++)
            {
                validNames[i] = ShearBehaviour.values()[i].name();
            }

            final String behaviour = config.getString("behaviour", CATEGORY, "RevertSheep", Reference.BEHAVIOUR_COMMENT, validNames);
            INSTANCE.shearBehaviour = ShearBehaviour.valueOf(behaviour);
        }
    }

    public enum Behaviours
    {
        INSTANCE;

        public static final String CATEGORY = Configuration.CATEGORY_GENERAL + ".behaviours";

        private boolean allowGlowstone = false;
        private boolean allowRedstone = true;
        private boolean allowBookshelf = true;
        private boolean allowCactus = true;
        private boolean allowTNT = true;
        private boolean allowFireDamage = true;
        private boolean allowAutoCrafting = true;


        public static boolean allowRedstone() {
            return INSTANCE.allowRedstone;
        }
        public static boolean allowBookshelf() {
            return INSTANCE.allowBookshelf;
        }
        public static boolean allowGlowstone() {
            return INSTANCE.allowGlowstone;
        }
        public static boolean allowCactus() {
            return INSTANCE.allowCactus;
        }
        public static boolean allowTNT() {
            return INSTANCE.allowTNT;
        }
        public static boolean allowFireDamage() {
            return INSTANCE.allowFireDamage;
        }
        public static boolean allowAutoCrafting() { return INSTANCE.allowAutoCrafting; }


        private static void syncConfig(Configuration config) {
            INSTANCE.allowRedstone = config.getBoolean("allowRedstone", CATEGORY, true, Reference.ALLOW_REDSTONE_COMMENT);
            INSTANCE.allowBookshelf = config.getBoolean("allowBookshelf", CATEGORY, true, Reference.ALLOW_BOOKSHELF_COMMENT);
            INSTANCE.allowGlowstone = config.getBoolean("allowGlowstone", CATEGORY, false, Reference.ALLOW_GLOWSTONE_COMMENT);
            INSTANCE.allowCactus = config.getBoolean("allowCactus", CATEGORY, true, Reference.ALLOW_CACTUS_COMMENT);
            INSTANCE.allowTNT = config.getBoolean("allowTNT", CATEGORY, true, Reference.ALLOW_TNT_COMMENT);
            INSTANCE.allowFireDamage = config.getBoolean("allowFireDamage", CATEGORY, true, Reference.ALLOW_FIRE_DAMAGE_COMMENT);
            INSTANCE.allowAutoCrafting = config.getBoolean("allowAutoCrafting", CATEGORY, true, Reference.ALLOW_AUTO_CRAFTING);
        }
    }

}
