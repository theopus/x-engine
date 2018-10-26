package com.theopus.xengine.nscheduler.platform;

import com.theopus.xengine.WindowConfig;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.input.GlfwInput;
import com.theopus.xengine.nscheduler.input.InputManager;
import org.joml.Vector4f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GlfwPlatformManager implements PlatformManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlfwPlatformManager.class);

    private GlfwInput hub;
    private Vector4f color;
    private boolean primitiveCompatible;
    private GLFWKeyCallback listener;
    public int width;
    public int height;
    public long mainContext;
    private GLCapabilities mainCapabilities;
    private int vSync;
    private long sideContext;
    private GLCapabilities sideCapabilities;

    public GlfwPlatformManager(WindowConfig windowConfig) {
        this.width = windowConfig.getWidth();
        this.height = windowConfig.getHeight();
        this.hub = new GlfwInput();
        this.color = windowConfig.getColor();
        this.vSync = windowConfig.getvSync();
        this.primitiveCompatible = windowConfig.isPrimitivesCompatible();
    }

    public GlfwPlatformManager(WindowConfig windowConfig, GLFWKeyCallback listener) {
        this.width = windowConfig.getWidth();
        this.height = windowConfig.getHeight();
        this.color = windowConfig.getColor();
        this.vSync = windowConfig.getvSync();
        this.primitiveCompatible = windowConfig.isPrimitivesCompatible();
        this.listener = listener;
    }

    public GlfwPlatformManager(int width, int height) {
        this.width = width;
        this.height = height;
        this.color = new Vector4f(0, 0, 0, 1);
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
        sideContext = GLFW.glfwCreateWindow(width, height, "", NULL, mainContext);

        if (listener != null) {
            listener = GLFW.glfwSetKeyCallback(mainContext, listener);
        }
        if (hub != null) {
            GLFW.glfwSetKeyCallback(mainContext, hub.keyCallback());
            GLFW.glfwSetMouseButtonCallback(mainContext, hub.mouseButtonCallback());
            GLFW.glfwSetCursorPosCallback(mainContext, hub.mousePosCallback());
            GLFW.glfwSetScrollCallback(mainContext, hub.scrollCallback());
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

    @Override
    public void processEvents() {
//        int errorCode = GL11.glGetError();
//        if (errorCode != 0) {
//            LOGGER.info("OpenGL Error: {}", errorCode);
//        }
        GLFW.glfwPollEvents();
    }

    @Override
    public void refreshWindow() {
        GLFW.glfwSwapBuffers(mainContext);
    }

    @Override
    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(mainContext);
    }

    @Override
    public void createContext(Context context) {
        //TODO
    }

    @Override
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

    @Override
    public InputManager getInput() {
        return hub;
    }
}
