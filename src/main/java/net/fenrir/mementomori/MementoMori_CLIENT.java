package net.fenrir.mementomori;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.util.Identifier;

public class MementoMori_CLIENT implements ClientModInitializer {

    public static boolean permaDeath = true;

    public static boolean blastUnphasable = true;

    @Override
    public void onInitializeClient() {
        ClientSidePacketRegistry.INSTANCE.register(new Identifier("mementomori:perma_death"), (packetContext, packetByteBuf) -> {
            permaDeath = packetByteBuf.readBoolean();
        });
        ClientSidePacketRegistry.INSTANCE.register(new Identifier("mementomori:blast_unphasable"), (packetContext, packetByteBuf) -> {
            blastUnphasable = packetByteBuf.readBoolean();
        });
    }
}
