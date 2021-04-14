package net.fenrir.mementomori;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.util.Identifier;

public class MementoMori_CLIENT implements ClientModInitializer {

    public static boolean blastUnphasable = true;
    public static boolean attritionGrowth = true;

    @Override
    public void onInitializeClient() {
        ClientSidePacketRegistry.INSTANCE.register(new Identifier("mementomori:blast_unphasable"), (packetContext, packetByteBuf) -> {
            blastUnphasable = packetByteBuf.readBoolean();
        });
        ClientSidePacketRegistry.INSTANCE.register(new Identifier("mementomori:attrition_growth"), (packetContext, packetByteBuf) -> {
            attritionGrowth = packetByteBuf.readBoolean();
        });
    }
}
