package com.theopus.xengine.wrapper.opengl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.theopus.xengine.wrapper.utils.State;

public class GlState {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlState.class);

    public final State<Boolean> depthTest;
    public final State<Boolean> backFaceCulling;
    public final State<Boolean> depthMask;
    public final State<Integer> depthFunciton;
    public final State<Boolean> blending;
    public final State<Boolean> alphaBlend;

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

        depthMask = State.bool(false,
                b -> GL11.glDepthMask(true),
                b -> GL11.glDepthMask(false));

        depthFunciton = new State<>(GL11.GL_LESS,
                GL11::glDepthFunc, true);

        blending = State.bool(false,
                b-> GL11.glEnable(GL11.GL_BLEND),
                b-> GL11.glDisable(GL11.GL_BLEND));

        alphaBlend = State.bool(false,
                b ->  GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA),
                b -> LOGGER.warn("NOT IMPLEMENTED"));
    }
}
