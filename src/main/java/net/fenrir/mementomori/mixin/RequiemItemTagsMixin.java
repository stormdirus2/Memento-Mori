package net.fenrir.mementomori.mixin;

import ladysnake.requiem.common.tag.RequiemItemTags;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;

@Mixin(RequiemItemTags.class)
public abstract class RequiemItemTagsMixin {
    @Mutable @Shadow @Final public static Tag<Item> BONES = TagRegistry.item(new Identifier(MementoMori.MOD_ID,"bones"));
    @Mutable @Shadow @Final public static Tag<Item> RAW_MEATS = TagRegistry.item(new Identifier(MementoMori.MOD_ID,"raw_meats"));
}
