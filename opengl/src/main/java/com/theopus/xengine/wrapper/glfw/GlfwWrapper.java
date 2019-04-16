package com.theopus.xengine.wrapper.glfw;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.joml.Vector4f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlfwWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlfwWrapper.class);
    private int width;
    private int height;
    private long mainContext;
    private Vector4f color;
    private boolean primitiveCompatible;
    private GLFWKeyCallbackI listener;
    private GLCapabilities mainCapabilities;
    private int vSync;
    private long sideContext;
    private GLCapabilities sideCapabilities;

    public GlfwWrapper() {
        this(new WindowConfig(600, 400, new Vector4f(1, 0, 0, 0), false, 0));
    }

    public GlfwWrapper(WindowConfig windowConfig) {
        this.width = windowConfig.getWidth();
        this.height = windowConfig.getHeight();
        this.color = windowConfig.getColor();
        this.vSync = windowConfig.getvSync();
        this.primitiveCompatible = windowConfig.isPrimitivesCompatible();
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
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3); //opengl 3.*
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3); //opengl *.3
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

    public void clearColorBuffer() {
        GL30.glClear(GL11.GL_COLOR_BUFFER_BIT);
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


    public void setKeyCallback(GLFWKeyCallbackI glfwKeyCallback) {
        GLFWKeyCallback was = GLFW.glfwSetKeyCallback(mainContext, glfwKeyCallback);
        if (was != null) {
            was.free();
        }
    }
    public void setCursorPosCallback(GLFWCursorPosCallbackI glfwCursorPosCallback) {
        GLFWCursorPosCallback was = GLFW.glfwSetCursorPosCallback(mainContext, glfwCursorPosCallback);
        if (was != null) {
            was.free();
        }
    }

    public void setMouseButtonCallback(GLFWMouseButtonCallbackI glfwKeyCallback) {
        GLFWMouseButtonCallback was = GLFW.glfwSetMouseButtonCallback(mainContext, glfwKeyCallback);
        if (was != null) {
            was.free();
        }
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

    public void setShouldClose(boolean status) {
        GLFW.glfwSetWindowShouldClose(mainContext, status);
    }

    public void setFramebufferChangedCallback(GLFWFramebufferSizeCallbackI glfwFramebufferSizeCallbackI) {
        GLFWFramebufferSizeCallback was = GLFW.glfwSetFramebufferSizeCallback(mainContext, glfwFramebufferSizeCallbackI);
        if (was != null) {
            was.free();
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
