package net.reciperemover.mixin;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.network.ServerRecipeBook;
import net.minecraft.util.Identifier;
import net.reciperemover.RecipeRemover;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin {

    // info.cancel() will stop the method completely
    // @Inject(method = "handleList", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 0), cancellable = true, locals =
    // LocalCapture.CAPTURE_FAILSOFT)
    // private void handleListMixin(NbtList list, Consumer<Recipe<?>> handler, RecipeManager recipeManager, CallbackInfo info, int i, String string) {
    // if (RecipeRemover.CONFIG.recipe_list.contains(string))
    // info.cancel();
    // }

    @Redirect(method = "handleList", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 0))
    private void errorMixin(Logger logger, String string, Object object) {
        if (!RecipeRemover.CONFIG.recipe_list.contains(((Identifier) object).toString()))
            logger.error("Tried to load unrecognized recipe: {} removed now.", (Identifier) object);
    }
}
