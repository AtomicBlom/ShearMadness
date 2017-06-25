package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.block.InvisibleBookshelfBlock;
import com.github.atomicblom.shearmadness.block.InvisibleGlowstoneBlock;
import com.github.atomicblom.shearmadness.block.InvisibleRedstoneBlock;
import com.github.atomicblom.shearmadness.utility.BlockLibrary;
import com.github.atomicblom.shearmadness.utility.Localization;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class ItemRegistrationEventHandler
{
	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		registry.register(configureItemBlock(new ItemBlock(BlockLibrary.invisible_redstone)));
		registry.register(configureItemBlock(new ItemBlock(BlockLibrary.invisible_glowstone)));
		registry.register(configureItemBlock(new ItemBlock(BlockLibrary.invisible_bookshelf)));
	}

	private static Item configureItemBlock(ItemBlock itemBlock)
	{
		return itemBlock
				.setRegistryName(itemBlock.getBlock().getRegistryName())
				.setCreativeTab(Reference.CreativeTab)
				.setUnlocalizedName(itemBlock.getBlock().getUnlocalizedName());
	}
}
