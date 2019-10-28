package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.block.InvisibleBookshelfBlock;
import com.github.atomicblom.shearmadness.block.InvisibleGlowstoneBlock;
import com.github.atomicblom.shearmadness.block.InvisibleRedstoneBlock;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegistrationEventHandler
{
    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
    {
        final IForgeRegistry<Block> registry = event.getRegistry();

        registry.register(
                new InvisibleRedstoneBlock(Block.Properties.create(Material.AIR))
                        .setRegistryName(Reference.Blocks.InvisibleRedstone)
        );

        registry.register(
                new InvisibleGlowstoneBlock(Block.Properties.create(Material.AIR))
                        .setRegistryName(Reference.Blocks.InvisibleGlowstone)
        );

        registry.register(
                new InvisibleBookshelfBlock(Block.Properties.create(Material.AIR))
                        .setRegistryName(Reference.Blocks.InvisibleBookshelf)
        );
    }
}
