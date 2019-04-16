package com.theopus.xengine.wrapper.opengl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.theopus.xengine.wrapper.utils.State;

public class GlState {

    public final State<Boolean> depthTest;
    public final State<Boolean> backFaceCulling;

    public GlState() {
        backFaceCulling = State.bool(false,
                b -> {
                    GL11.glEnable(GL11.GL_CULL_FACE);
                    GL11.glCullFace(GL11.GL_BACK);
                },
                b -> GL11.glDisable(GL11.GL_CULL_FACE));

        depthTest = State.bool(false,
                b -> GL11.glEnable(GL15.GL_DEPTH_TEST),
                b -> GL11.glDisable(GL11.GL_DEPTH_TEST));

    }
}
