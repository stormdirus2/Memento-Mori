package net.fenrir.mementomori.mixin;

import ladysnake.requiem.api.v1.possession.PossessionComponent;
import ladysnake.requiem.api.v1.remnant.RemnantComponent;
import ladysnake.requiem.api.v1.remnant.StickyStatusEffect;
import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import ladysnake.requiem.common.remnant.RemnantTypes;
import ladysnake.requiem.common.tag.RequiemEntityTypeTags;
import net.fenrir.mementomori.Gameplay.Satiation;
import net.fenrir.mementomori.Gameplay.SoulDamage;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
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
            MobEntity possessed = possessionComponent.getPossessedEntity();
            if (Satiation.isValidMob(possessed)) {
                RemnantComponent remnantComponent = RemnantComponent.get(player);
                if (StickyStatusEffect.shouldStick(this.getEffectType(), player) && remnantComponent.getRemnantType() != RemnantTypes.WANDERING_SPIRIT) {
                    if (player.getStatusEffect(MementoMori.SATIATION) == null && this.getEffectType() == RequiemStatusEffects.ATTRITION && MementoMori.getAttritionGrowth(livingEntity.world) && (amplifier < 3 || duration < attritionTime)) {
                        duration++;
                        if (duration >= attritionTime && amplifier < 3) {
                            SoulDamage.IncrementSoul(player, 1, false);
                        }
                    }
                }
            }
        }
    }
}