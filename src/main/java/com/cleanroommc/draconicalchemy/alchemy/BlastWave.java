package com.cleanroommc.draconicalchemy.alchemy;

import com.cleanroommc.draconicalchemy.util.IngredientConverter;
import com.cleanroommc.draconicalchemy.util.RandomCollection;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockStateMatcher;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.util.*;

@ZenRegister
@ZenClass("mods.draconicalchemy.BlastWave")
public class BlastWave {

    @ZenProperty
    public String name;
    @ZenProperty
    public boolean destructive;

    @ZenGetter
    public int getColour1() {
        return colour1;
    }

    @ZenSetter
    public void setColour1(int colour1) {
        this.colour1 = colour1;
    }

    @ZenGetter
    public int getColour2() {
        return colour2;
    }

    @ZenSetter
    public void setColour2(int colour2) {
        this.colour2 = colour2;
    }

    @ZenProperty
    public int colour1 = 0x0000ff;
    @ZenProperty
    public int colour2 = 0xff0000;

    @ZenGetter
    public String getName() {
        return name;
    }

    @ZenSetter
    public void setName(String name) {
        this.name = name;
    }

    @ZenGetter
    public boolean isDestructive() {
        return destructive;
    }

    @ZenSetter
    public void setDestructive(boolean destructive) {
        this.destructive = destructive;
    }

    public int id;
    public RandomCollection<IBlockState> depositions = new RandomCollection<>();

    public HashMap<IBlockState, Polarisation> converters = new HashMap<>();

    public HashMap<IBlockState, Transmutation> reactions = new HashMap<>();

    public Set<IBlockState> immunities = new HashSet<>();


    public BlastWave(String name, boolean destructive) {
        this.name = name;
        this.destructive = destructive;
    }

    public IBlockState getDeposition(Random rand) {
        IBlockState state = depositions.next(rand);
        return state == null ? Blocks.AIR.getDefaultState() : state;
    }

    public void addConversion(IBlockState blockState, int power, BlastWave blastWave) {
        Polarisation polarisation = new Polarisation(this, blockState, blastWave, power);
        converters.put(blockState, polarisation);
    }

    public void addReaction(IBlockState input, IBlockState output, int powerRequired) {
        Transmutation transmutation = new Transmutation(input, this, output, powerRequired);
        reactions.put(input, transmutation);
    }

    public void addImmunity(IBlockState state) {
        immunities.add(state);
    }

    //Crafttweaker API
    @ZenMethod
    public static BlastWave getDefaultBlastWave() {
        return AlchemyRegistry.CHAOTICBLASTWAVE;
    }

    @ZenMethod
    public static BlastWave newBlastWave(String name, boolean destructive) {
        BlastWave newWave = new BlastWave(name, destructive);
        AlchemyRegistry.registerBlastWave(newWave);
        return newWave;
    }

    @ZenMethod
    public void addPolariser(crafttweaker.api.block.IBlockState input, int power, BlastWave output) {
        CraftTweakerAPI.apply(new Polariser(this, CraftTweakerMC.getBlockState(input), power, output));
    }

    @ZenMethod
    public void addPolariser(crafttweaker.api.block.IBlockStateMatcher input, int power, BlastWave output) {
        for (crafttweaker.api.block.IBlockState blockState : input.getMatchingBlockStates()) {
            addPolariser(blockState, power, output);
        }
    }

    @ZenMethod
    public void addPolariser(IIngredient input, int power, BlastWave output) {
        for (crafttweaker.api.block.IBlockState blockState : IngredientConverter.getPossibleBlockStates(input)) {
            addPolariser(blockState, power, output);
        }
    }

    @ZenMethod
    public void addTransmutation(crafttweaker.api.block.IBlockState input, int power, crafttweaker.api.block.IBlockState output) {
        CraftTweakerAPI.apply(new Transmutor(this, CraftTweakerMC.getBlockState(input),
                power, CraftTweakerMC.getBlockState(output)));
    }

    @ZenMethod
    public void addTransmutation(IBlockStateMatcher input, int power, crafttweaker.api.block.IBlockState output) {
        for (crafttweaker.api.block.IBlockState inputState : input.getMatchingBlockStates()) {
            addTransmutation(inputState, power, output);
        }
    }

    @ZenMethod
    public void addTransmutation(IIngredient input, int power, crafttweaker.api.block.IBlockState output) {
        for (crafttweaker.api.block.IBlockState inputState : IngredientConverter.getPossibleBlockStates(input)) {
            addTransmutation(inputState, power, output);
        }
    }

    @ZenMethod
    public void addDeposition(crafttweaker.api.block.IBlockState blockState, int weight) {
        CraftTweakerAPI.apply(new AddDeposition(this,
               CraftTweakerMC.getBlockState(blockState), weight));
    }

    @ZenMethod
    public void removeDeposition(crafttweaker.api.block.IBlockState blockState) {
        CraftTweakerAPI.apply(new RemoveDeposition(this, CraftTweakerMC.getBlockState(blockState)));
    }


    @ZenMethod
    public void addImmunity(crafttweaker.api.block.IBlockState blockState) {
        CraftTweakerAPI.apply(new Immunity(this, CraftTweakerMC.getBlockState(blockState)));
    }

    @ZenMethod
    public void addImmunity(IBlockStateMatcher blockStateMatcher) {
        for (crafttweaker.api.block.IBlockState blockState : blockStateMatcher.getMatchingBlockStates()) {
            addImmunity(blockState);
        }
    }

    @ZenMethod
    public void addImmunity(IIngredient input) {
        for (crafttweaker.api.block.IBlockState inputState : IngredientConverter.getPossibleBlockStates(input)) {
            addImmunity(inputState);
        }
    }

    static class Polariser implements IAction {
        BlastWave blastWave;
        IBlockState block;
        int power;
        BlastWave output;

        public Polariser(BlastWave blastWave, IBlockState block, int power, BlastWave output) {
            this.blastWave = blastWave;
            this.block = block;
            this.power = power;
            this.output = output;
        }

        @Override
        public void apply() {
            blastWave.addConversion(block, power, output);
        }

        @Override
        public String describe() {
            return "Adding polariser block";
        }
    }

    static class Transmutor implements IAction {
        BlastWave blastWave;
        IBlockState input;
        IBlockState output;
        int power;

        public Transmutor(BlastWave blastWave, IBlockState input, int power, IBlockState output) {
            this.blastWave = blastWave;
            this.input = input;
            this.power = power;
            this.output = output;
        }

        @Override
        public void apply() {
            blastWave.addReaction(input, output, power);
        }

        @Override
        public String describe() {
            return "Adding block to block transmutation";
        }
    }

    static class AddDeposition implements IAction {
        BlastWave blastWave;
        IBlockState blockState;
        int weight;

        public AddDeposition(BlastWave blastWave, IBlockState blockState, int weight) {
            this.blastWave = blastWave;
            this.blockState = blockState;
            this.weight = weight;
        }


        @Override
        public void apply() {
            blastWave.depositions.add(weight, blockState);
        }

        @Override
        public String describe() {
            return "Adding deposition to blastwave";
        }
    }

    static class RemoveDeposition implements IAction {
        BlastWave blastWave;
        IBlockState blockState;


        public RemoveDeposition(BlastWave blastWave, IBlockState blockState) {
            this.blastWave = blastWave;
            this.blockState = blockState;
        }


        @Override
        public void apply() {
            blastWave.depositions.remove(blockState);
        }

        @Override
        public String describe() {
            return "Removing deposition from blastwave";
        }
    }

    static class Immunity implements IAction {
        BlastWave blastWave;
        IBlockState block;

        public Immunity(BlastWave blastWave, IBlockState block) {
            this.blastWave = blastWave;
            this.block = block;
        }

        @Override
        public void apply() {
            blastWave.addImmunity(block);
        }

        @Override
        public String describe() {
            return "Adding Immunity";
        }
    }

}
