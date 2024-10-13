package net.reciperemover;

import java.util.List;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.reciperemover.network.RecipeRemoverPacket;

@Environment(EnvType.CLIENT)
public class RecipeRemoverClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(RecipeRemoverPacket.PACKET_TYPE, (payload, context) -> {
            List<String> recipeStrings = payload.recipeStrings();

            context.client().execute(() -> {
                RecipeRemover.CONFIG.recipeList.clear();
                RecipeRemover.CONFIG.recipeList.addAll(recipeStrings);
            });
        });
    }
}
