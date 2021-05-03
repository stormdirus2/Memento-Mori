package net.fenrir.mementomori.mixin;

import ladysnake.requiem.api.v1.possession.PossessionComponent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    @Shadow protected abstract Block asBlock();

    @Inject(at = @At("RETURN"), method = "calcBlockBreakingDelta", cancellable = true)
    private void modifyBlockBreakSpeed(BlockState state, PlayerEntity player, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> info) {
        float base = info.getReturnValue();
        if (this.asBlock().getDefaultState().isToolRequired() && PossessionComponent.get(player).isPossessing()) {
            ItemStack stack = player.getMainHandStack();
            if (stack != null) {
                Item item = stack.getItem();
                Block block = Block.getBlockFromItem(item);
                if (block != null && block.getDefaultState().isToolRequired()) {
                    info.setReturnValue(base*12);
                }
            }
        }
    }

}
