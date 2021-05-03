package net.fenrir.mementomori.mixin;

import ladysnake.requiem.api.v1.possession.PossessionComponent;
import net.fenrir.mementomori.Gameplay.Mudsoaked;
import net.fenrir.mementomori.MementoMori;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Item.class, priority = 999)
public abstract class ItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        PossessionComponent possessionComponent = PossessionComponent.get(player);
        MobEntity possessedEntity = possessionComponent.getPossessedEntity();
        if (possessedEntity != null) {
            ItemStack heldStack = player.getStackInHand(hand);
            if (heldStack.getItem().equals(MementoMori.EAU_DE_MORT) || heldStack.getItem().equals(MementoMori.ROASTED_SPIDER_EYE)) {
                player.setCurrentHand(hand);
                cir.setReturnValue(new TypedActionResult<>(ActionResult.SUCCESS, heldStack));
            } else if (heldStack.getItem() == Items.DIRT) {
                Mudsoaked.add(possessedEntity);
                player.getItemCooldownManager().set(Items.DIRT, 20);
                heldStack.decrement(1);
                cir.setReturnValue(new TypedActionResult<>(ActionResult.CONSUME, heldStack));
            }
        }
    }
}
