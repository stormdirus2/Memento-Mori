package net.fenrir.mementomori;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MementoMori implements ModInitializer {
    public static final String MOD_ID = "mementomori";
    public static final String MOD_NAME = "Memento Mori";

    public static boolean getPermaDeath(World world) {
        if (world.isClient()) {
            return MementoMori_CLIENT.permaDeath;
        } else {
            return world.getGameRules().getBoolean(permaDeath);
        }
    }

    public static boolean getBlastUnphasable(World world) {
        if (world.isClient()) {
            return MementoMori_CLIENT.blastUnphasable;
        } else {
            return world.getGameRules().getBoolean(blastUnphasable);
        }
    }

    public static final Tag<Item> CURE_ALLS = TagRegistry.item(new Identifier(MOD_ID,"cure_alls"));

    public static final GameRules.Key<GameRules.BooleanRule> permaDeath =
            register("attritionPermaDeath", GameRuleFactory.createBooleanRule(true, (server, rule) -> {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBoolean(rule.get());
                server.getPlayerManager().sendToAll(ServerSidePacketRegistry.INSTANCE.toPacket(new Identifier("mementomori:perma_death"), buf));
            }), GameRules.Category.PLAYER);
    public static final GameRules.Key<GameRules.BooleanRule> cureAlls =
            register("cureAllItems", GameRuleFactory.createBooleanRule(true), GameRules.Category.PLAYER);
    public static final GameRules.Key<GameRules.BooleanRule> slowFade =
            register("slowAttritionFade", GameRuleFactory.createBooleanRule(true), GameRules.Category.PLAYER);
    public static final GameRules.Key<GameRules.BooleanRule> blastUnphasable =
            register("blastResistantUnphasable", GameRuleFactory.createBooleanRule(true, (server, rule) -> {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBoolean(rule.get());
                server.getPlayerManager().sendToAll(ServerSidePacketRegistry.INSTANCE.toPacket(new Identifier("mementomori:blast_unphasable"), buf));
            }), GameRules.Category.PLAYER);
    public static final GameRules.Key<GameRules.IntRule> attritionTime =
            register("attritionTime", GameRuleFactory.createIntRule(1200, 0), GameRules.Category.PLAYER);

    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(handler.player.world.getGameRules().getBoolean(permaDeath));
            ServerPlayNetworking.send(handler.player,new Identifier("mementomori:perma_death"), buf);

            PacketByteBuf buf2 = PacketByteBufs.create();
            buf2.writeBoolean(handler.player.world.getGameRules().getBoolean(blastUnphasable));
            ServerPlayNetworking.send(handler.player,new Identifier("mementomori:blast_unphasable"), buf2);
        });
        Logger.getLogger(MOD_ID).log(Level.INFO, "[" + MOD_NAME + "] " + "Initialized");
    }

    private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Type<T> type, GameRules.Category category) {
        return GameRuleRegistry.register(MOD_ID + ":" + name, category, type);
    }
}


