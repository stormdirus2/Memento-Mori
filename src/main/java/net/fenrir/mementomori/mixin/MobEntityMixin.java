package net.fenrir.mementomori.mixin;


import ladysnake.requiem.api.v1.possession.Possessable;
import net.fenrir.mementomori.Gameplay.AngerHelper;
import net.fenrir.mementomori.Gameplay.BurnsInDaylightInterface;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntityMixin implements BurnsInDaylightInterface {


    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract boolean canTarget(EntityType<?> type);

    @Shadow
    public abstract void setTarget(@Nullable LivingEntity target);

    @Inject(method = "getTarget", at = @At("RETURN"), cancellable = true)
    public void retrieve(CallbackInfoReturnable<LivingEntity> cir) {
        // Overridden
    }

    @Inject(
            method = "isAffectedByDaylight",
            at = @At("RETURN"),
            cancellable = true
    )
    public void mudEffect(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            cir.setReturnValue(getStatusEffect(MementoMori.MUDSOAKED) == null);
        }
    }

    @Override
    public void noTouchyTouch(Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            Possessable host = (Possessable) entity;
            if (host != null && host.isBeingPossessed() && AngerHelper.shouldAttack(living, host.getPossessor())) {
                this.setTarget(living);
            }
        }
    }

    @Override
    public boolean getBurnsInDaylight() {
        return false;
    }

}
