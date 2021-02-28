package net.fenrir.mementomori.mixin;

import ladysnake.requiem.common.util.ExtendedShapeContext;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractBlock.AbstractBlockState.class,priority = 999)
public abstract class AbstractBlockStateMixin  {
    @Shadow
    public abstract Block getBlock();

    @Shadow public abstract VoxelShape getCollisionShape(BlockView world, BlockPos pos);

    @Inject(
            at = @At(value = "HEAD"),
            method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;",
            cancellable = true
    )
    private void preventPhasing(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> info) {
        if (((ExtendedShapeContext) context).requiem_isNoClipping() && this.getBlock().getBlastResistance() >= 1200) {
            info.setReturnValue(this.getCollisionShape(world,pos));
        }
    }
}
