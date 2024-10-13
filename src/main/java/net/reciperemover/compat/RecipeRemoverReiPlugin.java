package net.reciperemover.compat;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.reciperemover.RecipeRemover;

@Environment(EnvType.CLIENT)
public class RecipeRemoverReiPlugin implements REIClientPlugin {

    @Override
    public void registerEntries(EntryRegistry registry) {
        REIClientPlugin.super.registerEntries(registry);

        for (int i = 0; i < RecipeRemover.CONFIG.recipeList.size(); i++) {
            Item item = Registries.ITEM.get(new Identifier(RecipeRemover.CONFIG.recipeList.get(i)));
            if (!item.equals(Items.AIR)) {
                registry.removeEntry(EntryStack.of(VanillaEntryTypes.ITEM, item.getDefaultStack()));
            } else if (RecipeRemover.CONFIG.printErrorMessage) {
                RecipeRemover.LOGGER.error("Failed to remove item with identifier \"{}\"", RecipeRemover.CONFIG.recipeList.get(i));
            }
        }
        for (int i = 0; i < RecipeRemover.CONFIG.itemList.size(); i++) {
            Item item = Registries.ITEM.get(new Identifier(RecipeRemover.CONFIG.itemList.get(i)));
            if (!item.equals(Items.AIR)) {
                registry.removeEntry(EntryStack.of(VanillaEntryTypes.ITEM, item.getDefaultStack()));
            } else if (RecipeRemover.CONFIG.printErrorMessage) {
                RecipeRemover.LOGGER.error("Failed to remove item with identifier \"{}\"", RecipeRemover.CONFIG.itemList.get(i));
            }
        }
    }

}