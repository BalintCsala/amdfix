package me.balintcsala.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.balintcsala.GLUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GlStateManager.class)
public class GlStateManagerMixin {
	@Inject(at = @At("HEAD"), method = "glShaderSource", cancellable = true)
	private static void glShaderSource(int shader, List<String> strings, CallbackInfo ci) {
		RenderSystem.assertThread(RenderSystem::isOnRenderThread);
		GLUtils.glShaderSourceWorkaround(shader, false, (String[]) strings.toArray());
		ci.cancel();
	}
}
