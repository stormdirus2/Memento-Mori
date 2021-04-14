package net.fenrir.mementomori.mixin;

import ladysnake.requiem.api.v1.remnant.StickyStatusEffect;
import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import net.fenrir.mementomori.Gameplay.SoulDamage;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = StatusEffectInstance.class,priority = 1001)
public abstract class StatusEffectInstanceMixin {
    @Shadow
    public abstract StatusEffect getEffectType();

    @Shadow private int duration;

    @Shadow private int amplifier;

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;updateDuration()I"))
    private void preventSoulboundCountdown(LivingEntity livingEntity, Runnable r, CallbackInfoReturnable<Boolean> cir) {
        int attritionTime = livingEntity.world.getGameRules().getInt(MementoMori.attritionTime)*20;
        if (StickyStatusEffect.shouldStick(this.getEffectType(), livingEntity)) {
            if (livingEntity.getStatusEffect(MementoMori.SATIATION) == null && this.getEffectType() == RequiemStatusEffects.ATTRITION && MementoMori.getAttritionGrowth(livingEntity.world) && (amplifier < 3 || duration < attritionTime)) {
                duration++;
                if (duration >= attritionTime) {
                    SoulDamage.IncrementSoul(livingEntity,1);
                }
            }
        }
    }
}