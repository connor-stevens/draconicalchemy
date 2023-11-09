package com.cleanroommc.draconicalchemy.alchemy;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class Reaction {
    public Block reactant;
    public BlastWave waveType;
    public Block result;
    public int powerRequired;

    public Reaction(Block reactant, BlastWave waveType, Block result, int powerRequired) {
        this.reactant = reactant;
        this.waveType = waveType;
        this.result = result;
        this.powerRequired = powerRequired;
    }
}
