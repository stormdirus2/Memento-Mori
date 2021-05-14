package net.fenrir.mementomori.mixin;

import ladysnake.requiem.api.v1.possession.PossessionComponent;
import net.fenrir.mementomori.Gameplay.ItemHelper;
import net.fenrir.mementomori.Gameplay.Mudsoaked;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class, priority = 900)
public abstract class ItemStackMixin {

    private boolean preventOverride;

    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract void decrement(int i);

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        PossessionComponent possessionComponent = PossessionComponent.get(player);
        MobEntity possessedEntity = possessionComponent.getPossessedEntity();
        if (possessedEntity != null) {
            Item item = getItem();
            if (ItemHelper.cementUse(item)) {
                player.setCurrentHand(hand);
                cir.setReturnValue(new TypedActionResult<>(ActionResult.SUCCESS, (ItemStack) (Object) this));
            } else if (item == Items.DIRT) {
                Mudsoaked.add(possessedEntity);
                player.getItemCooldownManager().set(Items.DIRT, 20);
                decrement(1);
                cir.setReturnValue(new TypedActionResult<>(ActionResult.CONSUME, (ItemStack) (Object) this));
            }
        }
    }

    @Inject(method = "finishUsing", at = @At("HEAD"), cancellable = true)
    private void overridePossessedUseEnd(World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (user instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) user;
            PossessionComponent possessionComponent = PossessionComponent.get(player);
            if (possessionComponent.canBeCured((ItemStack) (Object) this)) {
                possessionComponent.startCuring();
            }
        }
    }
}
