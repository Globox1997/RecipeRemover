package net.reciperemover.compat;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.reciperemover.RecipeRemover;

@Environment(EnvType.CLIENT)
public class RecipeRemoverEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.removeRecipes(recipe -> recipe.getId() != null && RecipeRemover.isRecipeRemoved(recipe.getId()));
        for (Item item : RecipeRemover.getRemovedItems()) {
            registry.removeEmiStacks(EmiStack.of(item));
        }
    }

}
