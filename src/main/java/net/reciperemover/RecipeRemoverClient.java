package net.reciperemover;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class RecipeRemoverClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(RecipeRemover.RECIPE_PACKET, (client, handler, buf, sender) -> {

            List<String> list = new ArrayList<String>();
            while (buf.isReadable())
                list.add(buf.readString());
            client.execute(() -> {
                RecipeRemover.CONFIG.recipeList.clear();
                RecipeRemover.CONFIG.recipeList.addAll(list);
            });

        });
    }

}
