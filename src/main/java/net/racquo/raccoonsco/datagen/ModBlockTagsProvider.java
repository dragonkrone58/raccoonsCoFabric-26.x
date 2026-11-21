package net.racquo.raccoonsco.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.racquo.raccoonsco.block.ModBlocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public ModBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }
    @Override
    protected void addTags(HolderLookup.Provider registries) {

        List<Block> MOD_FLOWERS = List.of(ModBlocks.MARIGOLD, ModBlocks.LUPINE, ModBlocks.VIOLET, ModBlocks.MILKWEED);

        List<Block> MOD_FLOWERPOTS = List.of(ModBlocks.LUPINE_FLOWER_POT,ModBlocks.MARIGOLD_FLOWER_POT,
                ModBlocks.VIOLET_FLOWER_POT, ModBlocks.MILKWEED_FLOWER_POT);

        for(Block block : MOD_FLOWERS){
            valueLookupBuilder(BlockTags.SMALL_FLOWERS).add(block);
            valueLookupBuilder(BlockTags.BEE_ATTRACTIVE).add(block);
        }

        for(Block block : MOD_FLOWERPOTS){
            valueLookupBuilder(BlockTags.FLOWER_POTS).add(block);
            valueLookupBuilder(BlockTags.BEE_ATTRACTIVE).add(block);
        }
        valueLookupBuilder(BlockTags.SMALL_FLOWERS).add(ModBlocks.MARIGOLD);
        valueLookupBuilder(BlockTags.FLOWER_POTS).add(ModBlocks.MARIGOLD_FLOWER_POT);
        valueLookupBuilder(BlockTags.SMALL_FLOWERS).add(ModBlocks.VIOLET);
        valueLookupBuilder(BlockTags.FLOWER_POTS).add(ModBlocks.VIOLET_FLOWER_POT);
        valueLookupBuilder(BlockTags.SMALL_FLOWERS).add(ModBlocks.LUPINE);
        valueLookupBuilder(BlockTags.FLOWER_POTS).add(ModBlocks.LUPINE_FLOWER_POT);
        valueLookupBuilder(BlockTags.SMALL_FLOWERS).add(ModBlocks.MILKWEED);
        valueLookupBuilder(BlockTags.FLOWER_POTS).add(ModBlocks.MILKWEED_FLOWER_POT);

    }
}