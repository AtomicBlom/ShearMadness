package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.items.NotAChiselItem;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemRegistrationEventHandler {
    @SubscribeEvent
    public static void onItemRegistryReady(final RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(new NotAChiselItem(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1)).setRegistryName(Reference.Items.NOT_A_CHISEL));
    }
}
