package com.cleanroommc.draconicalchemy.jei.polarisation;


import com.cleanroommc.draconicalchemy.alchemy.AlchemyRegistry;
import com.cleanroommc.draconicalchemy.alchemy.DetonationInstance;
import com.cleanroommc.draconicalchemy.alchemy.Polarisation;
import com.cleanroommc.draconicalchemy.alchemy.Transmutation;
import com.cleanroommc.draconicalchemy.jei.blastwave.DetonationInstanceIngredientHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeWrapperFactoryPolarisation implements IRecipeWrapperFactory<Polarisation> {

    @Override
    public IRecipeWrapper getRecipeWrapper(Polarisation recipe) {
        return new RecipeWrapperPolarisation(recipe);
    }

    public static class RecipeWrapperPolarisation implements IRecipeWrapper {
        Polarisation polarisation;

        public RecipeWrapperPolarisation(Polarisation polarisation) {
            this.polarisation = polarisation;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInput(DetonationInstanceIngredientHelper.iIngredientType, new DetonationInstance(polarisation.input));
            ingredients.setInput(VanillaTypes.ITEM, polarisation.getCatalystItemStack());
            ingredients.setOutput(DetonationInstanceIngredientHelper.iIngredientType, new DetonationInstance(polarisation.output));
        }

        public Polarisation getPolarisation() {
            return polarisation;
        }
    }
}
