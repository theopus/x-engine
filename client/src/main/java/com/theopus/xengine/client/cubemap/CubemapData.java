package com.theopus.xengine.client.cubemap;

public class CubemapData {
    public final String right;
    public final String left;
    public final String top;
    public final String bottom;
    public final String front;
    public final String back;

    public CubemapData(String right, String left, String top, String bottom, String front, String back) {
        this.right = right;
        this.left = left;
        this.top = top;
        this.bottom = bottom;
        this.front = front;
        this.back = back;
    }
}
