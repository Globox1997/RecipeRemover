package net.reciperemover.mixin;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementManager;
import net.minecraft.util.Identifier;
import net.reciperemover.RecipeRemover;

@Mixin(AdvancementManager.class)
public class AdvancementManagerMixin {

    @Inject(method = "load", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/common/collect/Maps;newHashMap(Ljava/util/Map;)Ljava/util/HashMap;"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void loadMixin(Map<Identifier, Advancement.Builder> advancements, CallbackInfo info, Map<Identifier, Advancement.Builder> map) {
        if (RecipeRemover.CONFIG.printRecipesAndAdvancements) {
            RecipeRemover.LOGGER.info("All Advancements");
            RecipeRemover.LOGGER.info(map.keySet());
        }

        if (RecipeRemover.CONFIG.removeAllAdvancements) {
            map.clear();
        }

        if (RecipeRemover.CONFIG.removeAllVanillaAdvancements) {
            List<Identifier> identifiers = map.keySet().stream().toList();
            for (int i = 0; i < identifiers.size(); i++) {
                if (identifiers.get(i).getNamespace().equals("minecraft")) {
                    map.remove(identifiers.get(i));
                }
            }
        }

        for (int i = 0; i < RecipeRemover.CONFIG.advancementList.size(); i++) {
            if (map.remove(new Identifier(RecipeRemover.CONFIG.advancementList.get(i))) == null && RecipeRemover.CONFIG.printErrorMessage) {
                RecipeRemover.LOGGER.error("Failed to remove advancement with identifier \"{}\"", RecipeRemover.CONFIG.advancementList.get(i));
            }
        }
        // example: minecraft:recipes/building_blocks/waxed_cut_copper_stairs_from_honeycomb
    }
}
