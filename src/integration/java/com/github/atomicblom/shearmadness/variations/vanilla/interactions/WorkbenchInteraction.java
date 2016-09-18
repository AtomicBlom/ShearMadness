package com.github.atomicblom.shearmadness.variations.vanilla.interactions;

import com.github.atomicblom.shearmadness.variations.vanilla.container.ContainerWorkbenchSheep;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class WorkbenchInteraction implements IInteractionObject
{
    private final World world;
    private final EntityLiving entity;

    public WorkbenchInteraction(World world, EntityLiving entity)
    {
        this.world = world;
        this.entity = entity;
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    @Override
    public String getName()
    {
        return null;
    }

    /**
     * Returns true if this thing is named
     */
    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentTranslation(Blocks.CRAFTING_TABLE.getUnlocalizedName() + ".name");
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerWorkbenchSheep(playerInventory, world, entity);
    }

    @Override
    public String getGuiID()
    {
        return "minecraft:crafting_table";
    }
}
