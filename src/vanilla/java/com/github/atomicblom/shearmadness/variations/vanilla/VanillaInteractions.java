package com.github.atomicblom.shearmadness.variations.vanilla;

import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.events.ShearMadnessSpecialInteractionEvent;
import com.github.atomicblom.shearmadness.api.CommonReference;
import com.github.atomicblom.shearmadness.variations.vanilla.behaviour.NoteBlockBehaviour;
import com.github.atomicblom.shearmadness.variations.vanilla.interactions.AnvilInteraction;
import com.github.atomicblom.shearmadness.variations.vanilla.interactions.EnchantmentInteraction;
import com.github.atomicblom.shearmadness.variations.vanilla.interactions.WorkbenchInteraction;
import net.minecraft.block.Blocks;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CommonReference.MOD_ID)
public class VanillaInteractions {
    @SubscribeEvent
    public static void onInteraction(ShearMadnessSpecialInteractionEvent event) {
        final ItemStack itemStack = event.getItemStack();
        final SheepEntity sheep = event.getSheep();

        if (ItemStackHelper.isStackForBlock(itemStack, Blocks.ANVIL)) {
            NetworkHooks.openGui(event.getPlayer(), new AnvilInteraction(sheep));
        }

        if (ItemStackHelper.isStackForBlock(itemStack, Blocks.CRAFTING_TABLE)) {
            NetworkHooks.openGui(event.getPlayer(), new WorkbenchInteraction(sheep));
        }
        if (ItemStackHelper.isStackForBlock(itemStack, Blocks.ENCHANTING_TABLE)) {
            NetworkHooks.openGui(event.getPlayer(), new EnchantmentInteraction(sheep));
        }
        if (ItemStackHelper.isStackForBlock(itemStack, Blocks.NOTE_BLOCK)) {
            NoteBlockBehaviour.tuneNoteBlockSheep(event.getSheep());
        }
    }
}
