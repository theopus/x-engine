package com.theopus.xengine.core.platform;

public interface PlatformManager {
    void init();

    void createWindow();

    boolean shouldClose();
    
    void processEvents();

    void refreshWindow();

    void close();
}
