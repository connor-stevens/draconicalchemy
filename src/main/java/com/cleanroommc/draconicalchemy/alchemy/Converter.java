package com.cleanroommc.draconicalchemy.alchemy;

import net.minecraft.block.state.IBlockState;

public class Converter {
    public BlastWave input;
    public IBlockState blockState;
    public BlastWave output;
    public int power;

    public Converter(BlastWave input, IBlockState blockState, BlastWave output, int power) {
        this.input = input;
        this.blockState = blockState;
        this.output = output;
        this.power = power;
    }
}
