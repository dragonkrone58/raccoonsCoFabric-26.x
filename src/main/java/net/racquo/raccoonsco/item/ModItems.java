package net.racquo.raccoonsco.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.material.Fluids;
import net.racquo.raccoonsco.RaccoonsCo;
import net.racquo.raccoonsco.entity.ModEntities;

import java.util.function.Function;

public class ModItems{

    public static Item registerItem(String name, Function<Item.Properties, Item> function ) {
        return Registry.register(
                BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, name),
                function.apply(
                        new Item.Properties().setId(ResourceKey.create(
                                Registries.ITEM, Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, name)))));
    }

    //Item registers
    public static final Item RACCOON_PELT = registerItem( "raccoon_pelt", Item :: new);

    public static final Item RAW_CRAWFISH = registerItem("raw_crawfish", properties ->
            new Item( properties.food(
                    ModFoodComponents.RAW_CRAWFISH_FOOD, ModFoodComponents.RAW_CRAWFISH_CONSUMABLE)));

    public static final Item COOKED_CRAWFISH = registerItem("cooked_crawfish", properties ->
            new Item( properties.food(
                    ModFoodComponents.COOKED_CRAWFISH_FOOD, ModFoodComponents.COOKED_CRAWFISH_CONSUMABLE)));

    public static final Item RACCOON_PELT_HELMET = registerItem("raccoon_pelt_helmet",
            properties -> new Item( properties.humanoidArmor(ModArmorMaterials.RACCOON_PELT_ARMOR_MATERIAL, ArmorType.HELMET)));


    public static final Item RACCOON_SPAWN_EGG = registerItem("raccoon_spawn_egg",
            setting-> new SpawnEggItem(setting.spawnEgg(ModEntities.RACCOON)));

    public static final Item CRAWFISH_SPAWN_EGG = registerItem("crawfish_spawn_egg",
            setting-> new SpawnEggItem(setting.spawnEgg(ModEntities.CRAWFISH)));

    public static final Item CRAWFISH_BUCKET = registerItem("crawfish_bucket",
            setting->new MobBucketItem(ModEntities.CRAWFISH, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, setting));


    //register all items
    public static void registerModItems(){

        RaccoonsCo.LOGGER.info("Registering ModItems for" + RaccoonsCo.MOD_ID );

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
                .register((entries) -> {
                        entries.accept(ModItems.RACCOON_PELT);

                });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register(entries -> {
                        entries.accept(ModItems.RAW_CRAWFISH);
                        entries.accept(ModItems.COOKED_CRAWFISH);
                });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register(entries -> {
                    entries.accept(ModItems.RACCOON_PELT_HELMET);
                });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS)
                .register(entries -> {
                    entries.accept(ModItems.RACCOON_SPAWN_EGG);
                    entries.accept(ModItems.CRAWFISH_SPAWN_EGG);
                });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> {
                    entries.accept(ModItems.CRAWFISH_BUCKET);
                });

    }


}
