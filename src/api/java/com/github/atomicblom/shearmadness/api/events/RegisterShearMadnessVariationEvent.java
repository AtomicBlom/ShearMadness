package com.github.atomicblom.shearmadness.api.events;

import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Fired during the init phase of Forge, provides a mechanism for mods to register
 * how their blocks should be rendered by Shear Madness.
 * This event is only fired on clients.
 */
@OnlyIn(Dist.CLIENT)
public class RegisterShearMadnessVariationEvent extends Event
{
    private final IVariationRegistry registry;

    public RegisterShearMadnessVariationEvent(IVariationRegistry registry) {

        this.registry = registry;
    }

    /**
     * @return A registry to declare variation against.
     */
    public IVariationRegistry getRegistry()
    {
        return registry;
    }
}
