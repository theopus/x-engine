package com.theopus.xengine.system;

import com.theopus.xengine.Render;
import com.theopus.xengine.WindowManager;
import com.theopus.xengine.trait.RenderTrait;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RenderSystem implements System {

    private WindowManager wm;
    private Render render;
    private List<RenderTrait> traits = new ArrayList<>();

    public RenderSystem(WindowManager wm, Render render, RenderTrait ... traitsArr) {
        this.wm = wm;
        this.render = render;
        traits.addAll(Arrays.asList(traitsArr));
    }

    @Override
    public void process() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        traits.forEach(renderTrait -> render.render(renderTrait));
        wm.swapBuffers();
        wm.printGLErrors();
    }
}
