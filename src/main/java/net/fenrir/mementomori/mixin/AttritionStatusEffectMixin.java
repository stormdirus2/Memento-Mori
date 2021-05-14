package net.fenrir.mementomori.mixin;

import ladysnake.requiem.common.entity.effect.AttritionStatusEffect;
import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import net.fenrir.mementomori.Gameplay.DeathHelper;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AttritionStatusEffect.class)
public abstract class AttritionStatusEffectMixin {


    @Inject(method = "apply", at = @At("HEAD"), cancellable = true, remap = false)
    private static void apply(PlayerEntity target, CallbackInfo ci) {
        DeathHelper.kill(target);
        ci.cancel();
    }

    @Inject(method = "addAttrition", at = @At("HEAD"), cancellable = true, remap = false)
    private static void addAttrition(LivingEntity target, int amplifier, CallbackInfo ci) {
        target.addStatusEffect(new StatusEffectInstance(
                RequiemStatusEffects.ATTRITION,
                target.world.getGameRules().getInt(MementoMori.attritionTime) * 20,
                amplifier,
                false,
                false,
                true
        ));

        ci.cancel();
    }
}
