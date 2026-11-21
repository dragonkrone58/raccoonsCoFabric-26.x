package net.racquo.raccoonsco.item.custom;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.Identifier;
import net.racquo.raccoonsco.RaccoonsCo;
import net.racquo.raccoonsco.item.ModItems;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;



public class RaccoonPeltHelmet {

    public static final Identifier RACCOON_SNEAK_SPEED_ID =
            Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "raccoon_sneak_speed");

    public static final AttributeModifier RACCOON_SNEAK_SPEED_MODIFIER =
            new AttributeModifier(
                    RACCOON_SNEAK_SPEED_ID,
                    0.5, // +50% total → Swift Sneak I feel
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            );

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(RaccoonPeltHelmet::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        for (Player player : server.getPlayerList().getPlayers()) {
            applySneakSpeed(player);
        }
    }

    private static void applySneakSpeed(Player player) {

        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);

        boolean wearingHelmet = helmet.is(ModItems.RACCOON_PELT_HELMET);
        boolean sneaking = player.isShiftKeyDown();

        AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute == null) return;

        boolean hasModifier = attribute.getModifier(RACCOON_SNEAK_SPEED_ID) != null;

        if (wearingHelmet && sneaking) {
            if (!hasModifier) {
                attribute.addTransientModifier(RACCOON_SNEAK_SPEED_MODIFIER);

            }
        } else {
            if (hasModifier) {
                attribute.removeModifier(RACCOON_SNEAK_SPEED_ID);
            }
        }
    }
}
