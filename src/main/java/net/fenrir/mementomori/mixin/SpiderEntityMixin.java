package net.fenrir.mementomori.mixin;

import ladysnake.requiem.api.v1.possession.Possessable;
import ladysnake.requiem.common.util.DamageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpiderEntity.class)
public abstract class SpiderEntityMixin extends LivingEntityMixin {
    public SpiderEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public DamageSource makeSpidersDropEyes(DamageSource deathCause) {
        Entity attacker = deathCause.getAttacker();
        if (attacker != null) {
            if (attacker instanceof Possessable) {
                Possessable possessed = (Possessable) attacker;
                PlayerEntity possessor = possessed.getPossessor();
                if (possessor != null) {
                    this.playerHitTimer = 100;
                    this.attackingPlayer = possessor;
                    DamageSource proxiedDamage = DamageHelper.createProxiedDamage(deathCause, possessor);
                    if (proxiedDamage != null) {
                        return proxiedDamage;
                    }
                }
            }
        }

        return deathCause;
    }

}
