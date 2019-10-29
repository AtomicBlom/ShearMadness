package com.github.atomicblom.shearmadness.variations.chancecubes.capability;

/**
 * Created by codew on 1/03/2017.
 */
public class ChanceCubeParticipationCapability implements IChanceCubeParticipationCapability {
    private boolean isParticipating;

    @Override
    public boolean isParticipating() {
        return isParticipating;
    }

    @Override
    public void setParticipation(boolean isParticipating) {
        this.isParticipating = isParticipating;
    }
}
