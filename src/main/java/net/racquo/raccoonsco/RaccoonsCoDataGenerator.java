package net.racquo.raccoonsco;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.racquo.raccoonsco.datagen.*;
import net.racquo.raccoonsco.world.ModConfiguredFeatures;
import net.racquo.raccoonsco.world.ModPlacedFeatures;

public class RaccoonsCoDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();

        //add custom data generation providers
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModEquipmentAssetProvider::new);
        pack.addProvider(ModItemTagsProvider::new);
        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModBlockTagsProvider::new);
        pack.addProvider(ModLootTableProvider::new);
        pack.addProvider(ModWorldGenProvider::new);

	}

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {

        registryBuilder.add(
                Registries.CONFIGURED_FEATURE,
                ModConfiguredFeatures::bootstrap
        );

        registryBuilder.add(
                Registries.PLACED_FEATURE,
                ModPlacedFeatures::bootstrap
        );
    }
}
