package com.github.atomicblom.chiselsheep;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@SuppressWarnings("ALL")
@ObjectHolder("chisel")
public class ChiselLibrary
{
    public static final Item chisel_iron = null;
    public static final Item chisel_diamond = null;
    public static final Item chisel_hitech = null;

    public static boolean isChisel(Item item)
    {
        return item == chisel_iron || item == chisel_diamond || item == chisel_hitech;
    }
}
