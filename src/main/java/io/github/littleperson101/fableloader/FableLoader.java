package io.github.littleperson101.fableloader;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class FableLoader implements ModInitializer {
    public static final String MOD_ID = "fableloader";

    public static final ResourceLocation INTRO_SOUND_ID = ResourceLocation.fromNamespaceAndPath(MOD_ID, "intro_sound");
    public static final SoundEvent INTRO_SOUND = SoundEvent.createVariableRangeEvent(INTRO_SOUND_ID);

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, INTRO_SOUND_ID, INTRO_SOUND);
    }
}