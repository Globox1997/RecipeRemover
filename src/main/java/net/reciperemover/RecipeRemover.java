package net.reciperemover;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.Unpooled;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import net.reciperemover.config.RecipeRemoverConfig;

public class RecipeRemover implements ModInitializer {

    public static RecipeRemoverConfig CONFIG = new RecipeRemoverConfig();
    public static final Logger LOGGER = LogManager.getLogger("RecipeRemover");
    public static final Identifier RECIPE_PACKET = new Identifier("reciperemover", "recipes");

    @Override
    public void onInitialize() {
        AutoConfig.register(RecipeRemoverConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(RecipeRemoverConfig.class).getConfig();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            for (int i = 0; i < CONFIG.recipeList.size(); i++)
                buf.writeString(CONFIG.recipeList.get(i));
            CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(RECIPE_PACKET, buf);
            handler.sendPacket(packet);
        });
    }

}