package net.reciperemover.mixin;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementManager;
import net.minecraft.util.Identifier;
import net.reciperemover.RecipeRemover;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            list.removeIf(entry -> !isExcepted(entry.id()));
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

    @Unique
    private static boolean isExcepted(Identifier identifier) {
        for (String exception : RecipeRemover.CONFIG.removeAllAdvancementsExcept) {
            if (exception.endsWith(":")) {
                if (identifier.getNamespace().equals(exception.substring(0, exception.length() - 1))) {
                    return true;
                }
            } else if (identifier.equals(Identifier.tryParse(exception))) {
                return true;
            }
        }
        return false;
    }

}
