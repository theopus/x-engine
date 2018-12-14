package com.theopus.xengine.wrapper.opengl;

import com.theopus.xengine.wrapper.opengl.objects.Texture;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);
    protected final MemoryContext context;
    public Loader(MemoryContext context) {
        this.context = context;
    }

    public void close() {
        LOGGER.info("Closing...");
        context.close();
    }
}
