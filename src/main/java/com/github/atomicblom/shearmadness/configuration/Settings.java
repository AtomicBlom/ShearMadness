package com.github.atomicblom.shearmadness.configuration;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public enum Settings
{
    INSTANCE;

    ForgeConfigSpec.BooleanValue debugModels;
    ForgeConfigSpec.BooleanValue debugInvisibleBlocks;

    public static boolean debugModels() {
        return INSTANCE.debugModels.get();
    }

    public static boolean debugInvisibleBlocks() {
        return INSTANCE.debugInvisibleBlocks.get();
    }

    public enum Shearing {
        INSTANCE;

        ForgeConfigSpec.EnumValue<ShearBehaviour> shearBehaviour;

        public static ShearBehaviour getBehaviour() {
            return INSTANCE.shearBehaviour.get();
        }
    }

    public enum Behaviours {
        INSTANCE;

        ForgeConfigSpec.EnumValue<BreedingBehaviour> breedingBehaviour;

        public static BreedingBehaviour getBreedingBehaviour() {
            return INSTANCE.breedingBehaviour.get();
        }
    }
}
