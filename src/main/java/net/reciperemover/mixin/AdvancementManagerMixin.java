package net.reciperemover.mixin;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementManager;
import net.minecraft.util.Identifier;
import net.reciperemover.RecipeRemover;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.*;

@Mixin(AdvancementManager.class)
public class AdvancementManagerMixin {

    @ModifyVariable(method = "addAll", at = @At("HEAD"), argsOnly = true)
    private Collection<AdvancementEntry> addAllMixin(Collection<AdvancementEntry> advancements) {
        if (RecipeRemover.CONFIG.printRecipesAndAdvancements) {
            RecipeRemover.LOGGER.info("All Advancements");
            RecipeRemover.LOGGER.info(advancements);
        }
        if (RecipeRemover.CONFIG.removeAllAdvancements) {
            return List.of();
        }

        List<AdvancementEntry> list = new ArrayList<>(advancements);

        if (RecipeRemover.CONFIG.removeAllVanillaAdvancements) {
            list.removeIf(entry -> entry.id().getNamespace().equals("minecraft"));
        }

        if (!RecipeRemover.CONFIG.removeAllAdvancementsExcept.isEmpty()) {
            list.removeIf(entry -> !RecipeRemover.isExcepted(entry.id()));
        }

        if (!RecipeRemover.CONFIG.advancementList.isEmpty()) {
            Set<Identifier> removedAdvancements = new HashSet<>();
            for (String advancement : RecipeRemover.CONFIG.advancementList) {
                Identifier identifier = Identifier.tryParse(advancement);
                if (identifier != null) {
                    removedAdvancements.add(identifier);
                } else if (RecipeRemover.CONFIG.printErrorMessage) {
                    RecipeRemover.LOGGER.error("Failed to remove advancement with identifier \"{}\"", advancement);
                }
            }
            list.removeIf(entry -> removedAdvancements.contains(entry.id()));
        }

        return list;
    }

}
