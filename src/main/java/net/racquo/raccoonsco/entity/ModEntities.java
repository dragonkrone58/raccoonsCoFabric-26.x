package net.racquo.raccoonsco.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.racquo.raccoonsco.RaccoonsCo;
import net.racquo.raccoonsco.entity.custom.RaccoonEntity;

public class ModEntities {
    public static final ResourceKey<EntityType<?>> RACCOON_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "raccoon"));

    public static final EntityType<RaccoonEntity> RACCOON = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "raccoon"),
            EntityType.Builder.of(RaccoonEntity::new, MobCategory.CREATURE).sized(0.75f, 0.8f).build(RACCOON_KEY));


    private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> key, EntityType.Builder<T> builder
    ) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key)
        );
    }

    public static void registerModEntities() {
        RaccoonsCo.LOGGER.info("Registering Mod Entities for " + RaccoonsCo.MOD_ID);

        FabricDefaultAttributeRegistry.register(RACCOON,RaccoonEntity.createAttributes()
        );

    }
}