package com.theopus.xengine.wrapper.text;

import java.util.Arrays;

import com.theopus.xengine.wrapper.opengl.objects.TexturedVao;

public class GLString {

    private char[] string;
    private TexturedVao vao;

    public GLString(String string) {
        this.string = string.toCharArray();
    }

    public int length(){
        return string.length;
    }

    @Override
    public String toString() {
        return "GLString{" +
                "string=" + Arrays.toString(string) +
                '}';
    }
}
