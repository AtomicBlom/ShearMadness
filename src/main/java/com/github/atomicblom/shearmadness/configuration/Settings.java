package com.github.atomicblom.shearmadness.configuration;

import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Type;

@SuppressWarnings({"StaticNonFinalField", "PublicField", "InstantiationOfUtilityClass", "unused"})
@Config(modid = CommonReference.MOD_ID, type = Type.INSTANCE, name = CommonReference.MOD_ID + "6")
public final class Settings
{
    //Categories
    public static final ShearingSettings Shearing = new ShearingSettings();
    public static final BehaviourSettings Behaviours = new BehaviourSettings();

    //Properties
    @Comment(Reference.DEBUG_MODELS)
    public static boolean debugModels = false;
    @Comment(Reference.DEBUG_INVISIBLE_BLOCKS)
    public static boolean debugInvisibleBlocks = false;

    @SuppressWarnings({"WeakerAccess", "UtilityClass"})
    public static final class ShearingSettings
    {
        @Comment(Reference.BEHAVIOUR_COMMENT)
        public static ShearBehaviour shearBehaviour = ShearBehaviour.RevertSheep;

        private ShearingSettings() {}
    }

    @SuppressWarnings({"WeakerAccess", "UtilityClass"})
    public static final class BehaviourSettings
    {
        @Comment(Reference.ALLOW_GLOWSTONE_COMMENT)
        public static boolean allowGlowstone = false;
        @Comment(Reference.ALLOW_REDSTONE_COMMENT)
        public static boolean allowRedstone = true;
        @Comment(Reference.ALLOW_BOOKSHELF_COMMENT)
        public static boolean allowBookshelf = true;
        @Comment(Reference.ALLOW_CACTUS_COMMENT)
        public static boolean allowCactus = true;
        @Comment(Reference.ALLOW_TNT_COMMENT)
        public static boolean allowTNT = true;
        @Comment(Reference.ALLOW_FIRE_DAMAGE_COMMENT)
        public static boolean allowFireDamage = true;

        private BehaviourSettings() {}
    }

    private Settings() {}
}
