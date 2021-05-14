package net.fenrir.mementomori.mixin;

import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DamageSource.class)
public interface DamageSourceAccessor {

    @Accessor
    void setBypassesArmor(boolean value);

    @Accessor
    void setUnblockable(boolean value);

    @Accessor
    void setName(String value);

}
