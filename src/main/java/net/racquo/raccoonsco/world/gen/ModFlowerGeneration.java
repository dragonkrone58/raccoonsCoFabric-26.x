
package net.racquo.raccoonsco.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.racquo.raccoonsco.world.ModPlacedFeatures;

public class ModFlowerGeneration {

    public static void generateFlowers() {

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.FLOWER_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.MARIGOLD_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        Biomes.TAIGA,
                        Biomes.OLD_GROWTH_SPRUCE_TAIGA,
                        Biomes.OLD_GROWTH_PINE_TAIGA
                ),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.VIOLET_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        Biomes.MEADOW,
                        Biomes.WINDSWEPT_HILLS
                ),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.LUPINE_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        Biomes.SWAMP,
                        Biomes.MANGROVE_SWAMP
                ),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.MILKWEED_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        Biomes.SWAMP,
                        Biomes.MANGROVE_SWAMP
                ),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.SWAMPY_REEDS_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        Biomes.SWAMP,
                        Biomes.MANGROVE_SWAMP
                ),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.SWAMP_GRASS_PLACED_KEY
        );
    }
}
