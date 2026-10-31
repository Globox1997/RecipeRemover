package net.reciperemover.mixin;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.server.ServerAdvancementLoader;
import net.reciperemover.RecipeRemover;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.HashSet;

@Mixin(ServerAdvancementLoader.class)
public class ServerAdvancementLoaderMixin {

    @Inject(method = "getAdvancements", at = @At("HEAD"), cancellable = true)
    private void getAdvancementsMixin(CallbackInfoReturnable<Collection<AdvancementEntry>> info) {
        if (RecipeRemover.CONFIG.removeAllAdvancements) {
            info.setReturnValue(new HashSet<>());
        }
    }

    @Inject(method = "getAdvancements", at = @At("RETURN"), cancellable = true)
    private void getAdvancementsMixinTwo(CallbackInfoReturnable<Collection<AdvancementEntry>> info) {
        Collection<AdvancementEntry> advancements = new HashSet<>(info.getReturnValue());
        if (RecipeRemover.CONFIG.removeAllVanillaAdvancements) {
            advancements.removeIf(entry -> entry.id().getNamespace().equals("minecraft"));
            info.setReturnValue(advancements);
        }
        if (!RecipeRemover.CONFIG.removeAllAdvancementsExcept.isEmpty()) {
            advancements.removeIf(entry -> !RecipeRemover.isExcepted(entry.id()));
            info.setReturnValue(advancements);
        }
    }
}
