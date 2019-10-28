package com.github.atomicblom.shearmadness.utility;

import com.github.atomicblom.shearmadness.block.InvisibleBlock;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(CommonReference.MOD_ID)
public final class BlockLibrary {

    public static final InvisibleBlock invisible_redstone;
    public static final InvisibleBlock invisible_glowstone;
    public static final InvisibleBlock invisible_bookshelf;

    static {
        invisible_redstone = null;
        invisible_glowstone = null;
        invisible_bookshelf = null;
    }
}