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

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("HEAD"))
    private void applyMixin(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        if (RecipeRemover.CONFIG.printRecipesAndAdvancements) {
            RecipeRemover.LOGGER.info("All Recipes");
            RecipeRemover.LOGGER.info(map.keySet());
        }

        for (String entry : RecipeRemover.CONFIG.recipeList) {
            if (!entry.contains(":")) {
                map.keySet().removeIf(identifier -> identifier.getNamespace().equals(entry));
            } else {
                Identifier identifier = Identifier.tryParse(entry);
                if ((identifier == null || map.remove(identifier) == null) && RecipeRemover.CONFIG.printErrorMessage) {
                    RecipeRemover.LOGGER.error("Failed to remove recipe with identifier \"{}\"", entry);
                }
            }
        }

    }

}
