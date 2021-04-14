package net.fenrir.mementomori;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
        FabricModelPredicateProviderRegistry.register(MementoMori.REPLACE_BOOK, (stack, world, entity) -> {
            ListTag enchantments = EnchantedBookItem.getEnchantmentTag(stack);
            for (int i = 0; i < enchantments.size(); i++) {
                CompoundTag tag = enchantments.getCompound(i);
                Identifier enchantId = Identifier.tryParse(tag.getString("id"));
                if (enchantId != null && (enchantId.equals(MementoMori.reaping) || enchantId.equals(MementoMori.soulCleaving))) {
                    return tag.getInt("lvl");
                }
            }
            return 0F;
        });
    }
}
