package net.reciperemover.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.reciperemover.RecipeRemover;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "isItemEnabled", at = @At("HEAD"), cancellable = true)
    private void isItemEnabledMixin(FeatureSet enabledFeatures, CallbackInfoReturnable<Boolean> info) {
        if (getItem() != null && RecipeRemover.CONFIG.itemList.contains(Registries.ITEM.getId(getItem()).toString())) {
            info.setReturnValue(false);
        }
    }

    @Shadow
    public abstract Item getItem();
}
