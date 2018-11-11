package com.theopus.xengine.utils;

import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.platform.PlatformManager;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import com.theopus.xengine.opengl.SimpleLoader;

import java.io.Closeable;
import java.util.Collection;

public class TaskUtils {

    public static ComponentTask loaderCloser(SimpleLoader loader) {
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
    public static ComponentTask teardownCtx(PlatformManager platformManager, Context ctx) {
        return new ComponentTask(ctx, false) {
            @Override
            public void process() throws Exception {
                platformManager.detachContext();
            }
        };
    }
}
