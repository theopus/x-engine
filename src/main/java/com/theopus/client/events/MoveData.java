package com.theopus.client.events;

public class MoveData {

    private int left = -1;
    private int right = -1;
    private int forward = -1;
    private int back = -1;
    private int top = -1;
    private int bot = -1;

    private int rotX = -1;
    private int rotY = -1;
    private int rotZ = -1;

    public MoveData left(boolean value) {
        left = value ? 1 : 0;
        return this;
    }

    public MoveData right(boolean value) {
        right = value ? 1 : 0;
        return this;
    }

    public MoveData forward(boolean value) {
        forward = value ? 1 : 0;
        return this;
    }

    public MoveData back(boolean value) {
        back = value ? 1 : 0;
        return this;
    }

    public MoveData top(boolean value) {
        top = value ? 1 : 0;
        return this;
    }

    public MoveData bot(boolean value) {
        bot = value ? 1 : 0;
        return this;
    }

    public MoveData rotX(boolean value) {
        rotX = value ? 1 : 0;
        return this;
    }

    public MoveData rotY(boolean value) {
        rotY = value ? 1 : 0;
        return this;
    }

    public MoveData rotZ(boolean value) {
        rotZ = value ? 1 : 0;
        return this;
    }

    public int left() {
        return left;
    }

    public int right() {
        return right;
    }

    public int forward() {
        return forward;
    }

    public int back() {
        return back;
    }

    public int top() {
        return top;
    }

    public int bot() {
        return bot;
    }

    public int rotX() {
        return rotX;
    }

    public int rotY() {
        return rotY;
    }

    public int rotZ() {
        return rotZ;
    }
}
