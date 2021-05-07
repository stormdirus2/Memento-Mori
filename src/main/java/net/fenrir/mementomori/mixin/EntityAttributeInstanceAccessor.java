package net.fenrir.mementomori.mixin;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;

@Mixin(EntityAttributeInstance.class)
public interface EntityAttributeInstanceAccessor {

    @Accessor
    EntityAttribute getType();

    @Invoker
    Collection<EntityAttributeModifier> invokeGetModifiersByOperation(EntityAttributeModifier.Operation operation);

}
