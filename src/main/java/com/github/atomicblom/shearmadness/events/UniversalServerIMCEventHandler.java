package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.api.BehaviourRegistry;
import com.github.atomicblom.shearmadness.api.CommonReference;
import com.github.atomicblom.shearmadness.api.events.IRegisterShearMadnessBehaviours;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

public class UniversalServerIMCEventHandler implements IIMCEventHandler {

    @Override
    public void processIMC(InterModProcessEvent event) {
        event.getIMCStream(CommonReference.IMCMethods.REGISTER_BEHAVIOURS::equals).forEach(imcMessage -> {
            IRegisterShearMadnessBehaviours messageSupplier = imcMessage.<IRegisterShearMadnessBehaviours>getMessageSupplier().get();
            messageSupplier.registerBehaviours(BehaviourRegistry.INSTANCE);
        });
    }
}
