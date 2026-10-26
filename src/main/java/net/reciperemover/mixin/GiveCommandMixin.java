package net.reciperemover.mixin;

import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.GiveCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.reciperemover.RecipeRemover;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(GiveCommand.class)
public class GiveCommandMixin {

    @Inject(method = "execute", at = @At("HEAD"), cancellable = true)
    private static void executeMixin(ServerCommandSource source, ItemStackArgument item, Collection<ServerPlayerEntity> targets, int count, CallbackInfoReturnable<Integer> info) {
        if (RecipeRemover.isItemRemoved(item.getItem())) {
            source.sendError(Text.translatable("commands.give.failed.removed", new ItemStack(item.getItem()).toHoverableText()));
            info.setReturnValue(0);
        }
    }
}
