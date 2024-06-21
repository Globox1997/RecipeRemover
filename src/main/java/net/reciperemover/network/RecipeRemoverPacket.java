package net.reciperemover.network;

import java.util.List;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RecipeRemoverPacket(List<String> recipeStrings) implements CustomPayload {

    public static final PacketCodec<PacketByteBuf, RecipeRemoverPacket> PACKET_CODEC = CustomPayload.codecOf(RecipeRemoverPacket::write, RecipeRemoverPacket::new);

    public static final CustomPayload.Id<RecipeRemoverPacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of("reciperemover", "reciperemover_packet"));

    private RecipeRemoverPacket(PacketByteBuf buf) {
        this(buf.readList(PacketByteBuf::readString));
    }

    private void write(PacketByteBuf buf) {
        buf.writeCollection(this.recipeStrings, PacketByteBuf::writeString);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
