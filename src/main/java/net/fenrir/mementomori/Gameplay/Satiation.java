package net.fenrir.mementomori.Gameplay;

import net.fenrir.mementomori.MementoMori;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;

public class Satiation extends StatusEffect {

    public Satiation() {
        super(StatusEffectType.BENEFICIAL, 0xAA3322);
    }

    public static int getTotalTime(LivingEntity Entity) {
        int time = 0;
        StatusEffectInstance effect = Entity.getStatusEffect(MementoMori.SATIATION);
        if (effect != null) {
            time = effect.getDuration();
        }
        return time;
    }
    public static void setTotalTime(LivingEntity Entity, int time) {
        Entity.removeStatusEffect(MementoMori.SATIATION);
        Entity.applyStatusEffect(new StatusEffectInstance(
                MementoMori.SATIATION,
                time,
                0,
                false,
                false,
                true
        ));
    }
    public static void IncrementSatiation (LivingEntity Entity, int Increment) {
        setTotalTime(Entity, Math.max(getTotalTime(Entity) + Increment,0));
    }
}
