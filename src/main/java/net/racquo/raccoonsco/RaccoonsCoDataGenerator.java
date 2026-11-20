package net.racquo.raccoonsco;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.racquo.raccoonsco.datagen.ModEquipmentAssetProvider;
import net.racquo.raccoonsco.datagen.ModItemTagsProvider;
import net.racquo.raccoonsco.datagen.ModModelProvider;

public class RaccoonsCoDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();

        //add custom data generation providers
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModEquipmentAssetProvider::new);
        pack.addProvider(ModItemTagsProvider::new);

	}
}
