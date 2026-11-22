package net.racquo.raccoonsco.block;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.racquo.raccoonsco.RaccoonsCo;
import net.racquo.raccoonsco.block.custom.SwampGrassBlock;
import net.racquo.raccoonsco.block.custom.SwampyReedsBlock;

import java.awt.*;
import java.util.function.Function;

public class ModBlocks {

    //register blocks

    public static final Block SWAMPY_REEDS = registerBlock("swampy_reeds",
            properties -> new SwampyReedsBlock(properties
                    .mapColor(MapColor.COLOR_GREEN)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)));

    public static final Block SWAMP_GRASS = registerBlock("swamp_grass",
            properties -> new SwampGrassBlock(
                    MobEffects.POISON, 4.0f,properties
                    .mapColor(MapColor.COLOR_GREEN)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)));

    public static final Block MARIGOLD = registerBlock("marigold",
            properties -> new FlowerBlock(
                    MobEffects.SPEED, 4.0f, properties
                    .mapColor(MapColor.COLOR_ORANGE)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)));

    public static final Block MARIGOLD_FLOWER_POT = registerBlockWithoutBlockItem("marigold_flower_pot",
            properties -> new FlowerPotBlock(ModBlocks.MARIGOLD, properties
                    .instabreak()
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)));

    public static final Block VIOLET = registerBlock("violet",
            properties -> new FlowerBlock(
                    MobEffects.POISON, 4.0f, properties
                    .mapColor(MapColor.COLOR_PURPLE)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)));

    public static final Block VIOLET_FLOWER_POT = registerBlockWithoutBlockItem("violet_flower_pot",
            properties -> new FlowerPotBlock(ModBlocks.VIOLET, properties
                    .instabreak()
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)));

    public static final Block LUPINE = registerBlock("lupine",
            properties -> new FlowerBlock(
                    MobEffects.NIGHT_VISION, 4.0f, properties
                    .mapColor(MapColor.COLOR_PURPLE )
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)));

    public static final Block LUPINE_FLOWER_POT = registerBlockWithoutBlockItem("lupine_flower_pot",
            properties -> new FlowerPotBlock(ModBlocks.LUPINE, properties
                    .instabreak()
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)));


    public static final Block MILKWEED = registerBlock("milkweed",
            properties -> new FlowerBlock(
                    MobEffects.POISON, 4.0f, properties
                    .mapColor(MapColor.COLOR_PINK)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)));

    public static final Block MILKWEED_FLOWER_POT = registerBlockWithoutBlockItem("milkweed_flower_pot",
            properties -> new FlowerPotBlock(ModBlocks.MILKWEED, properties
                    .instabreak()
                    .pushReaction(PushReaction.DESTROY)));


    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function) {
        Block toRegister = function.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, name))));
        registerBlockItem(name, toRegister);
        return Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, name), toRegister);
    }

    private static Block registerBlockWithoutBlockItem(String name, Function<BlockBehaviour.Properties, Block> function) {
        Block toRegister = function.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, name))));
        return Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, name), toRegister);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, name),
                new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix()
                        .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, name)))));
    }

    public static ResourceKey<Block> getRK(Block block){
        return BuiltInRegistries.BLOCK.getResourceKey(block).get();
    }

    public static void registerModBlocks() {
        RaccoonsCo.LOGGER.info("Registering Mod Blocks for " + RaccoonsCo.MOD_ID);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.accept(ModBlocks.MARIGOLD);
            entries.accept(ModBlocks.VIOLET);
            entries.accept(ModBlocks.LUPINE);
            entries.accept(ModBlocks.MILKWEED);
            entries.accept(ModBlocks.SWAMPY_REEDS);
            entries.accept(ModBlocks.SWAMP_GRASS);
        });
    }

}
