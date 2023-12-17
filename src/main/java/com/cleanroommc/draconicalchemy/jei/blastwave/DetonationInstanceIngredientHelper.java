package com.cleanroommc.draconicalchemy.jei.blastwave;

import com.cleanroommc.draconicalchemy.DraconicAlchemy;
import com.cleanroommc.draconicalchemy.alchemy.DetonationInstance;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.recipe.IIngredientType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DetonationInstanceIngredientHelper implements IIngredientHelper<DetonationInstance> {
    public static final IIngredientType<DetonationInstance> iIngredientType = new IIngredientType() {
        @Override
        @Nonnull
        public Class getIngredientClass() {
            return DetonationInstance.class;
        }
    };

    @Nullable
    @Override
    public DetonationInstance getMatch(Iterable<DetonationInstance> ingredients, DetonationInstance ingredientToMatch) {
        for (DetonationInstance detonation : ingredients) {
            if (detonation.waveType == ingredientToMatch.waveType) {
                return detonation;
            }
        }
        return null;
    }

    @Override
    public String getDisplayName(DetonationInstance ingredient) {
        return ingredient.waveType.getName();
    }

    @Override
    public String getUniqueId(DetonationInstance ingredient) {
        if (!ingredient.hasParameters) {
            return DraconicAlchemy.MODID + ":BlastWave:" + ingredient.waveType.name;
        } else {
            return DraconicAlchemy.MODID + ":BlastWave:" + ingredient.waveType.name;
        }
    }

    @Override
    public String getWildcardId(DetonationInstance ingredient) {
        return DraconicAlchemy.MODID + ":BlastWave:" + ingredient.waveType.name;
    }

    @Override
    public String getModId(DetonationInstance ingredient) {
        return DraconicAlchemy.MODID;
    }

    @Override
    public String getResourceId(DetonationInstance ingredient) {
        return "DetonationInstance." + ingredient.waveType.id;
    }

    @Override
    public DetonationInstance copyIngredient(DetonationInstance ingredient) {
        return ingredient.copy();
    }

    @Override
    public String getErrorInfo(@Nullable DetonationInstance ingredient) {
        return ingredient.waveType.getName();
    }
}
