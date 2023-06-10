package net.reciperemover.mixin;

import com.google.gson.JsonElement;

import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.reciperemover.RecipeRemover;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(method = "apply", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/common/collect/ImmutableMap;builder()Lcom/google/common/collect/ImmutableMap$Builder;"))
    private void applyMixin(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        if (RecipeRemover.CONFIG.printRecipesAndAdvancements) {
            RecipeRemover.LOGGER.info("All Recipes");
            RecipeRemover.LOGGER.info(map.keySet());
        }

        for (int i = 0; i < RecipeRemover.CONFIG.recipeList.size(); i++) {
            if (!RecipeRemover.CONFIG.recipeList.get(i).contains(":")) {
                Iterator<Identifier> recipeIdIterator = map.keySet().iterator();
                while (recipeIdIterator.hasNext()) {
                    Identifier identifier = recipeIdIterator.next();
                    if (identifier.getNamespace().equals(RecipeRemover.CONFIG.recipeList.get(i))) {
                        map.remove(identifier);
                    }
                }
            } else if (map.remove(new Identifier(RecipeRemover.CONFIG.recipeList.get(i))) == null && RecipeRemover.CONFIG.printErrorMessage) {
                RecipeRemover.LOGGER.error("Failed to remove item with identifier \"{}\"", RecipeRemover.CONFIG.recipeList.get(i));
            }
        }

    }

}
