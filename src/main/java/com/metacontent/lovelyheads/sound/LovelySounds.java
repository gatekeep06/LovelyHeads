package com.metacontent.lovelyheads.sound;

import com.metacontent.lovelyheads.LovelyHeads;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class LovelySounds {
    public static final SoundEvent HEAD_CONSTRUCTION = registerSoundEvent("head_construction");

    public static final SoundEvent MAGIC_EFFECT = registerSoundEvent("magic_effect");

    public static final SoundEvent MAGIC_EFFECT_2 = registerSoundEvent("magic_effect_2");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier identifier = new Identifier(LovelyHeads.ID, name);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void registerLovelySounds() {
        LovelyHeads.LOGGER.debug("Registering sounds for " + LovelyHeads.ID);
    }
}
