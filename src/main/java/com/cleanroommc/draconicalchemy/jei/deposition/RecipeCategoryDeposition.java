package com.cleanroommc.draconicalchemy.jei.deposition;

import com.cleanroommc.draconicalchemy.DraconicAlchemy;
import com.cleanroommc.draconicalchemy.alchemy.DetonationInstance;
import com.cleanroommc.draconicalchemy.jei.blastwave.DetonationInstanceIngredientHelper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeCategoryDeposition implements IRecipeCategory<RecipeWrapperFactoryDeposition.DepositionRecipeWrapper> {
    public static final String UID = DraconicAlchemy.MODID + ".Deposition";
    private final IDrawable background;

    public RecipeCategoryDeposition(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(142, 55);

    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return I18n.format("draconicalchemy.deposition");
    }

    @Override
    public String getModName() {
        return DraconicAlchemy.MODNAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperFactoryDeposition.DepositionRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiIngredientGroup<DetonationInstance> guiDetonationIngredientGroup = recipeLayout.getIngredientsGroup(DetonationInstanceIngredientHelper.iIngredientType);
        guiDetonationIngredientGroup.init(0, true, 63, 19);
        guiDetonationIngredientGroup.set(0, ingredients.getInputs(DetonationInstanceIngredientHelper.iIngredientType).get(0));

        int count = 0;
        IGuiItemStackGroup guiItems = recipeLayout.getItemStacks();
        for (List<ItemStack> items : ingredients.getOutputs(VanillaTypes.ITEM)) {
            guiItems.init(count, false, 22 + count * 17, 35);
            guiItems.set(count, items.get(count));
        }
    }

}
