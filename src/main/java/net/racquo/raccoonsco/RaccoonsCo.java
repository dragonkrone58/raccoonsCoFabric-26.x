package net.racquo.raccoonsco;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import net.racquo.raccoonsco.block.ModBlocks;
import net.racquo.raccoonsco.item.ModItems;
import net.racquo.raccoonsco.item.custom.RaccoonPeltHelmet;
import net.racquo.raccoonsco.sound.ModSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaccoonsCo implements ModInitializer {
	public static final String MOD_ID = "raccoonsco";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

        ModItems.registerModItems();
        RaccoonPeltHelmet.register();
        ModBlocks.registerModBlocks();
        ModSounds.registerSounds();

		LOGGER.info("Hello Fabric world!");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
