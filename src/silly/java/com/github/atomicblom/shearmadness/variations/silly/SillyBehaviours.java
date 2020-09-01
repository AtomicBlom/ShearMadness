package com.github.atomicblom.shearmadness.variations.silly;

import com.github.atomicblom.shearmadness.api.CommonReference;
import com.github.atomicblom.shearmadness.api.IBehaviourRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.behaviour.DamageBehaviour;
import com.github.atomicblom.shearmadness.api.behaviour.FlightBehaviour;
import com.github.atomicblom.shearmadness.api.events.IRegisterShearMadnessBehaviours;
import com.github.atomicblom.shearmadness.variations.IntegrationSettings;
import com.github.atomicblom.shearmadness.variations.vanilla.VanillaBehaviours;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@Mod.EventBusSubscriber(modid = CommonReference.MOD_ID)
public class SillyBehaviours
{
    @SubscribeEvent
    public static void onProcessIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(CommonReference.MOD_ID, CommonReference.IMCMethods.REGISTER_BEHAVIOURS, () -> (IRegisterShearMadnessBehaviours) SillyBehaviours::registerBehaviours);
    }

    //FIXME: May as well send these as individual IMC messages rather than a big batch.
    private static void registerBehaviours(IBehaviourRegistry registry) {
        registry.registerBehaviour(
                itemStack -> Items.GLOWSTONE_DUST.equals(itemStack.getItem()),
                entity -> new FlightBehaviour(entity, 9f, true)
        );
    }
}
