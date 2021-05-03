package net.fenrir.mementomori.mixin;

import ladysnake.requiem.api.v1.possession.Possessable;
import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import ladysnake.requiem.common.impl.possession.PossessionComponentImpl;
import net.fenrir.mementomori.Gameplay.SummonPossessable;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.logging.Level;
import java.util.logging.Logger;

@Mixin(PossessionComponentImpl.class)
public abstract class PossessionComponentImplMixin {


    @Shadow
    @Final
    private PlayerEntity player;

    @Shadow
    public abstract MobEntity getPossessedEntity();

    @Inject(method = "canBeCured", at = @At("RETURN"), cancellable = true, remap = false)
    private void canEat(ItemStack cure, CallbackInfoReturnable<Boolean> cir) {
        MobEntity possessedEntity = getPossessedEntity();
        if (possessedEntity != null) {
            if (MementoMori.CURE_ALLS.contains(cure.getItem()) && player.world.getGameRules().getBoolean(MementoMori.cureAlls)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(
        method = "startPossessing0",
        at = @At("TAIL"),
        remap = false
    )
    public void avoidPrematureDeath(MobEntity host, Possessable possessable, CallbackInfo ci) {
        StatusEffectInstance attrition = player.getStatusEffect(RequiemStatusEffects.ATTRITION);
        if (attrition != null && attrition.getDuration() == player.world.getGameRules().getInt(MementoMori.attritionTime)*20 && attrition.getAmplifier() < 3) {
            player.removeStatusEffect(RequiemStatusEffects.ATTRITION);
            player.addStatusEffect(new StatusEffectInstance(
                RequiemStatusEffects.ATTRITION,
                1,
                attrition.getAmplifier() + 1,
                false,
                false,
                true
            ));
        }
    }

    @Inject(
        method = "stopPossessing(Z)V",
        at = @At("TAIL"),
        remap = false
    )
    public void spawnNewHost(boolean transfer, CallbackInfo ci) {
        if (transfer && !player.world.isClient) {
            if (getPossessedEntity() == null || getPossessedEntity().isDead()) {
                SummonPossessable.spawnPossessable(player.getBlockPos(),player.world);
            }
        }
    }

}
