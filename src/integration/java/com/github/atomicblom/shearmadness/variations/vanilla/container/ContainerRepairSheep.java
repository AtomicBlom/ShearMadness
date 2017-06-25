package com.github.atomicblom.shearmadness.variations.vanilla.container;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class ContainerRepairSheep extends ContainerRepair
{
    private final EntityLiving entity;

    public ContainerRepairSheep(InventoryPlayer playerInventory, World worldIn, EntityPlayer player, EntityLiving entity)
    {
        super(playerInventory, worldIn, entity.getPosition(), player);
        this.entity = entity;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (!entity.hasCapability(Capability.CHISELED_SHEEP, null)) {
            return false;
        }
        final IChiseledSheepCapability capability = entity.getCapability(Capability.CHISELED_SHEEP, null);
        assert capability != null;
        final Item item = capability.getChiselItemStack().getItem();
        if (!(item instanceof ItemBlock) || !Objects.equals(((ItemBlock) item).getBlock(), Blocks.ANVIL)) {
            return false;
        }

        return playerIn.getDistanceSq(entity.getPosition()) <= 64.0D;
    }
}
