package com.github.atomicblom.shearmadness.proxy;

import com.github.atomicblom.shearmadness.utility.ChiselLibrary;
import com.github.atomicblom.shearmadness.Chiseling;
import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.Shearing;
import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("MethodMayBeStatic")
public class CommonProxy implements IProxy
{
    @Override
    public void registerRenderers()
    {

    }

    @SuppressWarnings({"ConstantConditions", "MethodWithMoreThanThreeNegations"})
    @SubscribeEvent
    public void onPlayerInteractionWithEntity(EntityInteract event)
    {
        //Process for shearing a sheep
        if (event.getWorld().isRemote) return;
        if (!(event.getTarget() instanceof EntitySheep)) return;
        final ItemStack itemStack = event.getItemStack();
        if (itemStack == null || !(itemStack.getItem() instanceof ItemShears)) return;

        final EntitySheep sheep = (EntitySheep) event.getTarget();
        if (!sheep.isShearable(itemStack, event.getWorld(), event.getPos())) return;

        final IChiseledSheepCapability capability = sheep.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        if (capability == null) return;
        if (!capability.isChiseled()) return;

        //Ok, we have a chiseled sheep, cancel vanilla.
        event.setCanceled(true);

        Shearing.ShearSheep(itemStack, sheep, capability);
    }

    @SuppressWarnings("BooleanVariableAlwaysNegated")
    @SubscribeEvent
    public void onAttack(AttackEntityEvent event)
    {
        //Process for chiseling a sheep
        final Entity sheep = event.getTarget();
        if (sheep == null || !(sheep instanceof EntitySheep)) return;

        final EntityPlayer entityPlayer = event.getEntityPlayer();
        ItemStack activeStack = entityPlayer.inventory.getCurrentItem();
        boolean attackedWithChisel = false;
        if (activeStack != null && ChiselLibrary.isChisel(activeStack.getItem()))
        {
            attackedWithChisel = true;
        } else
        {
            activeStack = entityPlayer.inventory.offHandInventory[0];
            if (activeStack != null && ChiselLibrary.isChisel(activeStack.getItem()))
            {
                attackedWithChisel = true;
            }
        }
        if (!attackedWithChisel)
        {
            return;
        }

        event.setCanceled(true);

        Chiseling.chiselSheep(sheep, entityPlayer, activeStack);
    }

    @SubscribeEvent
    public void onCapabilityAttaching(AttachCapabilitiesEvent.Entity event)
    {
        if (event.getEntity().getClass().equals(EntitySheep.class))
        {
            event.addCapability(new ResourceLocation(Reference.MOD_ID, "chiseledSheep"), new CapabilityProvider());
        }
    }
}
