package net.racquo.raccoonsco.util;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.racquo.raccoonsco.RaccoonsCo;

public class ModTags {
public static final TagKey<Item> REPAIRS_RACCOON_PELT =
        TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "repairs_raccoon_pelt"));


}
