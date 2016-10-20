package com.github.atomicblom.shearmadness.proxy;

import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraftforge.fml.common.SidedProxy;

@SuppressWarnings({"StaticNonFinalField", "UtilityClass"})
public final class Proxies
{
    @SidedProxy(
            modId = CommonReference.MOD_ID,
            clientSide = "com.github.atomicblom.shearmadness.proxy.ClientRenderProxy",
            serverSide = "com.github.atomicblom.shearmadness.proxy.CommonRenderProxy")
    public static CommonRenderProxy renderProxy = null;

    @SidedProxy(
            modId = CommonReference.MOD_ID,
            clientSide = "com.github.atomicblom.shearmadness.proxy.ClientBlockProxy",
            serverSide = "com.github.atomicblom.shearmadness.proxy.CommonBlockProxy")
    public static CommonBlockProxy blockProxy = null;

    @SidedProxy(
            modId = CommonReference.MOD_ID,
            clientSide = "com.github.atomicblom.shearmadness.proxy.ClientForgeEventProxy",
            serverSide = "com.github.atomicblom.shearmadness.proxy.CommonForgeEventProxy")
    public static CommonForgeEventProxy forgeEventProxy = null;

    @SidedProxy(
            modId = CommonReference.MOD_ID,
            clientSide = "com.github.atomicblom.shearmadness.proxy.ClientAudioProxy",
            serverSide = "com.github.atomicblom.shearmadness.proxy.CommonAudioProxy")
    public static CommonAudioProxy audioProxy = null;

    private Proxies() {}
}
