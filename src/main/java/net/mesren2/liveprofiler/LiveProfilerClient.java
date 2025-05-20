package net.mesren2.liveprofiler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.mesren2.liveprofiler.example.TestFeatures;

public class LiveProfilerClient implements ClientModInitializer {

    public static final String MODID = "liveprofiler";

    @Override
    public void onInitializeClient() {
        KeybindHandler.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KeybindHandler.toggleKey.wasPressed()) {
                ProfilerOverlay.toggle();
            }
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            ProfilerOverlay.render(context.getMatrices(), context);
        });

        TestFeatures.register();
    }
}
