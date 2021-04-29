package net.fenrir.mementomori.mixin;

import net.fenrir.mementomori.Gameplay.Unposessable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements Unposessable {
    private MobEntity last;

    @Override
    public MobEntity getLast() {
        return last;
    }

    @Override
    public void setLast(MobEntity newLast) {
        last = newLast;
    }

}
