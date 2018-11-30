package com.theopus.xengine.core.input;

public class InputEvent {
    public final InputAction action;
    public final InputActionType type;

    public InputEvent(InputAction action, InputActionType type) {
        this.action = action;
        this.type = type;
    }
}
