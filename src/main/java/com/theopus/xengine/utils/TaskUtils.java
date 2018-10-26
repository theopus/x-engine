package com.theopus.xengine.utils;

import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.platform.PlatformManager;
import com.theopus.xengine.nscheduler.task.ReadWriteTask;
import com.theopus.xengine.opengl.RenderTraitLoader;

import java.io.Closeable;

public class TaskUtils {

    public static ReadWriteTask loaderCloser(RenderTraitLoader loader) {
        return new ReadWriteTask(Context.MAIN, false) {
            @Override
            public void process() throws Exception {
                loader.close();
            }
        };
    }

    public static ReadWriteTask close(Context ctx, Closeable closeable) {
        return new ReadWriteTask(ctx, false) {
            @Override
            public void process() throws Exception {
                closeable.close();
            }
        };
    }

    public static ReadWriteTask close(Context ctx, AutoCloseable closeable) {
        return new ReadWriteTask(ctx, false) {
            @Override
            public void process() throws Exception {
                closeable.close();
            }
        };
    }


    public static ReadWriteTask initCtx(PlatformManager platformManager, Context ctx) {
        return new ReadWriteTask(ctx, false) {
            @Override
            public void process() throws Exception {
                platformManager.attachContext(ctx);
            }
        };
    }
}
