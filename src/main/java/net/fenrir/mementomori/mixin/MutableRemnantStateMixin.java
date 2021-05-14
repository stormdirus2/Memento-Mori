package net.fenrir.mementomori.mixin;

import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import ladysnake.requiem.common.impl.remnant.MutableRemnantState;
import net.fenrir.mementomori.Gameplay.DeathHelper;
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

    @Shadow
    @Final
    protected PlayerEntity player;

    @Inject(method = "prepareRespawn", at = @At("HEAD"), cancellable = true, remap = false)
    private void addAttrition(ServerPlayerEntity original, boolean lossless, CallbackInfo ci) {
        if (!lossless && original.isDead()) {
            StatusEffectInstance effect = original.getStatusEffect(RequiemStatusEffects.ATTRITION);
            if (effect != null) {
                player.addStatusEffect(effect);
            }
            DeathHelper.kill(player);
        }
    }

}
