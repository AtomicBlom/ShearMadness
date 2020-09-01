package com.github.atomicblom.shearmadness.variations.vanilla;

import com.github.atomicblom.shearmadness.api.CommonReference;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.events.ShearMadnessSheepKilledEvent;
import net.minecraft.block.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CommonReference.MOD_ID)
public class ShearMadnessEvents {
    @SubscribeEvent
    public static void onSheepKilled(ShearMadnessSheepKilledEvent event) {
        if (ItemStackHelper.isStackForBlock(event.getChiselItemStack(), Blocks.TNT) && event.getSource().isExplosion()) {
            event.noDrops();
        }
    }
}
