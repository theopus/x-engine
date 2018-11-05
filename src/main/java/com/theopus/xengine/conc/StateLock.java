package com.theopus.xengine.conc;

import com.theopus.xengine.nscheduler.lock.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateLock extends Lock<State> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateLock.class);

    public StateLock(int id, com.theopus.xengine.conc.State of) {
        super(id, of);
    }

    @Override
    public void resolve(Lock<com.theopus.xengine.conc.State> lastLock) {
        LOGGER.warn("Collision {}->{}, applying over {}", this.getFrame(), this.getNextFrame(), lastLock.getFrame());
        lastLock.getOf().getManager().copyTo(this.getOf().getManager());
        getOf().getManager().reApplyTransformations();

    }
}
