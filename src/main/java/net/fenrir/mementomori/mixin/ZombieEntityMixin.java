package net.fenrir.mementomori.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends MobEntityMixin {

    protected ZombieEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract boolean burnsInDaylight();

    @Override
    public boolean getBurnsInDaylight() {
        return burnsInDaylight();
    }

}
