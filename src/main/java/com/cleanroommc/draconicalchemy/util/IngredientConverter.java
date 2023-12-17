package com.cleanroommc.draconicalchemy.util;

import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.block.IBlockStateMatcher;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.List;

public class IngredientConverter {
    public static List<IBlockState> getPossibleBlockStates(IIngredient ingredient) {
        List<IItemStack> items = ingredient.getItems();
        List<ILiquidStack> fluids = ingredient.getLiquids();
        List<IBlockState> blockStates = new ArrayList<>();
        for (IItemStack item : items) {
            if (item.isItemBlock()) {
                IBlock block = item.asBlock();
                blockStates.add(block.getDefinition().getDefaultState());
            }
        }
        for (ILiquidStack liquidStack : fluids) {
            blockStates.add(liquidStack.getDefinition().getBlock().getDefinition().getDefaultState());
        }
        return blockStates;
    }

    public static ItemStack getItemStackFromBlockState(net.minecraft.block.state.IBlockState blockState) {
        Item item = Item.getItemFromBlock(blockState.getBlock());
        int meta = blockState.getBlock().damageDropped(blockState);
        ItemStack itemStack = item.getDefaultInstance();
        itemStack.setItemDamage(meta);
        return itemStack;
    }

}
