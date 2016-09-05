package com.github.atomicblom.shearmadness.api;

import net.minecraft.block.Block;
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

    public static Boolean isStackForBlock(ItemStack itemStack, Block block, int meta) {
        return isStackForBlock(itemStack, block) && itemStack.getItemDamage() == meta;
    }
}