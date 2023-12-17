package com.cleanroommc.draconicalchemy.jei.transmutation;

import com.cleanroommc.draconicalchemy.DraconicAlchemy;
import com.cleanroommc.draconicalchemy.jei.blastwave.DetonationInstanceIngredientHelper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;

import java.util.List;

public class RecipeCategoryTransmutation implements IRecipeCategory<RecipeWrapperFactoryTransmutation.RecipeWrapperTransmutation> {

    public static final String UID = DraconicAlchemy.MODID + ".Transmutation";
    private final IDrawable background;
    private final String title = "Transmutation";

    public RecipeCategoryTransmutation(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(142, 55);

    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return "Transmutation";
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
    public void drawExtras(Minecraft minecraft) {
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperFactoryTransmutation.RecipeWrapperTransmutation recipeWrapper, IIngredients ingredients) {
        IGuiIngredientGroup guiDetonationIngredientGroup = recipeLayout.getIngredientsGroup(DetonationInstanceIngredientHelper.iIngredientType);
        guiDetonationIngredientGroup.init(0, true, 63, 19);
        guiDetonationIngredientGroup.set(0, ingredients.getInputs(DetonationInstanceIngredientHelper.iIngredientType).get(0));

        IGuiItemStackGroup guiItems = recipeLayout.getItemStacks();
        guiItems.init(0, true, 39, 19);
        guiItems.init(1, false, 86, 19);
        guiItems.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        guiItems.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return IRecipeCategory.super.getTooltipStrings(mouseX, mouseY);
    }
}
