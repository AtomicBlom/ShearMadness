package com.github.atomicblom.shearmadness.container;

import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;

public class ContainerEnchantmentSheep extends ContainerEnchantment
{
    private final EntityLiving entity;

    public ContainerEnchantmentSheep(InventoryPlayer playerInventory, World worldIn, EntityLiving entity)
    {
        super(playerInventory, worldIn, entity.getPosition());
        this.entity = entity;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (!entity.hasCapability(CapabilityProvider.CHISELED_SHEEP, null)) {
            return false;
        }
        final IChiseledSheepCapability capability = entity.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        final Item item = capability.getChiselItemStack().getItem();
        if (!(item instanceof ItemBlock) || ((ItemBlock) item).block != Blocks.ENCHANTING_TABLE) {
            return false;
        }

        return playerIn.getDistanceSq(entity.getPosition()) <= 64.0D;
    }
}
