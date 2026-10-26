package net.reciperemover.compat;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.reciperemover.RecipeRemover;

@Environment(EnvType.CLIENT)
public class RecipeRemoverReiPlugin implements REIClientPlugin {

    @Override
    public void registerEntries(EntryRegistry registry) {
        REIClientPlugin.super.registerEntries(registry);

        for (Item item : RecipeRemover.getRemovedItems()) {
            registry.removeEntry(EntryStack.of(VanillaEntryTypes.ITEM, item.getDefaultStack()));
        }
    }

}
