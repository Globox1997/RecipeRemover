package net.reciperemover.compat;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.reciperemover.RecipeRemover;

public class RecipeRemoverReiPlugin implements REIClientPlugin {

    @Override
    public void registerEntries(EntryRegistry registry) {
        REIClientPlugin.super.registerEntries(registry);

        for (int i = 0; i < RecipeRemover.CONFIG.recipe_list.size(); i++) {
            Item item = Registry.ITEM.get(new Identifier(RecipeRemover.CONFIG.recipe_list.get(i)));

            if (!item.equals(Items.AIR))
                registry.removeEntry(EntryStack.of(VanillaEntryTypes.ITEM, item.getDefaultStack()));
            else if (RecipeRemover.CONFIG.printErrorMessage)
                RecipeRemover.LOGGER.error("Failed to remove item with identifier \"{}\"", RecipeRemover.CONFIG.recipe_list.get(i));
        }
    }

}
