package com.github.atomicblom.shearmadness.eventhandling;

import com.github.atomicblom.shearmadness.ShearMadnessMod;
import com.github.atomicblom.shearmadness.library.GuiLibrary;
import com.github.atomicblom.shearmadness.library.KeyboardLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(Side.CLIENT)
public class Keyboard {

    @SubscribeEvent()
    @SideOnly(Side.CLIENT)
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        final KeyBinding keyBind = KeyboardLibrary.configureBreeding;

        if (keyBind.isPressed()) {
            final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
            thePlayer.openGui(ShearMadnessMod.instance, GuiLibrary.CONFIGURE_BREEDING.getID(), thePlayer.worldObj, (int)thePlayer.posX, (int)thePlayer.posY, (int)thePlayer.posZ);
        }
    }
}
