package net.reciperemover;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.reciperemover.config.RecipeRemoverConfig;
import net.reciperemover.network.RecipeRemoverPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

import java.util.ArrayList;
import java.util.List;

public class RecipeRemover implements ModInitializer {

    public static RecipeRemoverConfig CONFIG = new RecipeRemoverConfig();
    public static final Logger LOGGER = LogManager.getLogger("RecipeRemover");
    public static final Identifier PACKET_ID = new Identifier("reciperemover", "reciperemover_packet");

    @Override
    public void onInitialize() {
        // Registering config
        AutoConfig.register(RecipeRemoverConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(RecipeRemoverConfig.class).getConfig();

        // Register server packet handler when a player joins
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {

            // Collecting all recipes from the config
            List<String> recipeStrings = new ArrayList<>(CONFIG.recipeList);

            // Prepare and send packet to client
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeCollection(recipeStrings, PacketByteBuf::writeString);
            ServerPlayNetworking.send(handler.player, PACKET_ID, buf);

            // Removing items from player's inventory based on config
            if (!CONFIG.itemList.isEmpty()) {
                for (int i = 0; i < handler.getPlayer().getInventory().size(); i++) {
                    String itemId = Registries.ITEM.getId(handler.getPlayer().getInventory().getStack(i).getItem()).toString();
                    if (CONFIG.itemList.contains(itemId)) {
                        handler.getPlayer().getInventory().removeStack(i);
                    }
                }
            }
        });

        // Register the packet handler
        RecipeRemoverPacket.registerServerReceiver();
    }
}
