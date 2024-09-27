package net.reciperemover.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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
    @WrapOperation(method = "handleList", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 0))
    private void errorMixin(Logger instance, String s, Object o, Operation<Void> original) {
        if (!RecipeRemover.CONFIG.recipeList.contains(((Identifier) o).toString())) {
            original.call(instance, s, o);
        }
    }

}
