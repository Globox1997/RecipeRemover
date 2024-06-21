package net.reciperemover.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.reciperemover.RecipeRemover;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

    @ModifyConstant(method = "init", constant = @Constant(intValue = 104), require = 0)
    private int initModifyXMixin(int original) {
        return original + RecipeRemover.CONFIG.inventoryRecipeBookX;
    }

    @ModifyConstant(method = "init", constant = @Constant(intValue = 22), require = 0)
    private int initModifyYMixin(int original) {
        return original + RecipeRemover.CONFIG.inventoryRecipeBookY;
    }

    @ModifyConstant(method = "method_19891", constant = @Constant(intValue = 104), require = 0, remap = false)
    private int initModifyLambdaXMixin(int original) {
        return original + RecipeRemover.CONFIG.inventoryRecipeBookX;
    }

    @ModifyConstant(method = "method_19891", constant = @Constant(intValue = 22), require = 0, remap = false)
    private int initModifyLambdaYMixin(int original) {
        return original + RecipeRemover.CONFIG.inventoryRecipeBookY;
    }
}
