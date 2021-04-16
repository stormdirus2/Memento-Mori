package net.fenrir.mementomori.Gameplay;

import ladysnake.requiem.api.v1.possession.PossessionComponent;
import ladysnake.requiem.common.network.RequiemNetworking;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class EauDeMort extends Item {

    public EauDeMort(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public SoundEvent getEatSound() {
        return getDrinkSound();
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) playerEntity, stack);
            PossessionComponent possessionComponent = PossessionComponent.get(playerEntity);
            MobEntity possessedEntity = possessionComponent.getPossessedEntity();
            if (possessedEntity != null) {
                Unposessable.of(playerEntity).ifPresent(Unposessable -> Unposessable.setLast(possessedEntity));
                possessionComponent.stopPossessing(false);
                RequiemNetworking.sendEtherealAnimationMessage((ServerPlayerEntity) playerEntity);
            } else {
                playerEntity.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.WITHER,
                        600,
                        1
                ));
                playerEntity.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.NAUSEA,
                        600,
                        4
                ));
                playerEntity.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.WEAKNESS,
                        600,
                        4
                ));
            }

            if (!playerEntity.isCreative()) {
                stack.decrement(1);
                return new ItemStack(Items.GLASS_BOTTLE);
            }
        }
        return stack;
    }
}