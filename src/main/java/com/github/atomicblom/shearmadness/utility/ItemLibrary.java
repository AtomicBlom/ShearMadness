package com.github.atomicblom.shearmadness.utility;

import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(CommonReference.MOD_ID)
public class ItemLibrary {
    public static final Item not_a_chisel;

    static {
        not_a_chisel = null;
    }
}
