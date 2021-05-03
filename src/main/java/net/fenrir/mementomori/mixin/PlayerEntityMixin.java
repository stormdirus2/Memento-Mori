package net.fenrir.mementomori.mixin;

import net.fenrir.mementomori.Gameplay.Unposessable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Unposessable {
    private MobEntity last;

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

}
