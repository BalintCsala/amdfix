package me.balintcsala.mixin;

import me.balintcsala.GLUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ARBShaderObjects.class)
public class ARBShaderObjectsMixin {

    @Inject(at = @At("HEAD"), method = "glShaderSourceARB(I[Ljava/lang/CharSequence;)V", cancellable = true, remap = false)
    private static void glShaderSourceARB(int shaderObj, CharSequence[] string, CallbackInfo ci) {
        GLUtils.glShaderSourceWorkaround(shaderObj, true, string);
        ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "glShaderSourceARB(ILjava/lang/CharSequence;)V", cancellable = true, remap = false)
    private static void glShaderSourceARB(int shaderObj, CharSequence string, CallbackInfo ci) {
        GLUtils.glShaderSourceWorkaround(shaderObj, true, string);
        ci.cancel();
    }

}
