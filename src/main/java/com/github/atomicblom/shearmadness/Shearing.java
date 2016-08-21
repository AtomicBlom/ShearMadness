package com.github.atomicblom.shearmadness;

import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.configuration.Settings;
import com.github.atomicblom.shearmadness.configuration.ShearBehaviour;
import com.github.atomicblom.shearmadness.networking.SheepChiseledMessage;
import com.github.atomicblom.shearmadness.utility.ItemStackUtils;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;

import static com.github.atomicblom.shearmadness.ChiselSheepMod.CHANNEL;

/**
 * Created by codew on 21/08/2016.
 */
public class Shearing
{
    public static void ShearSheep(ItemStack itemStack, EntitySheep sheep, IChiseledSheepCapability capability)
    {
        if (Settings.Shearing.getBehaviour() == ShearBehaviour.CannotShear) {
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

        if (Settings.Shearing.getBehaviour() == ShearBehaviour.RevertSheep) {
            capability.setChiseled(false);
            CHANNEL.sendToAll(new SheepChiseledMessage(sheep));
        }
    }

}
