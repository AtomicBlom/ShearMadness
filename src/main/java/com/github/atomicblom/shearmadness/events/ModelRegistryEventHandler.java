package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.rendering.RenderChiselSheep;
import com.github.atomicblom.shearmadness.utility.ItemLibrary;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ModelRegistryEventHandler
{
	@SubscribeEvent
	public static void onModelRegistryReady(ModelRegistryEvent event) {
		registerInventoryModel(ItemLibrary.invisible_bookshelf);
		registerInventoryModel(ItemLibrary.invisible_glowstone);
		registerInventoryModel(ItemLibrary.invisible_redstone);

		final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		renderManager.entityRenderMap.remove(EntitySheep.class);
		renderManager.entityRenderMap.put(EntitySheep.class, new RenderChiselSheep(renderManager, new ModelSheep2(), 0.7f));
	}

	private static void registerInventoryModel(Item item) {
		ModelLoader.setCustomModelResourceLocation(
				item,
				0,
				new ModelResourceLocation(
						item.getRegistryName(),
						Reference.Blocks.NORMAL_VARIANT
				)
		);

	}
}
