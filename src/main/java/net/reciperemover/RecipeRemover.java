package net.reciperemover;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.reciperemover.config.RecipeRemoverConfig;
import net.reciperemover.network.RecipeRemoverPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeRemover implements ModInitializer {

    public static RecipeRemoverConfig CONFIG = new RecipeRemoverConfig();
    public static final Logger LOGGER = LogManager.getLogger("RecipeRemover");

    private static volatile Set<Item> removedItems;

    @Override
    public void onInitialize() {
        AutoConfig.register(RecipeRemoverConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(RecipeRemoverConfig.class).getConfig();

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> {
            entries.getDisplayStacks().removeIf(stack -> isItemRemoved(stack.getItem()));
            entries.getSearchTabStacks().removeIf(stack -> isItemRemoved(stack.getItem()));
        });

        PayloadTypeRegistry.playS2C().register(RecipeRemoverPacket.PACKET_ID, RecipeRemoverPacket.PACKET_CODEC);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            List<String> recipeStrings = new ArrayList<String>();
            for (int i = 0; i < CONFIG.recipeList.size(); i++) {
                recipeStrings.add(CONFIG.recipeList.get(i));
            }
            ServerPlayNetworking.send(handler.player, new RecipeRemoverPacket(recipeStrings));

            if (!CONFIG.itemList.isEmpty()) {
                for (int i = 0; i < handler.getPlayer().getInventory().size(); i++) {
                    if (isItemRemoved(handler.getPlayer().getInventory().getStack(i).getItem())) {
                        handler.getPlayer().getInventory().removeStack(i);
                    }
                }
            }
        });
    }

    public static Identifier identifierOf(String name) {
        return Identifier.of("reciperemover", name);
    }

    public static Set<Item> getRemovedItems() {
        if (removedItems == null) {
            Set<Item> items = new HashSet<>();
            for (String entry : CONFIG.itemList) {
                Identifier identifier = Identifier.tryParse(entry);
                if (identifier != null && Registries.ITEM.containsId(identifier)) {
                    items.add(Registries.ITEM.get(identifier));
                } else if (CONFIG.printErrorMessage) {
                    LOGGER.error("Failed to remove item with identifier \"{}\"", entry);
                }
            }
            removedItems = items;
        }
        return removedItems;
    }

    public static boolean isItemRemoved(Item item) {
        return getRemovedItems().contains(item);
    }

    public static boolean isRecipeRemoved(Identifier identifier) {
        return CONFIG.recipeList.contains(identifier.toString()) || CONFIG.recipeList.contains(identifier.getNamespace());
    }

    public static boolean isExcepted(Identifier identifier) {
        for (String exception : RecipeRemover.CONFIG.removeAllAdvancementsExcept) {
            if (exception.endsWith(":")) {
                if (identifier.getNamespace().equals(exception.substring(0, exception.length() - 1))) {
                    return true;
                }
            } else if (identifier.equals(Identifier.tryParse(exception))) {
                return true;
            }
        }
        return false;
    }

}
