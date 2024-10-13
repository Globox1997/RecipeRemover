package net.reciperemover.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementManager;
import net.minecraft.util.Identifier;
import net.reciperemover.RecipeRemover;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(AdvancementManager.class)
public class AdvancementManagerMixin {

    @Unique
    private void addAllMixin(Collection<Advancement> advancements, CallbackInfo info, List<Advancement> list) {
        if (RecipeRemover.CONFIG.printRecipesAndAdvancements) {
            RecipeRemover.LOGGER.info("All Advancements");
            RecipeRemover.LOGGER.info(list);
        }
        if (RecipeRemover.CONFIG.removeAllAdvancements) {
            list.clear();
        }

        if (RecipeRemover.CONFIG.removeAllVanillaAdvancements) {
            List<Advancement> advancementEntries = list.stream().toList();
            for (Advancement advancementEntry : advancementEntries) {
                if (advancementEntry.getId().getNamespace().equals("minecraft")) {
                    list.remove(advancementEntry);
                }
            }
        }

        List<Advancement> removingAdvancementEntries = new ArrayList<>();
        for (int i = 0; i < RecipeRemover.CONFIG.advancementList.size(); i++) {
            for (Advancement entry : list) {
                if (new Identifier(RecipeRemover.CONFIG.advancementList.get(i)).equals(entry.getId())) {
                    removingAdvancementEntries.add(entry);
                }
            }
        }
        list.removeAll(removingAdvancementEntries);
    }
}
