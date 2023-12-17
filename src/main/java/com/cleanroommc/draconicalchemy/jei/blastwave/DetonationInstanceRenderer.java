package com.cleanroommc.draconicalchemy.jei.blastwave;

import com.cleanroommc.draconicalchemy.alchemy.DetonationInstance;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import mezz.jei.api.gui.IDrawable;
import javax.annotation.Nullable;
import java.util.List;

public class DetonationInstanceRenderer implements IIngredientRenderer<DetonationInstance> {
    ITextureObject defaultIcon;
    public DetonationInstanceRenderer() {
        ResourceLocation loc = new ResourceLocation("draconicalchemy","textures/gui/nuclearicon.png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
        defaultIcon =  Minecraft.getMinecraft().getTextureManager().getTexture(loc);
    }

    float getFloatFromHex(int hex, int rgb) {
        int mask = 0xff << rgb*8;
        return (float)(hex & mask) / (float)(mask);
    }

    @Override
    public void render(Minecraft minecraft, int xPosition, int yPosition, @Nullable DetonationInstance ingredient) {
        if (ingredient != null) {
            GlStateManager.enableDepth();

            GlStateManager.bindTexture(defaultIcon.getGlTextureId());
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            int colour = ingredient.waveType.getColour1();
            GlStateManager.color(getFloatFromHex(colour, 2), getFloatFromHex(colour, 1), getFloatFromHex(colour,0),1);
            minecraft.currentScreen.drawTexturedModalRect(xPosition, yPosition, 0, 0, 16, 16);
            colour = ingredient.waveType.getColour2();
            GlStateManager.color(getFloatFromHex(colour, 2), getFloatFromHex(colour, 1), getFloatFromHex(colour,0),1);
            minecraft.currentScreen.drawTexturedModalRect(xPosition, yPosition, 16, 0, 16, 16);
            GlStateManager.disableAlpha();

            GlStateManager.disableBlend();
            //minecraft.getRenderItem().renderItemAndEffectIntoGUI(null, fakeItem, xPosition, yPosition);
            //minecraft.getRenderItem().renderItemOverlayIntoGUI(font, fakeItem, xPosition, yPosition, null);

            RenderHelper.disableStandardItemLighting();
        }
    }

    @Override
    public List<String> getTooltip(Minecraft minecraft, DetonationInstance ingredient, ITooltipFlag tooltipFlag) {
        return ingredient.getTooltip();
    }
}
