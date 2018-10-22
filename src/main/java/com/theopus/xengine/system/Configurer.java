package com.theopus.xengine.system;

import com.theopus.xengine.conc.State;

public interface Configurer{

    void setRead(State state);
    void setWrite(State read, State state);
}
