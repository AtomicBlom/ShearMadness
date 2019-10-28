package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.inventory.container.NotAChiselContainer;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ContainerTypeEventHandler {
    @SubscribeEvent
    public static void registerContainerTypes(final RegistryEvent.Register<ContainerType<?>> containerTypeEvent) {
        IForgeRegistry<ContainerType<?>> registry = containerTypeEvent.getRegistry();
        registry.register(new ContainerType<>((IContainerFactory<NotAChiselContainer>) NotAChiselContainer::new).setRegistryName(Reference.NOT_A_CHISEL_CONTAINER_TYPE));
    }
}
