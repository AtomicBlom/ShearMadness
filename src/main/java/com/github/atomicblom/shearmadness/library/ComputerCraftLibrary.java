package com.github.atomicblom.shearmadness.library;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder("computercraft")
public class ComputerCraftLibrary {
    @GameRegistry.ObjectHolder("pocketComputer")
    public static final Item portable_computer;

    @GameRegistry.ObjectHolder("CC-Computer")
    public static final Item computer;

    static {
        portable_computer = null;
        computer = null;
    }
}
