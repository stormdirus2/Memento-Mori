package net.fenrir.mementomori.mixin;

import ladysnake.requiem.common.impl.possession.PossessionComponentImpl;
import net.fenrir.mementomori.Gameplay.SummonPossessable;
import net.fenrir.mementomori.MementoMori;
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

@Mixin(PossessionComponentImpl.class)
public abstract class PossessionComponentImplMixin {


    @Shadow
    @Final
    private PlayerEntity player;
    @Shadow
    private int conversionTimer;

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
            method = "startCuring",
            at = @At("TAIL"),
            remap = false
    )
    public void avoidCuringTimer(CallbackInfo ci) {
        if (!player.world.isClient) {
            conversionTimer = 1;
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
                SummonPossessable.spawnPossessable(player.getBlockPos(), player.world);
            }
        }
    }

}
