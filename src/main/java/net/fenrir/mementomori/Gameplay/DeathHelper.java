package net.fenrir.mementomori.Gameplay;

import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

public class DeathHelper {

    public static void kill(PlayerEntity target) {
        int attritionTime = target.world.getGameRules().getInt(MementoMori.attritionTime);
        StatusEffectInstance effect = target.getStatusEffect(RequiemStatusEffects.ATTRITION);
        if (effect == null || effect.getAmplifier() < 3) {
            SoulDamage.IncrementSoul(target, target.world.getLevelProperties().isHardcore() ? 2 * attritionTime : attritionTime);
        } else if (effect.getDuration() < attritionTime * 20) {
            target.removeStatusEffect(RequiemStatusEffects.ATTRITION);
            target.addStatusEffect(new StatusEffectInstance(
                    RequiemStatusEffects.ATTRITION,
                    attritionTime * 20,
                    3,
                    false,
                    false,
                    true
            ));
        }
    }

}
