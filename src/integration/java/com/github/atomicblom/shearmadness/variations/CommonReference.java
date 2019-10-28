package com.github.atomicblom.shearmadness.variations;

/**
 * Created by codew on 4/10/2016.
 */
public final class CommonReference {
    public static final String MOD_ID = "shearmadness";
    public static final String MOD_NAME = "Shear Madness";
    public static final String VERSION = "@MOD_VERSION@";

    private CommonReference() {}

    public static class IMCMethods {
        public static final String REGISTER_BEHAVIOURS = "register_behaviours";
        public static final String REGISTER_VARIATIONS = "register_variations";

        private IMCMethods() {}
    }
}
