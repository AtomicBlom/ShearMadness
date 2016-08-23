package com.github.atomicblom.shearmadness.utility;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;

public class Reference
{
    public static final String MOD_ID = "shearmadness";
    public static final String MOD_NAME = "Shear Madness";
    public static final String VERSION = "@MOD_VERSION@";
    public static final CreativeTabs CreativeTab = CreativeTabs.BUILDING_BLOCKS;
    public static final String MOD_GUI_FACTORY = "com.github.atomicblom.shearmadness.configuration.client.ModGuiFactory";
    public static final String BEHAVIOUR_COMMENT = "Sets the behaviour when a sheep is sheared\n" +
            "* RevertSheep - will change the sheep back to a normal sheep (default).\n" +
            "* ChiselFarm  - will allow the sheep to produce chiseled blocks (warning, this currently allows duping ores).\n" +
            "* CannotShear - You cannot shear the sheep while chiseled.\n";

    public static final class Blocks {
        public static final String NORMAL_VARIANT = "normal";

        public static final ResourceLocation InvisibleRedstone = new ResourceLocation(MOD_ID, "invisibleRedstone");
    }

}
