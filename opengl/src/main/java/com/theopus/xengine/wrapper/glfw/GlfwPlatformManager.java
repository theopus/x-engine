package com.theopus.xengine.wrapper.glfw;

import org.joml.Vector4f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GlfwPlatformManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlfwPlatformManager.class);
    public int width;
    public int height;
    public long mainContext;
    private Vector4f color;
    private boolean primitiveCompatible;
    private GLFWKeyCallback listener;
    private GLCapabilities mainCapabilities;
    private int vSync;
    private long sideContext;
    private GLCapabilities sideCapabilities;


    public GlfwPlatformManager(WindowConfig windowConfig, GLFWKeyCallback listener) {
        this.width = windowConfig.getWidth();
        this.height = windowConfig.getHeight();
        this.color = windowConfig.getColor();
        this.vSync = windowConfig.getvSync();
        this.primitiveCompatible = windowConfig.isPrimitivesCompatible();
        this.listener = listener;
    }

    public void createWindow() {

        GLFWErrorCallback.createPrint(System.err).set();


        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints(); // optional, the current mainContext hints are already the default
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL_FALSE); // the mainContext will stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL_TRUE); // the mainContext will be resizable

        if (!primitiveCompatible) {
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        }

        mainContext = GLFW.glfwCreateWindow(width, height, "xEngine", NULL, NULL);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        sideContext = GLFW.glfwCreateWindow(width, height, "second", NULL, mainContext);

        if (listener != null) {
            listener = GLFW.glfwSetKeyCallback(mainContext, listener);
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(mainContext, pWidth, pHeight);

            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());


            GLFW.glfwSetWindowPos(
                    mainContext,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }


        GLFW.glfwMakeContextCurrent(mainContext);
        mainCapabilities = GL.createCapabilities();

        detachContext();
        LOGGER.info("Created capabilietes for mainContext OPENGL");
        GLFW.glfwMakeContextCurrent(sideContext);
        sideCapabilities = GL.createCapabilities();


        detachContext();
        attachMainContext();

        GLFW.glfwSwapInterval(vSync);
        GL11.glClearColor(color.x, color.y, color.z, color.w);
    }

    public void detachContext() {
        LOGGER.info("Detached from cp:  {}", GL.getCapabilities());
        GLFW.glfwMakeContextCurrent(NULL);
        GL.setCapabilities(null);
    }


    public void showWindow() {
        GLFW.glfwShowWindow(mainContext);
    }

    public void processEvents() {
        GLFW.glfwPollEvents();
    }

    public void refreshWindow() {
        GLFW.glfwSwapBuffers(mainContext);
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(mainContext);
    }

    public void createContext(Context context) {
        //TODO
    }

    public void attachContext(Context context) {
        switch (context) {
            case MAIN:
                attachMainContext();
                break;
            case SIDE:
                attachSideContext();
                break;
            default:
                LOGGER.info("Requested context does not exist{}", context);
        }
    }


    public void setCallback(GLFWKeyCallback glfwKeyCallback) {
        GLFWKeyCallback was = GLFW.glfwSetKeyCallback(mainContext, glfwKeyCallback);
        was.free();
    }

    public void close() {
        LOGGER.info("Closing...");
        Callbacks.glfwFreeCallbacks(mainContext);
        Callbacks.glfwFreeCallbacks(sideContext);

        GLFW.glfwDestroyWindow(mainContext);
        GLFW.glfwDestroyWindow(sideContext);

        GLFW.glfwGetTime();
        GLFW.glfwGetTimerValue();

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
        GL.destroy();
    }

    private void attachMainContext() {
        GLFW.glfwMakeContextCurrent(mainContext);
        GL.setCapabilities(mainCapabilities);
        LOGGER.info("Attached to main context with cp: {}", GL.getCapabilities());
    }

    private void attachSideContext() {
        GLFW.glfwMakeContextCurrent(sideContext);
        GL.setCapabilities(sideCapabilities);
        LOGGER.info("Attached to side context with cp: {}", GL.getCapabilities());
    }

    public void scanErrors() {
        int error = GL11.glGetError();
        if (error != 0) {
            LOGGER.error("OpenGL Error: {}", error);
        }
    }
}
