package net.fenrir.mementomori.mixin;

import ladysnake.requiem.Requiem;
import ladysnake.requiem.api.v1.event.requiem.PossessionStartCallback;
import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import ladysnake.requiem.common.remnant.BasePossessionHandlers;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BasePossessionHandlers.class)
public abstract class BasePossessionHandlersMixin {


    @Inject(method = "register",at = @At("HEAD"),cancellable = true,remap = false)
    private static void register2(CallbackInfo ci) {
        PossessionStartCallback.EVENT.register(Requiem.id("soft_hardcore"), (target, possessor, simulate) -> {
            StatusEffectInstance effect = possessor.getStatusEffect(RequiemStatusEffects.ATTRITION);
            if (effect != null && effect.getAmplifier() > 3) {
                return PossessionStartCallback.Result.DENY;
            }
            return PossessionStartCallback.Result.PASS;
        });
    }
}
