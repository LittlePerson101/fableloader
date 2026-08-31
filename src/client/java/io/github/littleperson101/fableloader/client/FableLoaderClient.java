package io.github.littleperson101.fableloader.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.impl.util.log.Log;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class FableLoaderClient implements ClientModInitializer {
    public static final String MOD_ID = "fableloader";

    public static final ResourceLocation INTRO_SOUND_ID = ResourceLocation.fromNamespaceAndPath(MOD_ID, "intro_sound");
    public static final SoundEvent INTRO_SOUND = SoundEvent.createVariableRangeEvent(INTRO_SOUND_ID);


    @Override
    public void onInitializeClient() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, INTRO_SOUND_ID, INTRO_SOUND);
    }
}