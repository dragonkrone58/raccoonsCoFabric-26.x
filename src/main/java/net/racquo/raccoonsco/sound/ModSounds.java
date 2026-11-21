package net.racquo.raccoonsco.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.racquo.raccoonsco.RaccoonsCo;

public class ModSounds {
    public static final SoundEvent RACCOON_IDLE = registerSoundEvent("raccoon_idle");
    public static final SoundEvent RACCOON_HURT = registerSoundEvent("raccoon_hurt");
    public static final SoundEvent RACCOON_DEATH = registerSoundEvent("raccoon_death");
    public static final SoundEvent RACCOON_EATS = registerSoundEvent("raccoon_eats");
    public static final SoundEvent RACCOON_SLEEPS = registerSoundEvent("raccoon_sleeps");
    public static final SoundEvent RACCOON_SNIFFS = registerSoundEvent("raccoon_sniffs");
    public static final SoundEvent RACCOON_GROWLS = registerSoundEvent("raccoon_growls");
    public static final SoundEvent RACCOON_BITES = registerSoundEvent("raccoon_bites");
    public static final SoundEvent CRAWFISH_HURT = registerSoundEvent("crawfish_hurt");
    public static final SoundEvent CRAWFISH_DEATH = registerSoundEvent("crawfish_death");
    public static final SoundEvent SUSPICIOUS_MUD_STEP = registerSoundEvent("suspicious_mud_wade");
    public static final SoundEvent REED_RUSTLE = registerSoundEvent("reed_rustle");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerSounds() {
        RaccoonsCo.LOGGER.info("Registering Mod Sounds for " + RaccoonsCo.MOD_ID);
    }
}
