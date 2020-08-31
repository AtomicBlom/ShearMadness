package com.github.atomicblom.shearmadness.block;

public class InvisibleGlowstoneBlock extends InvisibleBlock
{
    public InvisibleGlowstoneBlock(Properties properties)
    {
        super(properties.setLightLevel((state) -> 15));
    }
}
