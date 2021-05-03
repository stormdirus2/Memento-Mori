package net.fenrir.mementomori.mixin;

import ladysnake.requiem.api.v1.possession.PossessionComponent;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Shadow
    public ServerWorld world;
    @Shadow
    public ServerPlayerEntity player;
    private BlockState lastBlockState;
    private BlockPos lastPos;

    @Inject(method = "tryBreakBlock", at = @At("HEAD"))
    private void captureBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        lastBlockState = world.getBlockState(pos);
        lastPos = pos;
    }

    @ModifyVariable(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;postMine(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V"), ordinal = 1)
    private boolean modifyEffectiveTool(boolean original) {
        if (PossessionComponent.get(player).isPossessing()) {
            if (Items.WOODEN_PICKAXE.canMine(lastBlockState, world, lastPos, player)) {
                return true;
            }
        }
        return original;
    }

}
