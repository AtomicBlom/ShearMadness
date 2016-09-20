package com.github.atomicblom.shearmadness.proxy;

import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessVariationEvent;
import com.github.atomicblom.shearmadness.api.VariationRegistry;
import com.github.atomicblom.shearmadness.networking.CheckSheepChiseledRequestMessage;
import com.github.atomicblom.shearmadness.library.KeyboardLibrary;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import static com.github.atomicblom.shearmadness.ShearMadnessMod.CHANNEL;

@SuppressWarnings({"MethodMayBeStatic", "unused"})
public class ClientForgeEventProxy extends CommonForgeEventProxy
{
    @Override
    public void fireRegistryEvent() {
        MinecraftForge.EVENT_BUS.post(new RegisterShearMadnessVariationEvent(VariationRegistry.INSTANCE));
        super.fireRegistryEvent();
    }

    @Override
    public void initializeKeyboard() {
        //FIXME: Configuration Setting
        KeyboardLibrary.configureBreeding = new KeyBinding("Configure Sheep Breeding", Keyboard.KEY_P, Reference.MOD_ID);
        ClientRegistry.registerKeyBinding(KeyboardLibrary.configureBreeding);
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        final Entity entity = event.getEntity();
        if (entity instanceof EntitySheep)
        {
            CHANNEL.sendToServer(new CheckSheepChiseledRequestMessage(entity));
        }
    }
}
