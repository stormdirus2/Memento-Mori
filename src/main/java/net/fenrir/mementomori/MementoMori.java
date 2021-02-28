package net.fenrir.mementomori;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MementoMori implements ModInitializer {
    public static final String MOD_ID = "mementomori";
    public static final String MOD_NAME = "Memento Mori";

    public static final Tag<Item> CURE_ALLS = TagRegistry.item(new Identifier(MOD_ID,"cure_alls"));

    @Override
    public void onInitialize() {
        Logger.getLogger(MOD_ID).log(Level.INFO, "[" + MOD_NAME + "] " + "Initialized");
    }
}

