package com.cleanroommc.draconicalchemy.alchemy;

import com.cleanroommc.draconicalchemy.util.RandomCollection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class BlastWave {
    public String name;
    public boolean destructive;

    public int id;
    public RandomCollection<IBlockState> depositions;

    public HashMap<IBlockState, Converter> converters = new HashMap<>();

    public HashMap<IBlockState, Reaction> reactions = new HashMap<>();


    public BlastWave(String name, boolean destructive) {
        this.name = name;
        this.destructive = destructive;
        this.depositions = new RandomCollection<>();
    }

    public IBlockState getDeposition(Random rand) {
        IBlockState state = depositions.next(rand);
        return state == null ? Blocks.AIR.getDefaultState() : state;
    }

    public void addConversion(IBlockState blockState, int power, BlastWave blastWave) {
        Converter converter = new Converter(this, blockState, blastWave, power);
        converters.put(blockState, converter);
    }

    public void addReaction(IBlockState input, IBlockState output, int powerRequired) {
        Reaction reaction = new Reaction(input, this, output, powerRequired);
        reactions.put(input, reaction);
    }
}
