package net.fenrir.mementomori.Gameplay;

import net.fenrir.mementomori.mixin.EntityAttributeInstanceAccessor;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;

import java.util.Iterator;

public class AttributeHelper {

    public final static String ATTRITION = "069ae0b1-4014-41dd-932f-a5da4417d711";

    public static double getValueWithoutAttrition(EntityAttributeInstance instance) {
        double d = instance.getBaseValue();
        EntityAttributeInstanceAccessor accessor = (EntityAttributeInstanceAccessor) instance;

        EntityAttributeModifier entityAttributeModifier;
        Iterator var;
        for (var = accessor.invokeGetModifiersByOperation(EntityAttributeModifier.Operation.ADDITION).iterator(); var.hasNext(); ) {
            entityAttributeModifier = (EntityAttributeModifier) var.next();
            if (!entityAttributeModifier.getId().toString().equals(ATTRITION)) {
                d += entityAttributeModifier.getValue();
            }
        }

        double e = d;

        for (var = accessor.invokeGetModifiersByOperation(EntityAttributeModifier.Operation.MULTIPLY_BASE).iterator(); var.hasNext(); ) {
            entityAttributeModifier = (EntityAttributeModifier) var.next();
            if (!entityAttributeModifier.getId().toString().equals(ATTRITION)) {
                e += d * entityAttributeModifier.getValue();
            }
        }

        for (var = accessor.invokeGetModifiersByOperation(EntityAttributeModifier.Operation.MULTIPLY_TOTAL).iterator(); var.hasNext(); ) {
            entityAttributeModifier = (EntityAttributeModifier) var.next();
            if (!entityAttributeModifier.getId().toString().equals(ATTRITION)) {
                e *= 1.0D + entityAttributeModifier.getValue();
            }
        }

        return accessor.getType().clamp(e);
    }


}
