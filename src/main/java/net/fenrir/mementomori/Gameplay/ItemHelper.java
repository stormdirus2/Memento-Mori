package net.fenrir.mementomori.Gameplay;

import net.fenrir.mementomori.MementoMori;
import net.minecraft.item.Item;

public class ItemHelper {

    public static boolean cementUse(Item item) {
        return item.isIn(MementoMori.CURE_ALLS) || item.equals(MementoMori.EAU_DE_MORT) || item.equals(MementoMori.ROASTED_SPIDER_EYE);
    }
}
