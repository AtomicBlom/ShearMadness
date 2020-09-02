package com.github.atomicblom.shearmadness.variations.silly;

import com.github.atomicblom.shearmadness.api.CommonReference;
import net.minecraft.util.ResourceLocation;

public class Reference {
    public static class Textures {
        public static final ResourceLocation CHICKEN_NUGGETS = resource("chicken_nuggets");
        public static final ResourceLocation CHICKEN_WINGLETS = resource("chicken_winglets");
    }

    private static ResourceLocation resource(String resourceName) {
        return new ResourceLocation(CommonReference.MOD_ID, resourceName);
    }
}
