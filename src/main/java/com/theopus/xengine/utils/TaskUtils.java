package com.theopus.xengine.utils;

import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.event.TopicReader;
import com.theopus.xengine.nscheduler.platform.PlatformManager;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import com.theopus.xengine.opengl.RenderTraitLoader;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;

import java.io.Closeable;
import java.util.Collection;

public class TaskUtils {

    public static ComponentTask loaderCloser(RenderTraitLoader loader) {
        return new ComponentTask(Context.MAIN, false) {
            @Override
            public void process() throws Exception {
                loader.close();
            }
        };
    }

    public static ComponentTask close(Context ctx, Closeable closeable) {
        return new ComponentTask(ctx, false) {
            @Override
            public void process() throws Exception {
                closeable.close();
            }
        };
    }

    public static ComponentTask close(Context ctx, Collection<Closeable> closeable) {
        return new ComponentTask(ctx, false) {
            @Override
            public void process() throws Exception {
                for (Closeable c : closeable) {
                    c.close();
                }
            }
        };
    }

    public static ComponentTask closeAutos(Context ctx, Collection<AutoCloseable> closeable) {
        return new ComponentTask(ctx, false) {
            @Override
            public void process() throws Exception {
                for (AutoCloseable c : closeable) {
                    c.close();
                }
            }
        };
    }

    public static ComponentTask close(Context ctx, AutoCloseable closeable) {
        return new ComponentTask(ctx, false) {
            @Override
            public void process() throws Exception {
                closeable.close();
            }
        };
    }


    public static ComponentTask initCtx(PlatformManager platformManager, Context ctx) {
        return new ComponentTask(ctx, false) {
            @Override
            public void process() throws Exception {
                platformManager.attachContext(ctx);
            }
        };
    }

    public static ComponentTask frameBufferRefresh(TopicReader<Vector2i> reader){
        return new ComponentTask(Context.MAIN, false, 60, 10) {
            private final TopicReader<Vector2i> _reader = reader;
            {
                components.add(_reader);
            }
            @Override
            public void process() throws Exception {
                reader.read().forEach(event -> GL11.glViewport(0, 0, event.data().x, event.data().y));
            }
        };

    }
}
