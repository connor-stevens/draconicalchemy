package com.cleanroommc.draconicalchemy.jei.transmutation;


import com.cleanroommc.draconicalchemy.alchemy.AlchemyRegistry;
import com.cleanroommc.draconicalchemy.alchemy.DetonationInstance;
import com.cleanroommc.draconicalchemy.alchemy.Transmutation;
import com.cleanroommc.draconicalchemy.jei.JEICompat;
import com.cleanroommc.draconicalchemy.jei.blastwave.DetonationInstanceIngredientHelper;
import com.cleanroommc.draconicalchemy.jei.polarisation.RecipeWrapperFactoryPolarisation;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeWrapperFactoryTransmutation implements IRecipeWrapperFactory<Transmutation> {

    @Override
    public IRecipeWrapper getRecipeWrapper(Transmutation recipe) {
        return new RecipeWrapperTransmutation(recipe);
    }

    public static class RecipeWrapperTransmutation implements IRecipeWrapper {
        Transmutation transmutation;

        public RecipeWrapperTransmutation(Transmutation transmutation) {
            this.transmutation = transmutation;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInput(VanillaTypes.ITEM, transmutation.getInputItemStack());
            ingredients.setInput(DetonationInstanceIngredientHelper.iIngredientType, new DetonationInstance(transmutation.waveType));
            ingredients.setOutput(VanillaTypes.ITEM, transmutation.getOutputItemStack());
        }

        public Transmutation getTransmutation() {
            return transmutation;
        }
    }
}
