package net.fenrir.mementomori.mixin;

import moriyashiine.onsoulfire.interfaces.OnSoulFireAccessor;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArrowEntity.class)
public class ArrowEntityMixin {
    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)V", at = @At("TAIL"))
    private void addSoulFire(World world, LivingEntity owner, CallbackInfo ci) {
        if (EnchantmentHelper.getLevel(MementoMori.REAPING, owner.getActiveItem()) > 0) {
            ((OnSoulFireAccessor) this).setOnSoulFire(true);
        }
    }
}
