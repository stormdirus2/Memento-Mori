package net.fenrir.mementomori.mixin;

import ladysnake.requiem.common.entity.SkeletonBoneComponent;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkeletonBoneComponent.class)
public abstract class SkeletonBoneComponentMixin {
    @Final @Shadow private MobEntity owner;
    private float originalHealth;
    @Inject(method = "replaceBone", at = @At("HEAD"), cancellable = true, remap = false)
    private void recordHeal(CallbackInfoReturnable<Boolean> cir) {
        originalHealth = this.owner.getHealth();
    }
    @Inject(method = "replaceBone", at = @At("RETURN"), cancellable = true, remap = false)
    private void decreaseHeal(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            float hpGone = this.owner.getHealth() - this.originalHealth;
            if (hpGone > 2.0F) {
                this.owner.heal(2.0F - hpGone);
            }
        }
    }

}
