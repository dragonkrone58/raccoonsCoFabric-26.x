package net.racquo.raccoonsco.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.racquo.raccoonsco.block.ModBlocks;
import net.racquo.raccoonsco.item.ModItems;

public class ModModelProvider extends FabricModelProvider{

    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

        blockModelGenerators.createPlantWithDefaultItem(ModBlocks.MARIGOLD, ModBlocks.MARIGOLD_FLOWER_POT, BlockModelGenerators.PlantType.NOT_TINTED );
        blockModelGenerators.createPlantWithDefaultItem(ModBlocks.VIOLET, ModBlocks.VIOLET_FLOWER_POT, BlockModelGenerators.PlantType.NOT_TINTED );
        blockModelGenerators.createPlantWithDefaultItem(ModBlocks.LUPINE, ModBlocks.LUPINE_FLOWER_POT, BlockModelGenerators.PlantType.NOT_TINTED );
        blockModelGenerators.createPlantWithDefaultItem(ModBlocks.MILKWEED, ModBlocks.MILKWEED_FLOWER_POT, BlockModelGenerators.PlantType.NOT_TINTED );
        blockModelGenerators.createCrossBlockWithDefaultItem(ModBlocks.SWAMP_GRASS, BlockModelGenerators.PlantType.NOT_TINTED );
        //N.B. swampy reeds is manual jsonw
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ModItems.RACCOON_PELT, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.RAW_CRAWFISH, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.COOKED_CRAWFISH, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.RACCOON_PELT_HELMET, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModBlocks.SWAMPY_REEDS.asItem(), ModelTemplates.FLAT_ITEM);


    }
}
