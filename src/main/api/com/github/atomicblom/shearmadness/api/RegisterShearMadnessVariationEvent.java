package com.github.atomicblom.shearmadness.api;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Fired during the init phase of Forge, provides a mechanism for mods to register
 * how their blocks should be rendered by Shear Madness.
 * This event is only fired on clients.
 */
@SideOnly(Side.CLIENT)
public class RegisterShearMadnessVariationEvent extends Event
{
    private final IVariationRegistry registry;

    public RegisterShearMadnessVariationEvent(IVariationRegistry registry) {

        this.registry = registry;
    }

    /**
     * @return A registry to declare variations against.
     */
    public IVariationRegistry getRegistry()
    {
        return registry;
    }
}
