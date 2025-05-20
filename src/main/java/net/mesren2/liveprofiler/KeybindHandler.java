package net.mesren2.liveprofiler;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class KeybindHandler {
    public static KeyBinding toggleKey;

    public static void register() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.liveprofiler.toggle",
                GLFW.GLFW_KEY_P,
                "category.liveprofiler"
        ));
    }
}
