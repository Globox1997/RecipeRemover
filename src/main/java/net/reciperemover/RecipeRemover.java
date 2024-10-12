package net.reciperemover;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.registry.Registries;
import net.reciperemover.config.RecipeRemoverConfig;
import net.reciperemover.network.RecipeRemoverPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RecipeRemover implements ModInitializer {

    public static RecipeRemoverConfig CONFIG = new RecipeRemoverConfig();
    public static final Logger LOGGER = LogManager.getLogger("RecipeRemover");

    @Override
    public void onInitialize() {
        AutoConfig.register(RecipeRemoverConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(RecipeRemoverConfig.class).getConfig();

        PayloadTypeRegistry.playS2C().register(RecipeRemoverPacket.PACKET_ID, RecipeRemoverPacket.PACKET_CODEC);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            List<String> recipeStrings = new ArrayList<String>();
            for (int i = 0; i < CONFIG.recipeList.size(); i++) {
                recipeStrings.add(CONFIG.recipeList.get(i));
            }
            ServerPlayNetworking.send(handler.player, new RecipeRemoverPacket(recipeStrings));

            if (!CONFIG.itemList.isEmpty()) {
                for (int i = 0; i < handler.getPlayer().getInventory().size(); i++) {
                    if (RecipeRemover.CONFIG.itemList.contains(Registries.ITEM.getId(handler.getPlayer().getInventory().getStack(i).getItem()).toString())) {
                        handler.getPlayer().getInventory().removeStack(i);
                    }
                }
            }
        });
    }

}
