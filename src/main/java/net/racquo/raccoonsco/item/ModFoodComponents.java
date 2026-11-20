package net.racquo.raccoonsco.item;


import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class ModFoodComponents {

    //Food components
    public static final FoodProperties RAW_CRAWFISH_FOOD =
            new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f).build();

    public static final FoodProperties COOKED_CRAWFISH_FOOD =
            new FoodProperties.Builder().nutrition(6).saturationModifier(0.6f).build();

    //Consumable components
    public static final Consumable RAW_CRAWFISH_CONSUMABLE =
            Consumables.defaultFood().consumeSeconds(0.8f).onConsume(
                    new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.POISON, 140), 0.8f)).build();

    public static final Consumable COOKED_CRAWFISH_CONSUMABLE =
            Consumables.defaultFood().consumeSeconds(0.8f).build();
}
