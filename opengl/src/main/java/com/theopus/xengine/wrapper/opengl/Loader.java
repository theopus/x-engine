package com.theopus.xengine.wrapper.opengl;

import java.io.Closeable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
