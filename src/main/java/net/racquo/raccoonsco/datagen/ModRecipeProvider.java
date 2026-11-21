package net.racquo.raccoonsco.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.racquo.raccoonsco.block.ModBlocks;
import net.racquo.raccoonsco.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        return new RecipeProvider(provider, output){

            List<ItemLike> RAW_CRAWFISH_COOKABLE = List.of(ModItems.RAW_CRAWFISH);

            @Override
            public void buildRecipes(){
                oreSmelting(RAW_CRAWFISH_COOKABLE, RecipeCategory.FOOD, CookingBookCategory.FOOD,
                        ModItems.COOKED_CRAWFISH, 0.25f, 200, "cooked_crawfish" );
                SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(ModItems.RAW_CRAWFISH), RecipeCategory.FOOD, ModItems.COOKED_CRAWFISH, 0.35F, 100);
                SimpleCookingRecipeBuilder.smoking(Ingredient.of(ModItems.RAW_CRAWFISH), RecipeCategory.FOOD, ModItems.COOKED_CRAWFISH, 0.35F, 100);

                shaped(RecipeCategory.COMBAT, ModItems.RACCOON_PELT_HELMET)
                        .pattern("R")
                        .pattern("L")
                        .define('R', ModItems.RACCOON_PELT)
                        .define('L', Items.LEATHER_HELMET)
                        .unlockedBy(getHasName(ModItems.RACCOON_PELT), has(ModItems.RACCOON_PELT_HELMET))
                        .unlockedBy(getHasName(Items.LEATHER_HELMET), has(ModItems.RACCOON_PELT_HELMET))
                        .group("raccoon_pelt")
                        .save(output);

                //marigold to orange dye recipe
                shapeless(RecipeCategory.MISC, Items.ORANGE_DYE, 1)
                        .requires(ModBlocks.MARIGOLD)
                        .unlockedBy(getHasName(ModBlocks.MARIGOLD), has(Items.ORANGE_DYE))
                        .save(output);

                //violet to purple dye recipe
                shapeless(RecipeCategory.MISC, Items.PURPLE_DYE, 1)
                        .requires(ModBlocks.VIOLET)
                        .unlockedBy(getHasName(ModBlocks.VIOLET), has(Items.PURPLE_DYE))
                        .save(output);

                //lupine to magenta dye recipe
                shapeless(RecipeCategory.MISC, Items.MAGENTA_DYE, 1)
                        .requires(ModBlocks.LUPINE)
                        .unlockedBy(getHasName(ModBlocks.LUPINE), has(Items.MAGENTA_DYE))
                        .save(output);

                shapeless(RecipeCategory.MISC, Items.PINK_DYE, 1)
                        .requires(ModBlocks.MILKWEED)
                        .unlockedBy(getHasName(ModBlocks.MILKWEED), has(Items.PINK_DYE))
                        .save(output);            }
        };
    }

    @Override
    public String getName() {
        return "RaccoonsCoModRecipeProvider";
    }
}
