package net.racquo.raccoonsco.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.racquo.raccoonsco.block.ModBlocks;
import net.racquo.raccoonsco.item.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends FabricTagsProvider.ItemTagsProvider {
        public ModItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider registries) {
            valueLookupBuilder(ItemTags.HEAD_ARMOR).add(ModItems.RACCOON_PELT_HELMET);
            valueLookupBuilder(ItemTags.MAP_INVISIBILITY_EQUIPMENT).add(ModItems.RACCOON_PELT_HELMET);
            valueLookupBuilder(ItemTags.BEE_FOOD).add(ModBlocks.LUPINE.asItem());
            valueLookupBuilder(ItemTags.BEE_FOOD).add(ModBlocks.MARIGOLD.asItem());
            valueLookupBuilder(ItemTags.BEE_FOOD).add(ModBlocks.VIOLET.asItem());
            valueLookupBuilder(ItemTags.BEE_FOOD).add(ModBlocks.MILKWEED.asItem());

    }
}
