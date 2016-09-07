package com.github.atomicblom.shearmadness.api.events;

import com.github.atomicblom.shearmadness.api.IBehaviourRegistry;
import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Fired during the init phase of Forge, provides a mechanism for mods to register
 * how their sheep should behave by Shear Madness.
 */
public class RegisterShearMadnessBehaviourEvent extends Event
{
    private final IBehaviourRegistry registry;

    public RegisterShearMadnessBehaviourEvent(IBehaviourRegistry registry) {

        this.registry = registry;
    }

    /**
     * @return A registry to declare variation against.
     */
    public IBehaviourRegistry getRegistry()
    {
        return registry;
    }
}
