package com.cleanroommc.draconicalchemy.alchemy;

import net.minecraft.block.state.IBlockState;

public class Reaction {
    public IBlockState input;
    public BlastWave waveType;
    public IBlockState output;
    public int powerRequired;

    public Reaction(IBlockState reactant, BlastWave waveType, IBlockState result, int powerRequired) {
        this.input = reactant;
        this.waveType = waveType;
        this.output = result;
        this.powerRequired = powerRequired;
    }
}
