package com.theopus.xengine.utils;

import org.roaringbitmap.BitSetUtil;

import java.util.BitSet;

public class BitSetUtils {

    public static BitSet cross(BitSet s0, BitSet s2){
        BitSet set = new BitSet();
        set.or(s0);
        set.and(s2);
        return set;
    }

    public static BitSet cross(BitSet s0, BitSet s2, BitSet target){
        target.clear();
        target.or(s0);
        target.and(s2);
        return target;
    }


    public static BitSet xor(BitSet s0, BitSet s2){
        BitSet set = new BitSet();
        set.or(s0);
        set.xor(s2);
        return set;
    }

    public static BitSet xor(BitSet s0, BitSet s2, BitSet target){
        target.clear();
        target.or(s0);
        target.xor(s2);
        return target;
    }
}
