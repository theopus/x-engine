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
}
