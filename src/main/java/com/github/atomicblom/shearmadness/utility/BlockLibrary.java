package com.github.atomicblom.shearmadness.utility;

import com.github.atomicblom.shearmadness.block.InvisibleBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@SuppressWarnings("ALL")
@ObjectHolder(Reference.MOD_ID)
public final class BlockLibrary {

    public static final InvisibleBlock invisibleRedstone;
    public static final InvisibleBlock invisibleGlowstone;
    public static final InvisibleBlock invisibleBookshelf;

    static {
        invisibleRedstone = null;
        invisibleGlowstone = null;
        invisibleBookshelf = null;
    }
}