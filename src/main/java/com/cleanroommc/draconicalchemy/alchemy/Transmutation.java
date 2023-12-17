package com.cleanroommc.draconicalchemy.alchemy;

import com.cleanroommc.draconicalchemy.jei.transmutation.RecipeWrapperFactoryTransmutation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Objects;

import static com.cleanroommc.draconicalchemy.util.IngredientConverter.getItemStackFromBlockState;

public class Transmutation {
    public IBlockState input;
    public BlastWave waveType;
    public IBlockState output;
    public int powerRequired;

    public ItemStack inputOverride;
    public ItemStack outputOverride;

    public Transmutation(IBlockState reactant, BlastWave waveType, IBlockState result, int powerRequired) {
        this.input = reactant;
        this.waveType = waveType;
        this.output = result;
        this.powerRequired = powerRequired;
        this.inputOverride = null;
        this.outputOverride = null;
        AlchemyRegistry.registerTransmutation(this);
    }

    public ItemStack getInputItemStack() {
        if (inputOverride == null) {
            return getItemStackFromBlockState(input);
        } else {
            return inputOverride;
        }
    }

    public ItemStack getOutputItemStack() {
        if (outputOverride == null) {
            return getItemStackFromBlockState(output);
        } else {
            return outputOverride;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transmutation that = (Transmutation) o;
        return waveType.equals(that.waveType) && getInputItemStack().isItemEqual(that.getInputItemStack()) && getOutputItemStack().isItemEqual(that.getOutputItemStack());
    }

    @Override
    public int hashCode() {
        return Objects.hash(waveType);
    }
}
