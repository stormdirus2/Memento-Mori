package net.fenrir.mementomori.Gameplay;

import net.fenrir.mementomori.MementoMori;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.mob.MobEntity;

public class Mudsoaked extends StatusEffect {

    public Mudsoaked() {
        super(StatusEffectType.BENEFICIAL, 0x9C694E);
    }

    public static void add(LivingEntity entity) {
        entity.addStatusEffect(new StatusEffectInstance(
                MementoMori.MUDSOAKED,
                600,
                0,
                false,
                false,
                true
        ));
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof MobEntity && ((BurnsInDaylightInterface) entity).getBurnsInDaylight() && entity.world.isDay() && !entity.hasStackEquipped(EquipmentSlot.HEAD) && entity.world.isSkyVisible(entity.getBlockPos()) && !entity.isTouchingWaterOrRain()) {
            entity.damage(DamageSource.ON_FIRE, 0.1F);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
