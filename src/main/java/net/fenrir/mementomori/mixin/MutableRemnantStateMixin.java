package net.fenrir.mementomori.mixin;

import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import ladysnake.requiem.common.impl.remnant.MutableRemnantState;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(MutableRemnantState.class)
public abstract class MutableRemnantStateMixin {

    @Shadow @Final protected PlayerEntity player;

    @Inject(method = "prepareRespawn", at = @At("RETURN"), cancellable = true, remap = false)
    private void addAttrition(ServerPlayerEntity original, boolean lossless, CallbackInfo ci) {
        StatusEffectInstance effect = original.getStatusEffect(RequiemStatusEffects.ATTRITION);
        if (effect != null) {
            if (effect.getAmplifier() < 3) {
                this.player.addStatusEffect(new StatusEffectInstance(
                        RequiemStatusEffects.ATTRITION,
                        this.player.world.getGameRules().getInt(MementoMori.attritionTime) * 20,
                        effect.getAmplifier() + 1,
                        false,
                        false,
                        true
                ));
            } else {
                this.player.addStatusEffect(new StatusEffectInstance(
                        RequiemStatusEffects.ATTRITION,
                        this.player.world.getGameRules().getInt(MementoMori.attritionTime) * 20,
                        effect.getAmplifier(),
                        false,
                        false,
                        true
                ));
            }
        }
    }
}
