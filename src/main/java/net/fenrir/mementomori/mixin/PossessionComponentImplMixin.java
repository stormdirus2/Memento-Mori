package net.fenrir.mementomori.mixin;
import ladysnake.requiem.common.impl.possession.PossessionComponentImpl;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PossessionComponentImpl.class)
public abstract class PossessionComponentImplMixin {

    @Shadow public abstract MobEntity getPossessedEntity();

    @Shadow @Final private PlayerEntity player;

    @Inject(method = "canBeCured", at = @At("RETURN"), cancellable = true, remap = false)
    private void addAttrition(ItemStack cure, CallbackInfoReturnable<Boolean> cir) {
        MobEntity possessedEntity = this.getPossessedEntity();
        if (possessedEntity != null && MementoMori.CURE_ALLS.contains(cure.getItem()) && this.player.world.getGameRules().getBoolean(MementoMori.cureAlls)) {
            cir.setReturnValue(true);
        }
    }

}
