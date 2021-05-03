package net.fenrir.mementomori.mixin;

import ladysnake.requiem.api.v1.possession.PossessionComponent;
import ladysnake.requiem.api.v1.remnant.RemnantComponent;
import net.fenrir.mementomori.Gameplay.SummonPossessable;
import net.fenrir.mementomori.Gameplay.Unposessable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Unposessable {
    private MobEntity last;
    private MobEntity lastOffered;

    @Inject(
        method = "onDeath",
        at = @At("TAIL")
    )
    public void spawnNewHost(DamageSource source, CallbackInfo ci) {
        if (RemnantComponent.get((PlayerEntity) (Object) this).isIncorporeal()) {
            LivingEntity entity = PossessionComponent.get((PlayerEntity) (Object) this).getPossessedEntity();
            if (entity == null || entity.isDead()) {
                SummonPossessable.spawnPossessable(getBlockPos(),world);
            }
        }
    }

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public MobEntity getLast() {
        return last;
    }

    @Override
    public void setLast(MobEntity newLast) {
        last = newLast;
    }

    @Override
    public MobEntity getLastOffered() {
        return lastOffered;
    }

    @Override
    public void setLastOffered(MobEntity newLast) {
        lastOffered = newLast;
    }

}
