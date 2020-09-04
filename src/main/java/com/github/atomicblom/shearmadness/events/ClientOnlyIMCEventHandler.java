package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.api.CommonReference;
import com.github.atomicblom.shearmadness.api.VariationRegistry;
import com.github.atomicblom.shearmadness.api.events.IRegisterShearMadnessVariations;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

public class ClientOnlyIMCEventHandler extends UniversalServerIMCEventHandler {
    @Override
    public void processIMC(InterModProcessEvent event) {
        event.getIMCStream(CommonReference.IMCMethods.REGISTER_VARIATIONS::equals).forEach(imcMessage -> {
            IRegisterShearMadnessVariations messageSupplier = imcMessage.<IRegisterShearMadnessVariations>getMessageSupplier().get();
            messageSupplier.registerVariations(VariationRegistry.INSTANCE);
        });

        super.processIMC(event);
    }
}
