package com.github.atomicblom.shearmadness.utility;

import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@SuppressWarnings("ALL")
@ObjectHolder(CommonReference.MOD_ID)
public final class ItemLibrary
{

    public static final ItemBlock invisible_redstone;
    public static final ItemBlock invisible_glowstone;
    public static final ItemBlock invisible_bookshelf;

    static {
        invisible_redstone = null;
        invisible_glowstone = null;
        invisible_bookshelf = null;
    }
}