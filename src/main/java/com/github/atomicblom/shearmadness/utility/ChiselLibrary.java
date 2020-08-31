package com.github.atomicblom.shearmadness.utility;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("chisel")
public class ChiselLibrary
{
    public static final Item chisel_iron;
    public static final Item chisel_diamond;
    public static final Item chisel_hitech;

    public static final Block technical;
    public static final Block technical1;

    public static final Block marbleextra;

    static {
        chisel_iron = null;
        chisel_diamond = null;
        chisel_hitech = null;

        technical = null;
        technical1 = null;

        marbleextra = null;
    }
}
