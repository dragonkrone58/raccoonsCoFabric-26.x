package net.racquo.raccoonsco.world.gen;


import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.racquo.raccoonsco.entity.ModEntities;
import net.racquo.raccoonsco.entity.custom.CrawfishEntity;

public class ModEntitySpawns {

    public static void addSpawns(){
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.FOREST, Biomes.SWAMP, Biomes.TAIGA)
                , MobCategory.CREATURE, ModEntities.RACCOON, 30, 1, 3);

        SpawnPlacements.register(ModEntities.RACCOON, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES
                , Animal::checkAnimalSpawnRules);

        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.SWAMP, Biomes.RIVER)
                , MobCategory.WATER_AMBIENT, ModEntities.CRAWFISH, 30, 2, 5);

        SpawnPlacements.register(ModEntities.CRAWFISH, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES
                , CrawfishEntity::canCrawfishSpawn);

    }
}
