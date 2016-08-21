package com.github.atomicblom.chiselsheep;

import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.configuration.Settings;
import com.github.atomicblom.chiselsheep.utility.ItemStackUtils;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;

/**
 * Created by codew on 21/08/2016.
 */
public class Shearing
{
    static void ShearSheep(ItemStack itemStack, EntitySheep sheep, IChiseledSheepCapability capability)
    {
        if (Settings.Shearing.getBehaviour() == Settings.ShearBehaviour.CannotShear) {
            //TODO: Play shear cancelled Sound.
            return;
        }

        sheep.setSheared(true);

        //TODO: Add support for facade drop.
        //Select the item to drop.
        final ItemStack itemToDrop = capability.getChiselItemStack().copy();
        itemToDrop.stackSize = 1;

        ItemStackUtils.dropItem(sheep, itemToDrop);

        sheep.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        itemStack.damageItem(1, sheep);
    }

}
