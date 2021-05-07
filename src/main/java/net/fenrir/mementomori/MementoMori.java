package net.fenrir.mementomori;

import ladysnake.requiem.api.v1.possession.PossessionComponent;
import ladysnake.requiem.api.v1.remnant.RemnantComponent;
import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fenrir.mementomori.Gameplay.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.item.*;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.Potion;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class MementoMori implements ModInitializer {
    public static final String MOD_ID = "mementomori";
    public static final String MOD_NAME = "Memento Mori";
    public static final Identifier REPLACE_BOOK = new Identifier("mementomori", "replace_book");
    public static final FoodComponent DRINK = new FoodComponent.Builder()
            .alwaysEdible()
            .build();
    public static final FoodComponent TAINTED_MEAT = new FoodComponent.Builder().alwaysEdible().meat().hunger(1)
            .statusEffect(new StatusEffectInstance(
                    StatusEffects.WEAKNESS,
                    140,
                    0
            ), 1).build();
    public static final StatusEffect SATIATION = new Satiation();
    public static final StatusEffect MUDSOAKED = new Mudsoaked();
    public static final StatusEffect ALLEVIATION = new Alleviation();
    public static final Potion WITHER = new Potion("Withering", new StatusEffectInstance(StatusEffects.WITHER, 600));
    public static final Item EAU_DE_MORT = new EauDeMort(new Item.Settings().food(DRINK).maxCount(1).fireproof().rarity(Rarity.RARE).recipeRemainder(Items.GLASS_BOTTLE).group(ItemGroup.MISC));
    public static final Item ROASTED_SPIDER_EYE = new Consumable(new Item.Settings().food(TAINTED_MEAT).group(ItemGroup.FOOD));
    public static final Item SOUL_SALVE = new SoulSalve(new Item.Settings().rarity(Rarity.UNCOMMON).group(ItemGroup.TOOLS));
    public static final Tag<Item> CURE_ALLS = TagRegistry.item(new Identifier(MOD_ID, "cure_alls"));
    public static final GameRules.Key<GameRules.BooleanRule> cureAlls =
            register("cureAllItems", GameRuleFactory.createBooleanRule(true), GameRules.Category.PLAYER);
    public static final GameRules.Key<GameRules.IntRule> soulDamageDeathLevel =
            register("soulDamageDeathLevel", GameRuleFactory.createIntRule(4, -1, 4), GameRules.Category.PLAYER);
    public static final GameRules.Key<GameRules.IntRule> phantomSpawnCount =
            register("phantomSpawnCount", GameRuleFactory.createIntRule(1, 0), GameRules.Category.PLAYER);
    public static final GameRules.Key<GameRules.BooleanRule> attritionGrowth =
            register("attritionGrowth", GameRuleFactory.createBooleanRule(true, (server, rule) -> {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBoolean(rule.get());
                server.getPlayerManager().sendToAll(ServerSidePacketRegistry.INSTANCE.toPacket(new Identifier("mementomori:attrition_growth"), buf));
            }), GameRules.Category.PLAYER);
    public static final GameRules.Key<GameRules.BooleanRule> blastUnphasable =
            register("blastResistantUnphasable", GameRuleFactory.createBooleanRule(true, (server, rule) -> {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBoolean(rule.get());
                server.getPlayerManager().sendToAll(ServerSidePacketRegistry.INSTANCE.toPacket(new Identifier("mementomori:blast_unphasable"), buf));
            }), GameRules.Category.PLAYER);
    public static final GameRules.Key<GameRules.IntRule> attritionTime =
            register("attritionTime", GameRuleFactory.createIntRule(600, 0), GameRules.Category.PLAYER);
    public static final Identifier soulCleaving = new Identifier("mementomori", "soul_cleaving");
    public static final Identifier reaping = new Identifier("mementomori", "reaping");
    private static final Pattern NETHER_CHEST = Pattern.compile("chests/.*nether.*");
    private static final Identifier BASTION_TREASURE = new Identifier("minecraft", "chests/bastion_treasure");
    private static final Identifier RUINED_PORTAL = new Identifier("minecraft", "chests/ruined_portal");
    private static final Identifier BASTION_OTHER = new Identifier("minecraft", "chests/bastion_other");
    private static final Identifier LIBRARY = new Identifier("minecraft", "chests/stronghold_library");
    private static final Identifier MANSION = new Identifier("minecraft", "chests/woodland_mansion");
    private static final Identifier END_CITY = new Identifier("minecraft", "chests/end_city_treasure");
    private static final Random RANDOM = new Random();
    private static final Enchantment SOUL_CLEAVING = Registry.register(
            Registry.ENCHANTMENT,
            new Identifier("mementomori", "soul_cleaving"),
            new SoulCleaving()
    );
    public static Enchantment REAPING = Registry.register(
            Registry.ENCHANTMENT,
            reaping,
            new Reaping()
    );

    public static boolean getBlastUnphasable(World world) {
        if (world.isClient()) {
            return MementoMori_CLIENT.blastUnphasable;
        } else {
            return world.getGameRules().getBoolean(blastUnphasable);
        }
    }

    public static boolean getAttritionGrowth(World world) {
        if (world.isClient()) {
            return MementoMori_CLIENT.attritionGrowth;
        } else {
            return world.getGameRules().getBoolean(attritionGrowth);
        }
    }

    private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Type<T> type, GameRules.Category category) {
        return GameRuleRegistry.register(MOD_ID + ":" + name, category, type);
    }

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("mementomori", "eau_de_mort"), EAU_DE_MORT);
        Registry.register(Registry.ITEM, new Identifier("mementomori", "roasted_spider_eye"), ROASTED_SPIDER_EYE);
        Registry.register(Registry.ITEM, new Identifier("mementomori", "soul_salve"), SOUL_SALVE);
        Registry.register(Registry.POTION, "withering", WITHER);
        Registry.register(Registry.STATUS_EFFECT, new Identifier("mementomori", "satiation"), SATIATION);
        Registry.register(Registry.STATUS_EFFECT, new Identifier("mementomori", "alleviation"), ALLEVIATION);
        Registry.register(Registry.STATUS_EFFECT, new Identifier("mementomori", "mudsoaked"), MUDSOAKED);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PacketByteBuf buf1 = PacketByteBufs.create();
            buf1.writeBoolean(handler.player.world.getGameRules().getBoolean(attritionGrowth));
            PacketByteBuf buf2 = PacketByteBufs.create();
            buf2.writeBoolean(handler.player.world.getGameRules().getBoolean(blastUnphasable));
            ServerPlayNetworking.send(handler.player, new Identifier("mementomori:attrition_growth"), buf1);
            ServerPlayNetworking.send(handler.player, new Identifier("mementomori:blast_unphasable"), buf2);
        });
        AtomicInteger phantomTick = new AtomicInteger();
        AtomicInteger hostTick = new AtomicInteger();
        ServerTickCallback.EVENT.register((server) -> {
            int phantomSpawnCount = server.getGameRules().getInt(MementoMori.phantomSpawnCount);
            if (server.getOverworld().isNight() && phantomSpawnCount > 0) {
                if (phantomTick.incrementAndGet() >= 200) {
                    server.getPlayerManager().getPlayerList().forEach((player) -> {
                        StatusEffectInstance effect = player.getStatusEffect(RequiemStatusEffects.ATTRITION);
                        LivingEntity Host = PossessionComponent.getPossessedEntity(player);
                        boolean isSpirit = RemnantComponent.isIncorporeal(player);
                        if (effect != null && player.world.getLightLevel(player.getBlockPos()) < 5) {
                            if (player.world == server.getOverworld() && (!isSpirit || Host != null) && !player.isCreative() && player.getBlockPos().getY() >= player.world.getSeaLevel() && player.world.isSkyVisible(player.getBlockPos()) && player.world.getBlockState(player.getBlockPos().up(25)).isAir()) {
                                for (int i = 0; i < (effect.getAmplifier() + 1) * phantomSpawnCount; i++) {
                                    PhantomEntity phantom = EntityType.PHANTOM.create(server.getOverworld(), null, null, null, new BlockPos(player.getX(), player.getY() + 25, player.getZ()), SpawnReason.NATURAL, true, false);
                                    server.getOverworld().spawnEntity(phantom);
                                }
                            }
                        }
                    });
                    phantomTick.set(0);
                }
            }
            if (hostTick.incrementAndGet() >= 400) {
                server.getPlayerManager().getPlayerList().forEach((player) -> {
                    if (RemnantComponent.get(player).isIncorporeal() && !PossessionComponent.get(player).isPossessing()) {
                        MobEntity lastOffered = ((Unposessable) player).getLastOffered();
                        if (lastOffered == null || lastOffered.distanceTo(player) > 25) {
                            ((Unposessable) player).setLastOffered(SummonPossessable.spawnPossessable(player.getBlockPos(), player.world));
                        }
                    }
                });
                hostTick.set(0);
            }

        });

        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
            float chance = 0;
            if (id == BASTION_TREASURE || id == END_CITY) {
                chance = 0.7F;
            } else if (id == BASTION_OTHER) {
                chance = 0.4F;
            } else if (NETHER_CHEST.matcher(id.getPath()).matches()) {
                chance = 0.2F;
            } else if (id == MANSION || id == LIBRARY || id == RUINED_PORTAL) {
                chance = 0.1F;
            }
            if (chance != 0) {
                Logger.getLogger(MOD_ID).log(Level.INFO, id.toString());
                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(ConstantLootTableRange.create(1))
                        .withEntry(ItemEntry.builder(EAU_DE_MORT).build())
                        .withCondition(RandomChanceLootCondition.builder(chance).build());

                supplier.withPool(poolBuilder.build());
            }
        });

        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
            if (NETHER_CHEST.matcher(id.getPath()).matches()) {
                Logger.getLogger(MOD_ID).log(Level.INFO, id.toString());
                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(ConstantLootTableRange.create(1))
                        .withEntry(ItemEntry.builder(Items.BOOK).apply(() -> new LootFunction() {
                            @Override
                            public LootFunctionType getType() {
                                throw new UnsupportedOperationException();
                            }

                            @Override
                            public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
                                EnchantmentLevelEntry enchantment = new EnchantmentLevelEntry(SOUL_CLEAVING, RANDOM.nextInt(2) + 1);
                                return EnchantedBookItem.forEnchantment(enchantment);
                            }
                        }).build())
                        .withCondition(RandomChanceLootCondition.builder(0.4F).build());
                supplier.withPool(poolBuilder.build());
            }
        });

        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
            if (NETHER_CHEST.matcher(id.getPath()).matches()) {
                Logger.getLogger(MOD_ID).log(Level.INFO, id.toString());
                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(ConstantLootTableRange.create(1)) // Same as "rolls": 1 in the loot table json
                        .withEntry(ItemEntry.builder(Items.BOOK).apply(() -> new LootFunction() {
                            @Override
                            public LootFunctionType getType() {
                                throw new UnsupportedOperationException();
                            }

                            @Override
                            public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
                                EnchantmentLevelEntry enchantment = new EnchantmentLevelEntry(REAPING, 1);
                                return EnchantedBookItem.forEnchantment(enchantment);
                            }
                        }).build())
                        .withCondition(RandomChanceLootCondition.builder(0.2F).build());
                supplier.withPool(poolBuilder.build());
            }
        });

        Logger.getLogger(MOD_ID).log(Level.INFO, "[" + MOD_NAME + "] " + "Initialized");
    }
}


