package net.fenrir.mementomori.mixin;

import ladysnake.requiem.api.v1.possession.Possessable;
import ladysnake.requiem.api.v1.possession.PossessionComponent;
import ladysnake.requiem.api.v1.remnant.RemnantComponent;
import ladysnake.requiem.api.v1.remnant.StickyStatusEffect;
import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import ladysnake.requiem.common.remnant.RemnantTypes;
import net.fenrir.mementomori.Gameplay.SoulDamage;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = StatusEffectInstance.class, priority = 1001)
public abstract class StatusEffectInstanceMixin {
    @Shadow
    private int duration;
    @Shadow
    private int amplifier;

    @Shadow
    public abstract StatusEffect getEffectType();

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;updateDuration()I"))
    private void preventSoulboundCountdown(LivingEntity livingEntity, Runnable r, CallbackInfoReturnable<Boolean> cir) {
        int attritionTime = livingEntity.world.getGameRules().getInt(MementoMori.attritionTime) * 20;
        if (livingEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity;
            PossessionComponent possessionComponent = PossessionComponent.get(player);
            if (possessionComponent.isPossessing()) {
                RemnantComponent remnantComponent = RemnantComponent.get(player);
                if (StickyStatusEffect.shouldStick(this.getEffectType(), livingEntity) && remnantComponent.getRemnantType() != RemnantTypes.WANDERING_SPIRIT) {
                    if (livingEntity.getStatusEffect(MementoMori.SATIATION) == null && this.getEffectType() == RequiemStatusEffects.ATTRITION && MementoMori.getAttritionGrowth(livingEntity.world) && (amplifier < 3 || duration < (attritionTime - 1))) {
                        duration++;
                        if (duration >= attritionTime) {
                            SoulDamage.IncrementSoul(livingEntity, 1);
                        }
                    }
                }
            }
        }
    }
}