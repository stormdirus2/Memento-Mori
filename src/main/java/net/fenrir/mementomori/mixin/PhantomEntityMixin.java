package net.fenrir.mementomori.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomEntity.class)
public abstract class PhantomEntityMixin extends MobEntityMixin {


    private boolean takeBreak = false;

    protected PhantomEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void retrieve(CallbackInfoReturnable<LivingEntity> cir) {
        if (takeBreak) {
            LivingEntity target = cir.getReturnValue();
            if (target == null || world.getLightLevel(target.getBlockPos()) > 4) {
                cir.setReturnValue(null);
                return;
            } else {
                takeBreak = false;
            }
        }
        if (world.getLightLevel(this.getBlockPos()) > 4) {
            takeBreak = true;
            cir.setReturnValue(null);
        }
    }

    @Override
    public boolean getBurnsInDaylight() {
        return true;
    }

}
