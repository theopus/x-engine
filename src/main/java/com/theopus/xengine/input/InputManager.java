package com.theopus.xengine.input;

public interface InputManager {

    boolean isKeyDown(int k);

    InputReader createReader();

    void prepare();

    void finish();
}
