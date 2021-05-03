package net.fenrir.mementomori.Gameplay;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public interface Unposessable {
    static Optional<Unposessable> of(PlayerEntity entity) {
        if (entity instanceof Unposessable) {
            return Optional.of((Unposessable) entity);
        }
        return Optional.empty();
    }

    MobEntity getLast();

    MobEntity getLastOffered();

    void setLastOffered(MobEntity last);

    void setLast(MobEntity last);
}