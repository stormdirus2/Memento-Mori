package net.fenrir.mementomori.Gameplay;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class SummonPossessable {

    private static final Random random = new Random();

    public static MobEntity spawnReinforcement(MobEntity entity, BlockPos pos) {
        if (entity != null) {
            int i = MathHelper.floor(pos.getX());
            int j = MathHelper.floor(pos.getY());
            int k = MathHelper.floor(pos.getZ());
            ServerWorldAccess serverWorld = (ServerWorldAccess) entity.world;
            if (serverWorld == null) {
                return null;
            }
            for (int l = 0; l < 50; ++l) {
                int m = i + MathHelper.nextInt(random, 7, 25) * MathHelper.nextInt(random, -1, 1);
                int n = j + MathHelper.nextInt(random, 1, 5) * MathHelper.nextInt(random, -1, 1);
                int o = k + MathHelper.nextInt(random, 7, 25) * MathHelper.nextInt(random, -1, 1);
                if (!entity.world.isAir(new BlockPos(m, n - 1, o))) {
                    entity.updatePosition(m, n, o);
                    if (entity.world.intersectsEntities(entity) && entity.world.isSpaceEmpty(entity)) {
                        entity.initialize(serverWorld, entity.world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.REINFORCEMENT, null, null);
                        serverWorld.spawnEntityAndPassengers(entity);
                        return entity;
                    }
                }
            }
        }
        return null;
    }

    public static BlockPos getGround(BlockPos pos, World world) {
        while (!world.getBlockState(pos).getMaterial().isSolid()) {
            if (pos.getY() < 5) {
                return null;
            }
            pos = pos.down();
        }
        return pos;
    }

    public static MobEntity spawnPossessable(BlockPos pos, World world) {
        BlockPos ground = getGround(pos, world);
        if (ground != null) {
            BlockPos placement = ground.up(2);
            if (world.getBiome(placement).getCategory() == Biome.Category.NETHER) {
                return spawnReinforcement(EntityType.ZOMBIFIED_PIGLIN.create(world), placement);
            } else if (world.getBiome(placement).getCategory() == Biome.Category.DESERT) {
                return spawnReinforcement(EntityType.HUSK.create(world), placement);
            } else if (world.getBlockState(placement).getBlock() == Blocks.WATER || world.getBlockState(placement.down()).getBlock() == Blocks.WATER) {
                return spawnReinforcement(EntityType.DROWNED.create(world), placement);
            } else if (world.isNight() || !world.isSkyVisible(placement)) {
                return spawnReinforcement(EntityType.SKELETON.create(world), placement);
            }
        }
        return null;
    }

}
