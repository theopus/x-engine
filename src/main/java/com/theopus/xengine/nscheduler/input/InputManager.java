package com.theopus.xengine.nscheduler.input;

public interface InputManager {

    boolean isKeyDown(int k);

    InputReader createReader();
}
