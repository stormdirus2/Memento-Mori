package net.fenrir.mementomori.mixin;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends Entity {

    public MobEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "setTarget", at = @At("HEAD"),cancellable = true)
    public void target(LivingEntity target, CallbackInfo ci) {
        // Overridden
    }

    @Inject(method = "getTarget", at = @At("HEAD"),cancellable = true)
    public void retrieve(CallbackInfoReturnable<LivingEntity>  cir) {
        // Overridden
    }

}