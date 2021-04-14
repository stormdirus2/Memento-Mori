package net.fenrir.mementomori.mixin;

import net.fenrir.mementomori.Gameplay.Unposessable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements Unposessable {
    @Shadow @Final public PlayerInventory inventory;
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
