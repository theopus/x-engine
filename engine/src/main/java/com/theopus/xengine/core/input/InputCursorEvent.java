package com.theopus.xengine.core.input;

import java.util.Map;

public class InputCursorEvent {
    public final double dx;
    public final double dy;
    private Map<InputAction, InputActionType> state;

    public InputCursorEvent(double dx, double dy, Map<InputAction, InputActionType> state) {
        this.dx = dx;
        this.dy = dy;
        this.state = state;
    }

    public InputActionType stateOf(InputAction action){
        return state.get(action);
    }
}
