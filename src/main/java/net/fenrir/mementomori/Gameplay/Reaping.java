package net.fenrir.mementomori.Gameplay;

import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import moriyashiine.onsoulfire.interfaces.OnSoulFireAccessor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;

public class Reaping extends Enchantment {

    public Reaping() {
        super(Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 20;
    }

    public int getMaxPower(int level) {
        return super.getMinPower(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if(target instanceof LivingEntity) {
            ((OnSoulFireAccessor) target).setOnSoulFire(true);
            StatusEffectInstance attrition = ((LivingEntity) target).getStatusEffect(RequiemStatusEffects.ATTRITION);
            if (attrition != null) {
                float power = attrition.getAmplifier() + 1;
                target.damage(DamageSource.MAGIC,power*3);
            }
        }
        super.onTargetDamaged(user, target, level);
    }
}
