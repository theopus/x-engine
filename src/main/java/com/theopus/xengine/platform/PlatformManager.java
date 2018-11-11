package com.theopus.xengine.platform;

import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.input.InputManager;

public interface PlatformManager extends AutoCloseable {

    void createWindow();

    void showWindow();

    void createContext(Context context);

    void attachContext(Context context);

    void detachContext();

    void processEvents();

    void refreshWindow();

    boolean shouldClose();

    InputManager getInput();

}
