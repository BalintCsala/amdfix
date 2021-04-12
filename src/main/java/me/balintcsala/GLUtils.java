package me.balintcsala;

import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class GLUtils {

    public static void glShaderSourceWorkaround(int shader, boolean arb, CharSequence... strings) {
        final MemoryStack stack = MemoryStack.stackGet();
        final int stackPointer = stack.getPointer();

        try {
            StringBuilder builder = new StringBuilder();
            for (CharSequence string : strings) {
                builder.append(string);
            }
            final ByteBuffer sourceBuffer = MemoryUtil.memUTF8(builder.toString(), true);
            final PointerBuffer pointers = stack.mallocPointer(1);
            pointers.put(sourceBuffer);
            if (arb) {
                ARBShaderObjects.nglShaderSourceARB(shader, 1, pointers.address0(), 0);
            } else {
                GL20C.nglShaderSource(shader, 1, pointers.address0(), 0);
            }
            org.lwjgl.system.APIUtil.apiArrayFree(pointers.address0(), 1);
        } finally {
            stack.setPointer(stackPointer);
        }
    }

}
