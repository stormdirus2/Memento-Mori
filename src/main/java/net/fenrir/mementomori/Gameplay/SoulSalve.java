package net.fenrir.mementomori.Gameplay;

import ladysnake.requiem.api.v1.possession.PossessionComponent;
import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SoulSalve extends Item {

    public SoulSalve(Settings settings) {
        super(settings);
    }

    public static int getTotalTime(LivingEntity Entity) {
        int time = 0;
        StatusEffectInstance effect = Entity.getStatusEffect(MementoMori.ALLEVIATION);
        if (effect != null) {
            time = effect.getDuration();
        }
        return time;
    }

    public static void setTotalTime(LivingEntity Entity, int time) {
        Entity.removeStatusEffect(MementoMori.ALLEVIATION);
        Entity.addStatusEffect(new StatusEffectInstance(
                MementoMori.ALLEVIATION,
                time,
                0,
                false,
                false,
                true
        ));
    }

    public static void IncrementAlleviation(LivingEntity Entity, int Increment) {
        setTotalTime(Entity, Math.max(getTotalTime(Entity) + Increment, 0));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user instanceof ServerPlayerEntity) {
            if (!PossessionComponent.get(user).isPossessing()) {
                if (user.hasStatusEffect(RequiemStatusEffects.ATTRITION)) {
                    ItemStack stack = user.getStackInHand(hand);
                    IncrementAlleviation(user, 12000);
                    SoulDamage.IncrementSoul(user, 300);
                    world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_EYE_DEATH, user.getSoundCategory(), 1.0F, 1.0F);
                    user.getItemCooldownManager().set(MementoMori.SOUL_SALVE, 20);
                    stack.decrement(1);
                    return TypedActionResult.consume(stack);
                }
            }
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }
}
