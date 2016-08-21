package com.github.atomicblom.chiselsheep.utility;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import java.util.Random;

/**
 * Created by codew on 21/08/2016.
 */
public class ItemStackUtils
{

    public static void dropItem(Entity nearEntity, ItemStack itemStack)
    {
        final EntityItem item = nearEntity.entityDropItem(itemStack, 1.0F);
        final Random rand = new Random();
        item.motionY += rand.nextFloat() * 0.05F;
        item.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
        item.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
    }
}
