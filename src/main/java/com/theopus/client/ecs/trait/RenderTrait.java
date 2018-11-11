package com.theopus.client.ecs.trait;


import com.theopus.xengine.ecs.Trait;

/**
 * Trait - marker
 */

public class RenderTrait extends Trait {
    @Override
    public Object duplicateTo(Object o) {
        return o;
    }
}
