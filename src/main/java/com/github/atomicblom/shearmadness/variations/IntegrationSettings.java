package com.github.atomicblom.shearmadness.variations;

import net.minecraftforge.common.ForgeConfigSpec;

import javax.swing.*;

//FIXME: Move this to individual integrations
public class IntegrationSettings {
    public enum ChanceCubes {
        INSTANCE;

        ForgeConfigSpec.BooleanValue enabled;
        ForgeConfigSpec.IntValue distance;

        public static boolean enabled() {
            return INSTANCE.enabled.get();
        }

        public static int distance() {
            return INSTANCE.distance.get();
        }
    }

    public enum Vanilla {
        INSTANCE;

        ForgeConfigSpec.BooleanValue allowRedstone;
        ForgeConfigSpec.BooleanValue allowBookshelf;
        ForgeConfigSpec.BooleanValue allowGlowstone;
        ForgeConfigSpec.BooleanValue allowCactus;
        ForgeConfigSpec.BooleanValue allowTNT;
        ForgeConfigSpec.BooleanValue allowAutoCrafting;
        ForgeConfigSpec.BooleanValue allowFireDamage;

        public static boolean allowRedstone() {
            return INSTANCE.allowRedstone.get();
        }
        public static boolean allowBookshelf() {
            return INSTANCE.allowBookshelf.get();
        }
        public static boolean allowGlowstone() {
            return INSTANCE.allowGlowstone.get();
        }
        public static boolean allowCactus() {
            return INSTANCE.allowCactus.get();
        }
        public static boolean allowTNT() {
            return INSTANCE.allowTNT.get();
        }
        public static boolean allowFireDamage() {
            return INSTANCE.allowFireDamage.get();
        }
        public static boolean allowAutoCrafting() {
            return INSTANCE.allowAutoCrafting.get();
        }
    }
}
