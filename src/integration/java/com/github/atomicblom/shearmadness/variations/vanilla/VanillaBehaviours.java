package com.github.atomicblom.shearmadness.variations.vanilla;

import com.github.atomicblom.shearmadness.api.IBehaviourRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.behaviour.DamageBehaviour;
import com.github.atomicblom.shearmadness.api.behaviour.ExplosiveBehaviour;
import com.github.atomicblom.shearmadness.api.behaviour.PlaceInvisibleBlockBehaviour;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessBehaviourEvent;
import com.github.atomicblom.shearmadness.configuration.Settings.Behaviours;
import com.github.atomicblom.shearmadness.utility.BlockLibrary;
import com.github.atomicblom.shearmadness.variations.vanilla.behaviour.FollowAutoCraftItems;
import com.github.atomicblom.shearmadness.variations.vanilla.behaviour.NoteBlockBehaviour;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings({"MethodMayBeStatic", "UnnecessarilyQualifiedInnerClassAccess"})
@Mod.EventBusSubscriber
public class VanillaBehaviours
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    @Optional.Method(modid = "shearmadness")
    public static void onShearMadnessRegisterBehaviours(RegisterShearMadnessBehaviourEvent event) {
        final IBehaviourRegistry registry = event.getRegistry();

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.CACTUS),
                entity -> new DamageBehaviour(entity, Behaviours::allowCactus, DamageSource.CACTUS)
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.MAGMA),
                entity -> new DamageBehaviour(entity, Behaviours::allowFireDamage, DamageSource.HOT_FLOOR)
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.REDSTONE_BLOCK),
                entity -> new PlaceInvisibleBlockBehaviour(entity, Behaviours::allowRedstone, BlockLibrary.invisible_redstone.getDefaultState())
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.GLOWSTONE),
                entity -> new PlaceInvisibleBlockBehaviour(entity, Behaviours::allowGlowstone, BlockLibrary.invisible_glowstone.getDefaultState())
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.BOOKSHELF),
                entity -> new PlaceInvisibleBlockBehaviour(entity, Behaviours::allowBookshelf, BlockLibrary.invisible_bookshelf.getDefaultState())
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.TNT),
                sheep -> new ExplosiveBehaviour(sheep, Behaviours::allowTNT)
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.NOTEBLOCK),
                sheep -> new NoteBlockBehaviour(sheep, () -> true)
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.CRAFTING_TABLE),
                FollowAutoCraftItems::new
        );
    }
}
