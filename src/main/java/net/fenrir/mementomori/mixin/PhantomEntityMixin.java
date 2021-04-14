package net.fenrir.mementomori.mixin;

import ladysnake.requiem.api.v1.possession.Possessable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomEntity.class)
public abstract class PhantomEntityMixin extends MobEntityMixin {
    protected PhantomEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }
    private LivingEntity trueTarget;
    @Override
    public void target(LivingEntity target, CallbackInfo ci) {
        if (target == null) {
            if (trueTarget == null) {
                ci.cancel();
            }
        }  else if (((Possessable) target).isBeingPossessed()) {
            trueTarget = target;
        }
    }

    @Override
    public void retrieve(CallbackInfoReturnable<LivingEntity>  cir) {
        if (trueTarget != null) {
            cir.setReturnValue(trueTarget);
        }
    }
}
