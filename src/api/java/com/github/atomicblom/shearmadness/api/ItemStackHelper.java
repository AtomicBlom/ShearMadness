package com.github.atomicblom.shearmadness.api;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemStackHelper
{


    public static boolean isStackForBlock(ItemStack itemStack, Block block)
    {
        final Item item = itemStack.getItem();
        if (item instanceof ItemBlock) {
            if (((ItemBlock) item).block == block) {
                return true;
            }
        }
        return false;
    }
}
