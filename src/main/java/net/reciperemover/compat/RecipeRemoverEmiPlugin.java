package net.reciperemover.compat;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.reciperemover.RecipeRemover;

@Environment(EnvType.CLIENT)
public class RecipeRemoverEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        for (int i = 0; i < RecipeRemover.CONFIG.recipeList.size(); i++) {

            Item item = Registry.ITEM.get(new Identifier(RecipeRemover.CONFIG.recipeList.get(i)));

            if (!item.equals(Items.AIR))
                registry.removeEmiStacks(EmiStack.of(item));
            else if (RecipeRemover.CONFIG.printErrorMessage)
                RecipeRemover.LOGGER.error("Failed to remove item with identifier \"{}\"", RecipeRemover.CONFIG.recipeList.get(i));
        }
    }

}
