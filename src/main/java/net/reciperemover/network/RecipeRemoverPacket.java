package net.reciperemover.network;

import java.util.List;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class RecipeRemoverPacket {

    public static final Identifier PACKET_ID = new Identifier("reciperemover", "reciperemover_packet");
    private final List<String> recipeStrings;

    public RecipeRemoverPacket(List<String> recipeStrings) {
        this.recipeStrings = recipeStrings;
    }

    public static RecipeRemoverPacket fromByteBuf(PacketByteBuf buf) {
        return new RecipeRemoverPacket(buf.readList(PacketByteBuf::readString));
    }

    public void toByteBuf(PacketByteBuf buf) {
        buf.writeCollection(this.recipeStrings, PacketByteBuf::writeString);
    }

    public List<String> getRecipeStrings() {
        return recipeStrings;
    }

    public static void sendToServer(RecipeRemoverPacket packet) {
        PacketByteBuf buf = PacketByteBufs.create();
        packet.toByteBuf(buf);
        // Sending the packet to the server
        ClientPlayNetworking.send(PACKET_ID, buf);
    }

    public static void registerServerReceiver() {
        // Registering the receiver to handle packets on the server side
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, (server, player, handler, buf, responseSender) -> {
            RecipeRemoverPacket packet = RecipeRemoverPacket.fromByteBuf(buf);
            server.execute(() -> {
                // Handle the received packet here on the server
                List<String> recipes = packet.getRecipeStrings();
                // Implement your custom logic here
            });
        });
    }
}
