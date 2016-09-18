package com.github.atomicblom.shearmadness.variations.vanilla;

import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.events.ShearMadnessSpecialInteractionEvent;
import com.github.atomicblom.shearmadness.variations.vanilla.interactions.AnvilInteraction;
import com.github.atomicblom.shearmadness.variations.vanilla.interactions.EnchantmentInteraction;
import com.github.atomicblom.shearmadness.variations.vanilla.interactions.WorkbenchInteraction;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class VanillaInteractions {

    @SubscribeEvent
    public void onInteraction(ShearMadnessSpecialInteractionEvent event) {
        final ItemStack itemStack = event.getItemStack();
        final EntitySheep sheep = event.getSheep();
        final EntityPlayer player = event.getPlayer();
        if (ItemStackHelper.isStackForBlock(itemStack, Blocks.ANVIL)) {
            player.displayGui(new AnvilInteraction(event.getWorld(), sheep));
        }
        if (ItemStackHelper.isStackForBlock(itemStack, Blocks.CRAFTING_TABLE)) {
            player.displayGui(new WorkbenchInteraction(event.getWorld(), sheep));
        }
        if (ItemStackHelper.isStackForBlock(itemStack, Blocks.ENCHANTING_TABLE)) {
            player.displayGui(new EnchantmentInteraction(event.getWorld(), sheep));
        }
    }
}
