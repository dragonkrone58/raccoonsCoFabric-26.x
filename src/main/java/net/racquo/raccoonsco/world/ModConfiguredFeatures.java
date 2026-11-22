
package net.racquo.raccoonsco.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.racquo.raccoonsco.RaccoonsCo;
import net.racquo.raccoonsco.block.ModBlocks;
import org.jetbrains.annotations.NotNull;

public class ModConfiguredFeatures {

    public static final ResourceKey<@NotNull ConfiguredFeature<?, ?>> MARIGOLD_KEY =
            registerKey("marigold");

    public static final ResourceKey<@NotNull ConfiguredFeature<?, ?>> VIOLET_KEY =
            registerKey("violet");

    public static final ResourceKey<@NotNull ConfiguredFeature<?, ?>> LUPINE_KEY =
            registerKey("lupine");

    public static final ResourceKey<@NotNull ConfiguredFeature<?, ?>> MILKWEED_KEY =
            registerKey("milkweed");

    public static final ResourceKey<@NotNull ConfiguredFeature<?, ?>> SWAMPY_REEDS_KEY =
            registerKey("swampy_reeds");

    public static final ResourceKey<@NotNull ConfiguredFeature<?, ?>> SWAMP_GRASS_KEY =
            registerKey("swamp_grass");

    public static void bootstrap(BootstrapContext<@NotNull ConfiguredFeature<?, ?>> context) {

        register(context, MARIGOLD_KEY,
                ModBlocks.MARIGOLD.defaultBlockState());

        register(context, VIOLET_KEY,
                ModBlocks.VIOLET.defaultBlockState());

        register(context, LUPINE_KEY,
                ModBlocks.LUPINE.defaultBlockState());

        register(context, MILKWEED_KEY,
                ModBlocks.MILKWEED.defaultBlockState());

        register(context, SWAMPY_REEDS_KEY,
                ModBlocks.SWAMPY_REEDS.defaultBlockState());

        register(context, SWAMP_GRASS_KEY,
                ModBlocks.SWAMP_GRASS.defaultBlockState());
    }

    private static void register(
            BootstrapContext<@NotNull ConfiguredFeature<?, ?>> context,
            ResourceKey<@NotNull ConfiguredFeature<?, ?>> key,
            BlockState state
    ) {
        context.register(
                key,
                new ConfiguredFeature<>(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(state)
                        )
                )
        );
    }

    public static ResourceKey<@NotNull ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                RaccoonsCo.id(name)
        );
    }
}
