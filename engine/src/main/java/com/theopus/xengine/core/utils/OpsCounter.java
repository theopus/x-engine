package com.theopus.xengine.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpsCounter {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpsCounter.class);
    private static final float secondInMillis = 1000.0f;

    private long current;
    private long previous = System.nanoTime();
    private String name;
    private long operationTime;

    private long lastPrintTime = System.currentTimeMillis();
    private long deltaToPrint = 3000;

    public OpsCounter(String name) {
        this.name = name;
    }

    public void operate() {
        current = System.nanoTime();
        operationTime = current - previous;
        previous = current;
    }

    public void printOps() {
        if (System.currentTimeMillis() - lastPrintTime > deltaToPrint) {
            float currentOps = 1_000_000_000f / operationTime;
            LOGGER.info("{}: [{}] per/sec", name, String.format("%.02f", currentOps));
            lastPrintTime = System.currentTimeMillis();
        }
    }

    public void operateAndLog() {
        operate();
        printOps();
    }
}
