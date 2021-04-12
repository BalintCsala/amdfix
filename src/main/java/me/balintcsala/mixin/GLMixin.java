package me.balintcsala.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;
import java.util.List;

@Mixin(GlStateManager.class)
public class GLMixin {
	@Inject(at = @At("HEAD"), method = "glShaderSource", cancellable = true)
	private static void glShaderSource(int shader, List<String> strings, CallbackInfo ci) {
		RenderSystem.assertThread(RenderSystem::isOnRenderThread);
		final MemoryStack stack = MemoryStack.stackGet();
		final int stackPointer = stack.getPointer();

		try {
			StringBuilder builder = new StringBuilder();
			for (String string : strings) {
				builder.append(string);
			}
			final ByteBuffer sourceBuffer = MemoryUtil.memUTF8(builder.toString(), true);
			final PointerBuffer pointers = stack.mallocPointer(1);
			pointers.put(sourceBuffer);
			GL20C.nglShaderSource(shader, 1, pointers.address0(), 0);
			org.lwjgl.system.APIUtil.apiArrayFree(pointers.address0(), 1);
		} finally {
			stack.setPointer(stackPointer);
		}
		ci.cancel();
	}
}
