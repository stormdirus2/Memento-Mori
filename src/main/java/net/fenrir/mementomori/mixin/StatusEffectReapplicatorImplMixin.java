package net.fenrir.mementomori.mixin;

import ladysnake.requiem.api.v1.internal.StatusEffectReapplicator;
import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ladysnake.requiem.common.entity.effect.StatusEffectReapplicatorImpl;

import java.util.Collection;

@Mixin(StatusEffectReapplicatorImpl.class)
public abstract class StatusEffectReapplicatorImplMixin implements StatusEffectReapplicator {
    @Shadow @Final private Collection<StatusEffectInstance> reappliedEffects;

    @Inject(method = "onStatusEffectRemoved", at = @At("HEAD"), cancellable = true, remap = false)
    protected void noRemove(StatusEffectInstance effect, CallbackInfo ci) {
        if (effect.getEffectType() == RequiemStatusEffects.ATTRITION) {
            if (effect.getDuration() > 0 ) {
                this.reappliedEffects.add(new StatusEffectInstance(
                        RequiemStatusEffects.ATTRITION,
                        effect.getDuration(),
                        effect.getAmplifier(),
                        false,
                        false,
                        true
                ));
            } else if (effect.getAmplifier() > 0) {
                this.reappliedEffects.add(new StatusEffectInstance(
                        RequiemStatusEffects.ATTRITION,
                        24000,
                        effect.getAmplifier() - 1,
                        false,
                        false,
                        true
                ));
            }
            ci.cancel();
        }
    }
}
