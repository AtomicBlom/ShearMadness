package com.github.atomicblom.shearmadness.variations.vanilla;

import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.events.ShearMadnessSpecialInteractionEvent;
import com.github.atomicblom.shearmadness.variations.vanilla.behaviour.NoteBlockBehaviour;
import com.github.atomicblom.shearmadness.variations.vanilla.interactions.AnvilInteraction;
import com.github.atomicblom.shearmadness.variations.vanilla.interactions.EnchantmentInteraction;
import com.github.atomicblom.shearmadness.variations.vanilla.interactions.WorkbenchInteraction;
import net.minecraft.block.Blocks;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class VanillaInteractions {

    @SubscribeEvent
    public static void onInteraction(ShearMadnessSpecialInteractionEvent event) {
        final ItemStack itemStack = event.getItemStack();
        final SheepEntity sheep = event.getSheep();
        final PlayerEntity player = event.getPlayer();
        if (ItemStackHelper.isStackForBlock(itemStack, Blocks.ANVIL)) {
            player.displayGui(new AnvilInteraction(event.getWorld(), sheep));
        }
        if (ItemStackHelper.isStackForBlock(itemStack, Blocks.CRAFTING_TABLE)) {
            player.displayGui(new WorkbenchInteraction(event.getWorld(), sheep));
        }
        if (ItemStackHelper.isStackForBlock(itemStack, Blocks.ENCHANTING_TABLE)) {
            player.displayGui(new EnchantmentInteraction(event.getWorld(), sheep));
        }
        if (ItemStackHelper.isStackForBlock(itemStack, Blocks.NOTE_BLOCK)) {
            NoteBlockBehaviour.tuneNoteBlockSheep(event.getSheep());
        }
    }
}
