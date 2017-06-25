package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.block.InvisibleBookshelfBlock;
import com.github.atomicblom.shearmadness.block.InvisibleGlowstoneBlock;
import com.github.atomicblom.shearmadness.block.InvisibleRedstoneBlock;
import com.github.atomicblom.shearmadness.utility.Localization;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class BlockRegistrationEventHandler
{
	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();
		registry.register(
				configureBlock(
						new InvisibleRedstoneBlock(),
						Reference.Blocks.InvisibleRedstone
				)
		);
		registry.register(
				configureBlock(
						new InvisibleGlowstoneBlock(),
						Reference.Blocks.InvisibleGlowstone
				)
		);
		registry.register(
				configureBlock(
						new InvisibleBookshelfBlock(),
						Reference.Blocks.InvisibleBookshelf
				)
		);
	}

	private static Block configureBlock(Block block, ResourceLocation name) {
		return block.setRegistryName(name)
				.setUnlocalizedName(Localization.getUnlocalizedNameFor(block));
	}
}
