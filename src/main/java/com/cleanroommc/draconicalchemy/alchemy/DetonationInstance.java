package com.cleanroommc.draconicalchemy.alchemy;

import java.util.Arrays;
import java.util.List;

public class DetonationInstance {
    public BlastWave waveType;
    public boolean hasParameters;

    public DetonationInstance(BlastWave waveType) {
        this.hasParameters = false;
        this.waveType = waveType;
    }

    public DetonationInstance(DetonationInstance instance) {
        this.waveType = instance.waveType;
        this.hasParameters = instance.hasParameters;
    }

    public List<String> getTooltip() {
        return Arrays.asList(
             waveType.getName()
        );
    }

    public DetonationInstance copy() {
        return new DetonationInstance(this);
    }
}
