package com.github.atomicblom.shearmadness.utility;

import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@SuppressWarnings("ALL")
@ObjectHolder(CommonReference.MOD_ID)
public class SoundLibrary
{
    public static final SoundEvent sheepchiseled;

    static {
        sheepchiseled = null;
    }
}
