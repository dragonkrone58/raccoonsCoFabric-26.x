package net.racquo.raccoonsco.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.racquo.raccoonsco.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootSubProvider {
    public ModLootTableProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {
        //generate block drops/loot
        dropSelf(ModBlocks.MARIGOLD);
        add(ModBlocks.MARIGOLD_FLOWER_POT, block -> createPotFlowerItemTable(ModBlocks.MARIGOLD));
        dropSelf(ModBlocks.VIOLET);
        add(ModBlocks.VIOLET_FLOWER_POT, block -> createPotFlowerItemTable(ModBlocks.VIOLET));

        dropSelf(ModBlocks.LUPINE);
        add(ModBlocks.LUPINE_FLOWER_POT, block -> createPotFlowerItemTable(ModBlocks.LUPINE));

        add(ModBlocks.SWAMPY_REEDS, createDoublePlantWithSeedDrops(ModBlocks.SWAMPY_REEDS, ModBlocks.SWAMP_GRASS));
        add(ModBlocks.SWAMP_GRASS, createShearsDispatchTable(ModBlocks.SWAMP_GRASS, LootItem.lootTableItem(Items.WHEAT_SEEDS)
                .when(LootItemRandomChanceCondition.randomChance(0.3F))));
    }
}
