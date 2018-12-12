package com.theopus.xengine.core;

public class Loop {
    private final BoolFunction condition;
    private final RunFunction run;
    private final VoidFunction onClose;

    public Loop(BoolFunction condition, RunFunction run, VoidFunction onClose) {
        this.condition = condition;
        this.run = run;
        this.onClose = onClose;
    }

    interface RunFunction {
        void run(long elapsed);
    }

    interface BoolFunction {
        boolean test();
    }

    interface VoidFunction {
        void execute();
    }

    public void run() {
        long before = System.currentTimeMillis();
        long now;
        long elapsed;

        while (condition.test()) {
            now = System.currentTimeMillis();
            elapsed = now - before;
            before = now;
            run.run(elapsed);
        }
        onClose.execute();
    }

    public static class Builder {
        private Loop.BoolFunction condition;
        private Loop.RunFunction run;
        private Loop.VoidFunction onClose;

        public Builder setCondition(Loop.BoolFunction condition) {
            this.condition = condition;
            return this;
        }

        public Builder setRun(Loop.RunFunction run) {
            this.run = run;
            return this;
        }

        public Builder setOnClose(Loop.VoidFunction onClose) {
            this.onClose = onClose;
            return this;
        }

        public Loop createLoop() {
            return new Loop(condition, run, onClose);
        }
    }

}
