package net.fenrir.mementomori.mixin;

import ladysnake.requiem.client.RequiemClient;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fenrir.mementomori.MementoMori;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RequiemClient.class)
public class RequiemClientMixin {
    @Inject(method = "registerModelPredicates",at = @At("HEAD"),cancellable = true,remap = false)
    private void injectHumanityBook(CallbackInfo ci) {
        FabricModelPredicateProviderRegistry.register(MementoMori.REPLACE_BOOK, (stack, world, entity) -> {
            ListTag enchantments = EnchantedBookItem.getEnchantmentTag(stack);
            for (int i = 0; i < enchantments.size(); i++) {
                CompoundTag tag = enchantments.getCompound(i);
                Identifier enchantId = Identifier.tryParse(tag.getString("id"));
                if (enchantId != null && (enchantId.equals(MementoMori.reaping) || enchantId.equals(MementoMori.soulCleaving))) {
                    return tag.getInt("lvl");
                }
            }
            return 0F;
        });
    }
}
