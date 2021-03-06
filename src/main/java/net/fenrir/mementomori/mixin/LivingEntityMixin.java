package net.fenrir.mementomori.mixin;


import ladysnake.requiem.api.v1.possession.Possessable;
import moriyashiine.onsoulfire.interfaces.OnSoulFireAccessor;
import net.fenrir.mementomori.Gameplay.AttributeHelper;
import net.fenrir.mementomori.Gameplay.Satiation;
import net.fenrir.mementomori.Gameplay.SoulDamage;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 999)
public abstract class LivingEntityMixin extends Entity {


    @Shadow
    public float flyingSpeed;
    @Shadow
    protected int playerHitTimer;
    @Shadow
    protected PlayerEntity attackingPlayer;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    @Nullable
    public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    @Nullable
    public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

    @Inject(method = "damage", at = @At("RETURN"))
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        if (!cir.getReturnValue()) {
            return;
        }
        if ((source.isFire() && ((OnSoulFireAccessor) this).getOnSoulFire())
                || source == DamageSource.WITHER
                || source == DamageSource.DRAGON_BREATH
        ) {
            SoulDamage.IncrementSoul((LivingEntity) (Object) this, 3);
        }
        if (Satiation.isValidMob(attacker)) {
            LivingEntity this2 = (LivingEntity) (Object) this;
            EntityType<?> thisType = this2.getType();
            EntityType<?> type = attacker.getType();
            if (!type.equals(EntityType.PLAYER)) {
                if (thisType.equals(EntityType.VILLAGER) ||
                        thisType.equals(EntityType.PILLAGER) ||
                        thisType.equals(EntityType.WITCH) ||
                        thisType.equals(EntityType.IRON_GOLEM) ||
                        thisType.equals(EntityType.SNOW_GOLEM) ||
                        thisType.equals(EntityType.VINDICATOR) ||
                        thisType.equals(EntityType.EVOKER)
                ) {
                    Satiation.IncrementSatiation((LivingEntity) attacker, (int) Math.floor(amount * 900));
                } else if (thisType.equals(EntityType.PIGLIN) || thisType.equals(EntityType.PIGLIN_BRUTE)) {
                    Satiation.IncrementSatiation((LivingEntity) attacker, (int) Math.floor(amount * 225));
                }
            }
            if (type.equals(EntityType.PHANTOM)) {
                SoulDamage.IncrementSoul(this2, 60);
            } else if (type.equals(EntityType.VEX)) {
                SoulDamage.IncrementSoul(this2, 15);
            } else if (type.equals(EntityType.EVOKER_FANGS)) {
                SoulDamage.IncrementSoul(this2, 5);
            }
        }
    }

    private boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.areTagsEqual(stack1, stack2);
    }

    public int getSlotWithStack(PlayerInventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.main.size(); ++i) {
            if (!(inventory.main.get(i)).isEmpty() && areItemsEqual(stack, inventory.main.get(i))) {
                return i;
            }
        }

        return -1;
    }

    @Inject(method = "tryUseTotem", at = @At("RETURN"), cancellable = true)
    public void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
        LivingEntity user = (LivingEntity) (Object) this;
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
        if (playerEntity == null) {
            Possessable host = (Possessable) user;
            playerEntity = host.getPossessor();
        }
        if (playerEntity instanceof ServerPlayerEntity) {
            PlayerInventory inventory = playerEntity.inventory;
            ItemStack stack = PotionUtil.setPotion(new ItemStack(Items.POTION), MementoMori.WITHER);
            int slot = getSlotWithStack(inventory, stack);
            while (slot >= 0 && inventory.isValid(slot, stack)) {
                inventory.setStack(slot, new ItemStack(MementoMori.EAU_DE_MORT));
                slot = getSlotWithStack(inventory, stack);
            }
        }
    }

    @ModifyVariable(method = "drop", at = @At(value = "HEAD"), argsOnly = true)
    public DamageSource makeSpidersDropEyes(DamageSource deathCause) {
        return deathCause;
    }

    @Inject(
            method = "getAttributeValue",
            at = @At("HEAD"),
            cancellable = true
    )
    public void maxHealthWithoutAttrition(EntityAttribute attribute, CallbackInfoReturnable<Double> cir) {
        if (attribute == EntityAttributes.GENERIC_MAX_HEALTH && hasStatusEffect(MementoMori.ALLEVIATION)) {
            cir.setReturnValue(AttributeHelper.getValueWithoutAttrition(getAttributeInstance(attribute)));
        }
    }


    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    public void noTouchyTouch(Entity entity, CallbackInfo ci) {
        //Overridden
    }

    @Inject(
            method = "tryAttack",
            at = @At("HEAD"),
            cancellable = true
    )
    public void tryAttacking(Entity target, CallbackInfoReturnable<Boolean> cir) {
        //Overridden
    }

}