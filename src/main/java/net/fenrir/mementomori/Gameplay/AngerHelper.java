package net.fenrir.mementomori.Gameplay;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;

public class AngerHelper {

    public static boolean shouldAttack(LivingEntity entity, PlayerEntity playerEntity) {
        if (entity instanceof HostileEntity) {
            return entity.canTarget(EntityType.PLAYER);
        } else if (entity instanceof Angerable && !(entity instanceof TameableEntity)) {
            return ((Angerable) entity).shouldAngerAt(playerEntity) && entity.canTarget(EntityType.PLAYER);
        }
        return false;
    }

}
