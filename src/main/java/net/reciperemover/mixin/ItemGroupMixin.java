package net.reciperemover.mixin;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.reciperemover.RecipeRemover;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mixin(ItemGroup.class)
public class ItemGroupMixin {

    @Shadow
    private Collection<ItemStack> displayStacks;
    @Shadow
    private Set<ItemStack> searchTabStacks;

    @Inject(method = "getDisplayStacks", at = @At("HEAD"), cancellable = true)
    private void getDisplayStacksMixin(CallbackInfoReturnable<Collection<ItemStack>> info) {
        if (displayStacks.stream().anyMatch(stack -> RecipeRemover.CONFIG.itemList.contains(Registries.ITEM.getId(stack.getItem()).toString()))) {
            List<ItemStack> stacks = new ArrayList<>();
            for (ItemStack stack : displayStacks) {
                if (!RecipeRemover.CONFIG.itemList.contains(Registries.ITEM.getId(stack.getItem()).toString())) {
                    stacks.add(stack);
                }
            }
            info.setReturnValue(stacks);
        }
    }

    @Inject(method = "getSearchTabStacks", at = @At("HEAD"), cancellable = true)
    private void getSearchTabStacksMixin(CallbackInfoReturnable<Collection<ItemStack>> info) {
        if (searchTabStacks.stream().anyMatch(stack -> RecipeRemover.CONFIG.itemList.contains(Registries.ITEM.getId(stack.getItem()).toString()))) {
            List<ItemStack> stacks = new ArrayList<>();
            for (ItemStack stack : searchTabStacks) {
                if (!RecipeRemover.CONFIG.itemList.contains(Registries.ITEM.getId(stack.getItem()).toString())) {
                    stacks.add(stack);
                }
            }
            info.setReturnValue(stacks);
        }
    }

    @Inject(method = "contains", at = @At("HEAD"), cancellable = true)
    private void containsMixin(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if (RecipeRemover.CONFIG.itemList.contains(Registries.ITEM.getId(stack.getItem()).toString())) {
            info.setReturnValue(false);
        }
    }

}
