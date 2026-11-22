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
            tag(ItemTags.HEAD_ARMOR).add(ModItems.getRK(ModItems.RACCOON_PELT_HELMET));
            tag(ItemTags.MAP_INVISIBILITY_EQUIPMENT).add(ModItems.getRK(ModItems.RACCOON_PELT_HELMET));
            tag(ItemTags.BEE_FOOD).add(ModItems.getRK(ModBlocks.LUPINE.asItem()));
            tag(ItemTags.BEE_FOOD).add(ModItems.getRK(ModBlocks.MARIGOLD.asItem()));
            tag(ItemTags.BEE_FOOD).add(ModItems.getRK(ModBlocks.VIOLET.asItem()));
            tag(ItemTags.BEE_FOOD).add(ModItems.getRK(ModBlocks.MILKWEED.asItem()));

    }
}
