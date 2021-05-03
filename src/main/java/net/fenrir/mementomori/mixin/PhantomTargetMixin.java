package net.fenrir.mementomori.mixin;

import ladysnake.requiem.api.v1.possession.Possessable;
import ladysnake.requiem.api.v1.remnant.RemnantComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PhantomEntity.class)
public abstract class PhantomTargetMixin extends LivingEntity {

    protected PhantomTargetMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean canTarget(LivingEntity entity) {
        Possessable host = (Possessable) entity;
        PlayerEntity playerEntity = entity instanceof PlayerEntity ? (PlayerEntity) entity : null;
        if (playerEntity != null) {
            RemnantComponent component = RemnantComponent.get(playerEntity);
            return !component.isIncorporeal();
        }
        return host != null && host.isBeingPossessed();
    }
}
