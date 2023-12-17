package com.cleanroommc.draconicalchemy.jei.polarisation;

import com.cleanroommc.draconicalchemy.DraconicAlchemy;
import com.cleanroommc.draconicalchemy.jei.blastwave.DetonationInstanceIngredientHelper;
import com.cleanroommc.draconicalchemy.alchemy.DetonationInstance;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;
import java.util.List;

public class RecipeCategoryPolarisation implements IRecipeCategory<RecipeWrapperFactoryPolarisation.RecipeWrapperPolarisation> {

    public static final String UID = DraconicAlchemy.MODID + ".Polarisation";
    private final IDrawable background;
    private final String title = "Polarisation";

    public RecipeCategoryPolarisation(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(142, 55);

    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return I18n.format("draconicalchemy.polarisation");
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
    public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperFactoryPolarisation.RecipeWrapperPolarisation recipeWrapper, IIngredients ingredients) {
        IGuiIngredientGroup<DetonationInstance> guiDetonationIngredientGroup = recipeLayout.getIngredientsGroup(DetonationInstanceIngredientHelper.iIngredientType);

        guiDetonationIngredientGroup.init(0, true, 39, 19);
        guiDetonationIngredientGroup.init(1, false, 86, 19);
        guiDetonationIngredientGroup.set(0, ingredients.getInputs(DetonationInstanceIngredientHelper.iIngredientType).get(0));
        guiDetonationIngredientGroup.set(1, ingredients.getOutputs(DetonationInstanceIngredientHelper.iIngredientType).get(0));


        IGuiItemStackGroup guiItems = recipeLayout.getItemStacks();
        guiItems.init(0, true, 63, 19);
        guiItems.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
    }

    @Override
    @Nonnull
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return IRecipeCategory.super.getTooltipStrings(mouseX, mouseY);
    }
}
