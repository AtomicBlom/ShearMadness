package com.github.atomicblom.shearmadness.library;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@SuppressWarnings("ALL")
@ObjectHolder("chisel")
public class ChiselLibrary
{
    public static final Item chisel_iron = null;
    public static final Item chisel_diamond = null;
    public static final Item chisel_hitech = null;

    public static final Block technical = null;
    public static final Block technical1 = null;

    public static final Block marbleextra = null;

    public static boolean isChisel(Item item)
    {
        return item == chisel_iron || item == chisel_diamond || item == chisel_hitech;
    }
}
