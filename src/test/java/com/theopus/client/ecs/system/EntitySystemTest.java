package com.theopus.client.ecs.system;

import com.theopus.xengine.ecs.mapper.EntityManager;
import com.theopus.xengine.ecs.system.EntitySystem;
import org.junit.Test;

import java.util.BitSet;

public class EntitySystemTest {

    public static class TestES extends EntitySystem {

        private EntityManager manager;

        public TestES() {
            super(null, false, 1);
        }

        @Override
        public void process(BitSet entities) {

        }
    }

    @Test
    public void name() {
        new TestES();
    }
}