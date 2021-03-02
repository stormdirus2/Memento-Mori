package net.fenrir.mementomori;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MementoMori implements ModInitializer {
    public static final String MOD_ID = "mementomori";
    public static final String MOD_NAME = "Memento Mori";

    public static final Tag<Item> CURE_ALLS = TagRegistry.item(new Identifier(MOD_ID,"cure_alls"));

    public static final GameRules.Key<GameRules.BooleanRule> permaDeath =
            register("attritionPermaDeath", GameRuleFactory.createBooleanRule(true), GameRules.Category.PLAYER);
    public static final GameRules.Key<GameRules.BooleanRule> cureAlls =
            register("cureAllItems", GameRuleFactory.createBooleanRule(true), GameRules.Category.PLAYER);
    public static final GameRules.Key<GameRules.BooleanRule> slowFade =
            register("slowAttritionFade", GameRuleFactory.createBooleanRule(true), GameRules.Category.PLAYER);
    public static final GameRules.Key<GameRules.BooleanRule> blastUnphasable =
            register("blastResistantUnphasable", GameRuleFactory.createBooleanRule(true), GameRules.Category.PLAYER);
    public static final GameRules.Key<GameRules.IntRule> attritionTime =
            register("attritionTime", GameRuleFactory.createIntRule(1200, 0), GameRules.Category.PLAYER);

    @Override
    public void onInitialize() {
        Logger.getLogger(MOD_ID).log(Level.INFO, "[" + MOD_NAME + "] " + "Initialized");
    }

    private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Type<T> type, GameRules.Category category) {
        return GameRuleRegistry.register(MOD_ID + ":" + name, category, type);
    }
}

