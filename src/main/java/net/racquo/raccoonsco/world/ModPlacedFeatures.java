
package net.racquo.raccoonsco.world;

import java.util.List;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.racquo.raccoonsco.RaccoonsCo;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> MARIGOLD_PLACED_KEY =
            registerKey("marigold_placed");

    public static final ResourceKey<PlacedFeature> VIOLET_PLACED_KEY =
            registerKey("violet_placed");

    public static final ResourceKey<PlacedFeature> LUPINE_PLACED_KEY =
            registerKey("lupine_placed");

    public static final ResourceKey<PlacedFeature> MILKWEED_PLACED_KEY =
            registerKey("milkweed_placed");

    public static final ResourceKey<PlacedFeature> SWAMPY_REEDS_PLACED_KEY =
            registerKey("swampy_reeds_placed");

    public static final ResourceKey<PlacedFeature> SWAMP_GRASS_PLACED_KEY =
            registerKey("swamp_grass_placed");


    public static void bootstrap(BootstrapContext<PlacedFeature> context) {

        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures =
                context.lookup(Registries.CONFIGURED_FEATURE);

        // Marigold
        register(context, MARIGOLD_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.MARIGOLD_KEY),
                RarityFilter.onAverageOnceEvery(12),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                CountPlacement.of(32),
                RandomOffsetPlacement.ofTriangle(7, 3),
                BlockPredicateFilter.forPredicate(
                        BlockPredicate.allOf(
                                BlockPredicate.ONLY_IN_AIR_PREDICATE)),
                BiomeFilter.biome()
        );

        // Violet
        register(context, VIOLET_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.VIOLET_KEY),
                RarityFilter.onAverageOnceEvery(12),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                CountPlacement.of(32),
                RandomOffsetPlacement.ofTriangle(7, 3),
                BlockPredicateFilter.forPredicate(
                        BlockPredicate.allOf(
                                BlockPredicate.ONLY_IN_AIR_PREDICATE
                        )),
                BiomeFilter.biome()
        );

        // Lupine
        register(context, LUPINE_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.LUPINE_KEY),
                RarityFilter.onAverageOnceEvery(8),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                CountPlacement.of(32),
                RandomOffsetPlacement.ofTriangle(7, 3),
                BlockPredicateFilter.forPredicate(
                        BlockPredicate.allOf(
                                BlockPredicate.ONLY_IN_AIR_PREDICATE
                        )),
                BiomeFilter.biome()
        );

        // Milkweed
        register(context, MILKWEED_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.MILKWEED_KEY),
                RarityFilter.onAverageOnceEvery(15),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                CountPlacement.of(32),
                RandomOffsetPlacement.ofTriangle(7, 3),
                BlockPredicateFilter.forPredicate(
                        BlockPredicate.allOf(
                                BlockPredicate.ONLY_IN_AIR_PREDICATE
                        )),
                BiomeFilter.biome()
        );

        // Swampy Reeds
        register(context, SWAMPY_REEDS_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.SWAMPY_REEDS_KEY),
                CountPlacement.of(10),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome(),
                CountPlacement.of(32),
                RandomOffsetPlacement.ofTriangle(6, 3),
                BlockPredicateFilter.forPredicate(
                        BlockPredicate.ONLY_IN_AIR_PREDICATE
                ));

        // Swamp Grass
        register(context, SWAMP_GRASS_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.SWAMP_GRASS_KEY),
                RarityFilter.onAverageOnceEvery(2),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                CountPlacement.of(32),
                RandomOffsetPlacement.ofTriangle(7, 3),
                BlockPredicateFilter.forPredicate(
                        BlockPredicate.allOf(
                                BlockPredicate.ONLY_IN_AIR_PREDICATE
                        )),
                BiomeFilter.biome()
        );
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                RaccoonsCo.id(name)
        );
    }

    private static void register(
            BootstrapContext<PlacedFeature> context,
            ResourceKey<PlacedFeature> key,
            net.minecraft.core.Holder<ConfiguredFeature<?, ?>> configuredFeature,
            PlacementModifier... modifiers
    ) {
        context.register(
                key,
                new PlacedFeature(
                        configuredFeature,
                        List.of(modifiers)
                )
        );
    }
}
