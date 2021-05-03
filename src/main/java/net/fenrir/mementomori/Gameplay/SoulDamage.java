package net.fenrir.mementomori.Gameplay;

import ladysnake.requiem.api.v1.remnant.RemnantComponent;
import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

public class SoulDamage {
    public static int getTotalTime(LivingEntity Entity) {
        int time = 0;
        StatusEffectInstance effect = Entity.getStatusEffect(RequiemStatusEffects.ATTRITION);
        if (effect != null) {
            time = effect.getAmplifier() * Entity.world.getGameRules().getInt(MementoMori.attritionTime) * 20 + effect.getDuration();
        }
        return time;
    }

    public static void setTotalTime(LivingEntity Entity, int time) {
        int attritionTime = Entity.world.getGameRules().getInt(MementoMori.attritionTime) * 20;
        Entity.removeStatusEffect(RequiemStatusEffects.ATTRITION);
        int amplifier = Math.min((time - time % attritionTime) / attritionTime, 3);
        Entity.addStatusEffect(new StatusEffectInstance(
                RequiemStatusEffects.ATTRITION,
                (time - amplifier * attritionTime),
                amplifier,
                false,
                false,
                true
        ));
    }

    public static void IncrementSoul(LivingEntity Entity, int Increment) {
        int total = Math.max(getTotalTime(Entity) + Increment * 20, 0);
        int attritionTime = Entity.world.getGameRules().getInt(MementoMori.attritionTime) * 20;
        int deathLevel = Entity.world.getGameRules().getInt(MementoMori.soulDamageDeathLevel);
        int totalTime = attritionTime * (deathLevel - 1);
        if (deathLevel < 0 || (total < totalTime && deathLevel > 0)) {
            setTotalTime(Entity, total);
        } else {
            setTotalTime(Entity, total);
            PlayerEntity player = Entity instanceof PlayerEntity ? (PlayerEntity) Entity : null;
            if (player == null || !RemnantComponent.get(player).isIncorporeal()) {
                Entity.damage(DamageSource.OUT_OF_WORLD, 100);
            }
        }
    }
}
