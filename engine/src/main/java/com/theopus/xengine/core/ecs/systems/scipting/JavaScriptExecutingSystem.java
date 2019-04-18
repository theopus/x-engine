package com.theopus.xengine.core.ecs.systems.scipting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.annotations.Wire;
import com.theopus.xengine.core.events.EventBus;
import com.theopus.xengine.core.events.Subscriber;

public class JavaScriptExecutingSystem extends ExecutingEngineSystem implements Subscriber<JavaStriptExecutionEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaScriptExecutingSystem.class);

    @Wire
    private ExecutingEngineContext context;
    @Wire
    private EventBus bus;
    private final ScriptEngine engine;

    public JavaScriptExecutingSystem() {
        engine = new ScriptEngineManager().getEngineByName("nashorn");

    }

    @Override
    protected void initialize() {
        super.initialize();
        engine.put("eec", context);
        Thread consoleThread = new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String statement = reader.readLine();
                    bus.post(new JavaStriptExecutionEvent(statement));
                } catch (IOException e) {
                    LOGGER.error("Read error:", e);
                }
            }
        });
        consoleThread.setDaemon(true);
        consoleThread.start();
    }

    @Override
    public void onEvent(JavaStriptExecutionEvent javaStriptExecutionEvent) {
        try {
            LOGGER.info("{}", javaStriptExecutionEvent.statement);
            Object eval = engine.eval(javaStriptExecutionEvent.statement);
            LOGGER.info("{}", eval);
        } catch (Exception e) {
            LOGGER.error("Eval error: ", e);
        }
    }
}
