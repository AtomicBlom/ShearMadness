package com.github.atomicblom.shearmadness.variations.vanilla;

import com.github.atomicblom.shearmadness.api.IBehaviourRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.behaviour.DamageBehaviour;
import com.github.atomicblom.shearmadness.api.behaviour.ExplosiveBehaviour;
import com.github.atomicblom.shearmadness.api.behaviour.PlaceInvisibleBlockBehaviour;
import com.github.atomicblom.shearmadness.api.events.IRegisterShearMadnessBehaviours;
import com.github.atomicblom.shearmadness.api.events.ShearMadnessSheepKilledEvent;
import com.github.atomicblom.shearmadness.utility.BlockLibrary;
import com.github.atomicblom.shearmadness.api.CommonReference;
import com.github.atomicblom.shearmadness.variations.IntegrationSettings;
import com.github.atomicblom.shearmadness.variations.vanilla.behaviour.AutoCraftGoal;
import com.github.atomicblom.shearmadness.variations.vanilla.behaviour.FollowAutoCraftItems;
import com.github.atomicblom.shearmadness.variations.vanilla.behaviour.NoteBlockBehaviour;
import net.minecraft.block.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@SuppressWarnings({"MethodMayBeStatic", "UnnecessarilyQualifiedInnerClassAccess"})
@Mod.EventBusSubscriber(modid = CommonReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VanillaBehaviours
{
    @SubscribeEvent
    public static void onProcessIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(CommonReference.MOD_ID, CommonReference.IMCMethods.REGISTER_BEHAVIOURS, () -> (IRegisterShearMadnessBehaviours) VanillaBehaviours::registerBehaviours);
    }

    //FIXME: May as well send these as individual IMC messages rather than a big batch.
    private static void registerBehaviours(IBehaviourRegistry registry) {
        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.CACTUS),
                entity -> new DamageBehaviour(entity, IntegrationSettings.Vanilla::allowCactus, DamageSource.CACTUS)
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.MAGMA_BLOCK),
                entity -> new DamageBehaviour(entity, IntegrationSettings.Vanilla::allowFireDamage, DamageSource.HOT_FLOOR)
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.REDSTONE_BLOCK),
                entity -> new PlaceInvisibleBlockBehaviour(entity, IntegrationSettings.Vanilla::allowRedstone, BlockLibrary.invisible_redstone.getDefaultState())
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.GLOWSTONE),
                entity -> new PlaceInvisibleBlockBehaviour(entity, IntegrationSettings.Vanilla::allowGlowstone, BlockLibrary.invisible_glowstone.getDefaultState())
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.BOOKSHELF),
                entity -> new PlaceInvisibleBlockBehaviour(entity, IntegrationSettings.Vanilla::allowBookshelf, BlockLibrary.invisible_bookshelf.getDefaultState())
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.TNT),
                sheep -> new ExplosiveBehaviour(sheep, IntegrationSettings.Vanilla::allowTNT)
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.NOTE_BLOCK),
                sheep -> new NoteBlockBehaviour(sheep, () -> true)
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.CRAFTING_TABLE),
                sheep -> new FollowAutoCraftItems(sheep, IntegrationSettings.Vanilla::allowAutoCrafting)
        );

        registry.registerGoal(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.CRAFTING_TABLE),
                sheep -> new AutoCraftGoal(sheep, IntegrationSettings.Vanilla::allowAutoCrafting)
        );
    }

    @SubscribeEvent
    public static void onSheepKilled(ShearMadnessSheepKilledEvent event) {
        if (ItemStackHelper.isStackForBlock(event.getChiselItemStack(), Blocks.TNT) && event.getSource().isExplosion()) {
            event.noDrops();
        }
    }
}
