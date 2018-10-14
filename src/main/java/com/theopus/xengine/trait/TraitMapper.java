package com.theopus.xengine.trait;

import com.theopus.xengine.utils.Reflection;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class TraitMapper<Trait extends com.theopus.xengine.trait.Trait> {

    private Class<Trait> traitClass;
    private Map<Integer, Trait> traits;

    private BitSet available;

    private TraitEditor<Trait> editor;

    public TraitMapper(Class<Trait> traitClass, TraitEditor<Trait> editor) {
        this.traitClass = traitClass;
        this.editor = editor;
        this.editor.with(this);
        this.traits = new HashMap<>();
        this.available = new BitSet();
    }

    public boolean has(int entityId) {
        return available.get(entityId);
    }

    public Trait get(int entityId) {
        boolean contains = available.get(entityId);
        if (contains) {
            return traits.get(entityId);
        } else {
            Trait trait = Reflection.newInstance(traitClass);
            available.set(entityId);
            traits.put(entityId, trait);
            return trait;
        }
    }

    public Trait remove(int entityId) {
        available.set(entityId, false);
        return traits.remove(entityId);
    }

    public BitSet traitsBits() {
        return available;
    }

    public Map<Integer, Trait> traits() {
        return traits;
    }

    public TraitEditor<Trait> getEditor() {
        return editor;
    }

    public void clearEditor(){
        editor.clear();
    }

    public Class<Trait> getTraitClass() {
        return traitClass;
    }
}
