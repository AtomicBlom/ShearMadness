package com.github.atomicblom.shearmadness.gui;

import com.github.atomicblom.shearmadness.gui.container.ConfigureBreedingContainer;
import com.github.atomicblom.shearmadness.gui.ui.ConfigureBreedingGui;
import com.github.atomicblom.shearmadness.library.GuiLibrary;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public enum GuiHandler implements IGuiHandler
{
    INSTANCE;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        switch(GuiLibrary.fromId(id))
        {
            case CONFIGURE_BREEDING:
                return new ConfigureBreedingContainer(player.inventory);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        switch(GuiLibrary.fromId(id))
        {
            case CONFIGURE_BREEDING:
                return new ConfigureBreedingGui(player.inventory);
        }
        return null;
    }
}

