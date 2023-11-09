package com.cleanroommc.draconicalchemy.alchemy;

import com.cleanroommc.draconicalchemy.util.RandomCollection;
import net.minecraft.block.state.IBlockState;

import java.util.Random;

public class BlastWave {
    public String name;
    public boolean destructive;

    public RandomCollection<IBlockState> depositions;

    public BlastWave(String name, boolean destructive) {
        this.name = name;
        this.destructive = destructive;
        this.depositions = new RandomCollection<>();
    }

    public IBlockState getDeposition(Random rand) {
        return depositions.next(rand);
    }
}
