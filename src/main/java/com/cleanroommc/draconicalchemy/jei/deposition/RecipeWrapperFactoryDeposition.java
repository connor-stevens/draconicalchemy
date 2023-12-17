package com.cleanroommc.draconicalchemy.jei.deposition;

import com.cleanroommc.draconicalchemy.alchemy.BlastWave;
import com.cleanroommc.draconicalchemy.alchemy.DetonationInstance;
import com.cleanroommc.draconicalchemy.jei.blastwave.DetonationInstanceIngredientHelper;
import com.cleanroommc.draconicalchemy.util.IngredientConverter;
import com.cleanroommc.draconicalchemy.util.ItemStackMap;
import com.cleanroommc.draconicalchemy.util.RandomCollection;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.terraingen.WorldTypeEvent;

import javax.annotation.Nonnull;
import java.util.List;

public class RecipeWrapperFactoryDeposition implements IRecipeWrapperFactory<BlastWave> {

    @Override
    @Nonnull
    public IRecipeWrapper getRecipeWrapper(BlastWave recipe) {
        return new DepositionRecipeWrapper(recipe);
    }

    ItemStackMap<Integer> getItemStackswithWeight(RandomCollection<IBlockState> depositions) {
        ItemStackMap<Integer> itemStackMap = new ItemStackMap<>();
        for (IBlockState state : depositions.getMap().keySet()) {
            ItemStack item = IngredientConverter.getItemStackFromBlockState(state);
            Integer weight = itemStackMap.getOrDefault(item, 0);
            itemStackMap.put(item, weight + depositions.getWeight(state));
        }
        return itemStackMap;
    }

    public class DepositionRecipeWrapper implements IRecipeWrapper {

        public BlastWave blastWave;
        ItemStackMap<Integer> weights;


        public DepositionRecipeWrapper(BlastWave blastWave) {
            this.blastWave = blastWave;
            this.weights = getItemStackswithWeight(blastWave.depositions);
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInput(DetonationInstanceIngredientHelper.iIngredientType, new DetonationInstance(blastWave));
            ingredients.setOutputs(VanillaTypes.ITEM, weights.getKeys());
        }
    }
}
