package com.theopus.xengine.system;

import com.theopus.xengine.trait.State;
import com.theopus.xengine.trait.StateManager;

public interface Configurer {

    StateManager.LockType type();

    void setRead(State state);
    void setWrite(State state);

}
