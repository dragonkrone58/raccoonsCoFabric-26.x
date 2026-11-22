
package net.racquo.raccoonsco.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootSubProvider;


import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import net.racquo.raccoonsco.entity.ModEntities;
import net.racquo.raccoonsco.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModEntityLootProvider extends FabricEntityLootSubProvider {

    public ModEntityLootProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

    @Override
    public void generate() {

        this.add(ModEntities.RACCOON, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ModItems.RACCOON_PELT))));

        this.add(ModEntities.CRAWFISH, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ModItems.RAW_CRAWFISH))));
    }
}
