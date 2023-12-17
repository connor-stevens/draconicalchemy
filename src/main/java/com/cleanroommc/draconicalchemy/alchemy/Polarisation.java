package com.cleanroommc.draconicalchemy.alchemy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.util.Objects;

import static com.cleanroommc.draconicalchemy.util.IngredientConverter.getItemStackFromBlockState;

public class Polarisation {
    public BlastWave input;
    public IBlockState blockState;
    public BlastWave output;
    public int power;
    public ItemStack inputOverride;

    public Polarisation(BlastWave input, IBlockState blockState, BlastWave output, int power) {
        this.input = input;
        this.blockState = blockState;
        this.output = output;
        this.power = power;
        AlchemyRegistry.registerPolariser(this);
    }

    public ItemStack getCatalystItemStack(){
        if (inputOverride == null) {
            return getItemStackFromBlockState(blockState);
        } else {
            return inputOverride;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polarisation that = (Polarisation) o;
        return input.equals(that.input) && output.equals(that.output) && getCatalystItemStack().isItemEqual(that.getCatalystItemStack());
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, output);
    }
}
