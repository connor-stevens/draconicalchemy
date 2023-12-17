package com.cleanroommc.draconicalchemy.jei;

import com.cleanroommc.draconicalchemy.alchemy.*;
import com.cleanroommc.draconicalchemy.jei.blastwave.DetonationInstanceIngredientHelper;
import com.cleanroommc.draconicalchemy.jei.blastwave.DetonationInstanceRenderer;
import com.cleanroommc.draconicalchemy.jei.deposition.RecipeCategoryDeposition;
import com.cleanroommc.draconicalchemy.jei.deposition.RecipeWrapperFactoryDeposition;
import com.cleanroommc.draconicalchemy.jei.polarisation.RecipeCategoryPolarisation;
import com.cleanroommc.draconicalchemy.jei.polarisation.RecipeWrapperFactoryPolarisation;
import com.cleanroommc.draconicalchemy.jei.transmutation.RecipeCategoryTransmutation;
import com.cleanroommc.draconicalchemy.jei.transmutation.RecipeWrapperFactoryTransmutation;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class JEICompat implements IModPlugin {

    RecipeCategoryTransmutation recipeCategoryTransmutation;
    RecipeCategoryPolarisation recipeCategoryPolarisation;
    RecipeCategoryDeposition recipeCategoryDeposition;

    public List<DetonationInstance> getDetonationInstances() {
        List<DetonationInstance> detonationInstances = new ArrayList<>();
        for (BlastWave blastWave : AlchemyRegistry.waveTypes) {
            detonationInstances.add(new DetonationInstance(blastWave));
        }
        return detonationInstances;
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
        DetonationInstanceIngredientHelper detonationInstanceIngredientHelper = new DetonationInstanceIngredientHelper();
        DetonationInstanceRenderer detonationInstanceRenderer = new DetonationInstanceRenderer();
        registry.register(DetonationInstanceIngredientHelper.iIngredientType, getDetonationInstances(), detonationInstanceIngredientHelper, detonationInstanceRenderer);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helpers = registry.getJeiHelpers();
        final IGuiHelper gui = helpers.getGuiHelper();

        recipeCategoryTransmutation = new RecipeCategoryTransmutation(gui);
        recipeCategoryPolarisation = new RecipeCategoryPolarisation(gui);
        recipeCategoryDeposition = new RecipeCategoryDeposition(gui);

        registry.addRecipeCategories(recipeCategoryTransmutation);
        registry.addRecipeCategories(recipeCategoryPolarisation);
        registry.addRecipeCategories(recipeCategoryDeposition);
    }



    @Override
    public void register(IModRegistry registry) {
        RecipeWrapperFactoryTransmutation transmutationFactory = new RecipeWrapperFactoryTransmutation();
        registry.handleRecipes(Transmutation.class, transmutationFactory, recipeCategoryTransmutation.getUid());
        registry.addRecipes(AlchemyRegistry.getUniqueTransmutations(), recipeCategoryTransmutation.getUid());

        RecipeWrapperFactoryPolarisation polarisationFactory = new RecipeWrapperFactoryPolarisation();
        registry.handleRecipes(Polarisation.class, polarisationFactory, recipeCategoryPolarisation.getUid());
        registry.addRecipes(AlchemyRegistry.getUniquePolarisations(), recipeCategoryPolarisation.getUid());

        RecipeWrapperFactoryDeposition depositionFactory = new RecipeWrapperFactoryDeposition();
        registry.handleRecipes(BlastWave.class, depositionFactory, recipeCategoryDeposition.getUid());
        registry.addRecipes(AlchemyRegistry.waveTypes, recipeCategoryDeposition.getUid());
    }
}
