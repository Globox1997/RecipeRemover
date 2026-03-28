package net.reciperemover.mixin;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementManager;
import net.minecraft.util.Identifier;
import net.reciperemover.RecipeRemover;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(AdvancementManager.class)
public class AdvancementManagerMixin {

    @Inject(method = "addAll", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void addAllMixin(Collection<AdvancementEntry> advancements, CallbackInfo info, List<AdvancementEntry> list) {
        if (RecipeRemover.CONFIG.printRecipesAndAdvancements) {
            RecipeRemover.LOGGER.info("All Advancements");
            RecipeRemover.LOGGER.info(list);
        }
        if (RecipeRemover.CONFIG.removeAllAdvancements) {
            list.clear();
        }

        if (RecipeRemover.CONFIG.removeAllVanillaAdvancements) {
            List<AdvancementEntry> advancementEntries = list.stream().toList();
            for (AdvancementEntry advancementEntry : advancementEntries) {
                if (advancementEntry.id().getNamespace().equals("minecraft")) {
                    list.remove(advancementEntry);
                }
            }
        }

        if (!RecipeRemover.CONFIG.removeAllAdvancementsExcept.isEmpty()) {
            List<AdvancementEntry> advancementEntries = list.stream().toList();
            for (int i = 0; i < RecipeRemover.CONFIG.removeAllAdvancementsExcept.size(); i++) {
                for (AdvancementEntry entry : advancementEntries) {
                    if (RecipeRemover.CONFIG.removeAllAdvancementsExcept.get(i).endsWith(":")) {
                        if (!entry.id().getNamespace().equals(RecipeRemover.CONFIG.removeAllAdvancementsExcept.get(i).replace(":", ""))) {
                            list.remove(entry);
                        }
                    } else if (!Identifier.of(RecipeRemover.CONFIG.removeAllAdvancementsExcept.get(i)).equals(entry.id())) {
                        list.remove(entry);
                    }
                }
            }
        }

        if (!RecipeRemover.CONFIG.advancementList.isEmpty()) {
            List<AdvancementEntry> removingAdvancementEntries = new ArrayList<>();
            for (int i = 0; i < RecipeRemover.CONFIG.advancementList.size(); i++) {
                for (AdvancementEntry entry : list) {
                    if (Identifier.of(RecipeRemover.CONFIG.advancementList.get(i)).equals(entry.id())) {
                        removingAdvancementEntries.add(entry);
                    }
                    // if (map.remove(new Identifier(RecipeRemover.CONFIG.advancementList.get(i))) == null && RecipeRemover.CONFIG.printErrorMessage) {
                    // RecipeRemover.LOGGER.error("Failed to remove advancement with identifier \"{}\"", RecipeRemover.CONFIG.advancementList.get(i));
                }
            }
            list.removeAll(removingAdvancementEntries);
        }

        // example: minecraft:building_blocks/waxed_cut_copper_stairs_from_honeycomb
    }

}
