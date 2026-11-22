package net.racquo.raccoonsco.item;

import net.minecraft.core.Registry;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.*;
import net.racquo.raccoonsco.RaccoonsCo;
import net.racquo.raccoonsco.util.ModTags;
import org.jetbrains.annotations.NotNull;

public class ModArmorMaterials {


    public static final ResourceKey<? extends @NotNull Registry<@NotNull EquipmentAsset>> REGISTRY_KEY =
            ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));

    public static final ResourceKey<@NotNull EquipmentAsset> RACCOON_PELT_KEY =
            ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "raccoon_pelt"));


    public static final ArmorMaterial RACCOON_PELT_ARMOR_MATERIAL = new ArmorMaterial(8, ArmorMaterials.makeDefense(2, 4, 6, 1, 4), 10,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f, ModTags.REPAIRS_RACCOON_PELT, RACCOON_PELT_KEY);

}