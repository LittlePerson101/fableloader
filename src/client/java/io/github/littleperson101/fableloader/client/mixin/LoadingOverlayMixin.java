package io.github.littleperson101.fableloader.client.mixin;

import io.github.littleperson101.fableloader.client.FableLoaderClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingOverlay.class)
public class LoadingOverlayMixin {

    @Shadow @Final private Minecraft minecraft;

    @Unique private long startTime = -1;
    @Unique private boolean audioPlayed = false;
    @Unique private static final int TOTAL_FRAMES = 170;
    @Unique private static final int FPS = 20;

    @Unique private static final long INITIAL_STALL_MS = 1800;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderVideoAndAudio(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }

        long rawElapsed = System.currentTimeMillis() - startTime;

        // Hold on frame 0 during the cold-boot stall phase while the sound engine wakes up
        if (rawElapsed < INITIAL_STALL_MS) {
            ResourceLocation frameTexture = ResourceLocation.fromNamespaceAndPath("fableloader", "textures/gui/loading_frames/frame_0.png");
            int width = guiGraphics.guiWidth();
            int height = guiGraphics.guiHeight();

            guiGraphics.blit(frameTexture, 0, 0, 0.0f, 0.0f, width, height, width, height);
            ci.cancel();
            return;
        }

        // Timer starts rolling only after the sound engine has had time to initialize
        long elapsed = rawElapsed - INITIAL_STALL_MS;

        if (!audioPlayed) {
            try {
                // Stop any background music/ambient noise trying to boot early
                this.minecraft.getSoundManager().stop();

                this.minecraft.getSoundManager().play(
                        SimpleSoundInstance.forUI(FableLoaderClient.INTRO_SOUND, 1.0F, 1.0F)
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            audioPlayed = true;
        }
        double exactFrame = elapsed / (1000.0 / FPS);
        int currentFrame = (int) Math.min(TOTAL_FRAMES - 1, exactFrame);

        ResourceLocation frameTexture = ResourceLocation.fromNamespaceAndPath("fableloader", "textures/gui/loading_frames/frame_" + currentFrame + ".png");

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();

        guiGraphics.blit(
                frameTexture,
                0, 0,
                0.0f, 0.0f,
                width, height,
                width, height
        );

        double videoDurationMs = TOTAL_FRAMES * (1000.0 / FPS);

        if (elapsed >= videoDurationMs && this.minecraft.getOverlay() != null) {
            return;
        }

        ci.cancel();
    }
}