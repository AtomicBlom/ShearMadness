package com.github.atomicblom.chiselsheep.utility;

public class Reference
{
    public static final String MOD_ID = "chiselsheep";
    public static final String MOD_NAME = "Chisel Sheep";
    public static final String VERSION = "1.0";
    public static final String MOD_GUI_FACTORY = "com.github.atomicblom.chiselsheep.configuration.client.ModGuiFactory";
    public static final String BEHAVIOUR_COMMENT = "Sets the behaviour when a sheep is sheared\n" +
            "* RevertSheep - will change the sheep back to a normal sheep (default).\n" +
            "* ChiselFarm  - will allow the sheep to produce chiseled blocks (warning, this currently allows duping ores).\n" +
            "* CannotShear - You cannot shear the sheep while chiseled.\n";


}
