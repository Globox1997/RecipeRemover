package net.reciperemover.mixin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementManager;
import net.minecraft.util.Identifier;
import net.reciperemover.RecipeRemover;

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
            for (int i = 0; i < advancementEntries.size(); i++) {
                if (advancementEntries.get(i).id().getNamespace().equals("minecraft")) {
                    list.remove(advancementEntries.get(i));
                }
            }
        }

        List<AdvancementEntry> removingAdvancementEntries = new ArrayList<AdvancementEntry>();
        for (int i = 0; i < RecipeRemover.CONFIG.advancementList.size(); i++) {
            Iterator<AdvancementEntry> iterator = list.iterator();
            while (iterator.hasNext()) {
                AdvancementEntry entry = iterator.next();
                if (Identifier.of(RecipeRemover.CONFIG.advancementList.get(i)).equals(entry.id())) {
                    removingAdvancementEntries.add(entry);
                }
                // list.stream().toList().
                // if (map.remove(new Identifier(RecipeRemover.CONFIG.advancementList.get(i))) == null && RecipeRemover.CONFIG.printErrorMessage) {
                // RecipeRemover.LOGGER.error("Failed to remove advancement with identifier \"{}\"", RecipeRemover.CONFIG.advancementList.get(i));
            }
        }
        list.removeAll(removingAdvancementEntries);

        // example: minecraft:recipes/building_blocks/waxed_cut_copper_stairs_from_honeycomb
    }

}
