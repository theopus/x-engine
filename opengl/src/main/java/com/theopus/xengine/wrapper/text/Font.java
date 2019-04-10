package com.theopus.xengine.wrapper.text;

import java.util.ArrayList;
import java.util.List;

import com.sun.deploy.util.ArrayUtil;
import com.theopus.xengine.wrapper.font.TextMeshData;
import com.theopus.xengine.wrapper.opengl.objects.Texture;

public class Font {

    private final String name;
    private final String fntFile;
    private final Texture texture;
    private final int fontSize;
    private MetaFile metaFile;

    public Font(String name, String fntFile, Texture texture, int fontSize) {
        this.name = name;
        this.fntFile = fntFile;
        this.texture = texture;
        this.fontSize = fontSize;
    }

    public void load(double aspectRatio){
        metaFile = new MetaFile(fntFile, aspectRatio);
    }

    public double spaceWidth(){
        return metaFile.getSpaceWidth();
    }

    public Character getChar(char character){
        return metaFile.getCharacter(character);
    }

    public Character getChar(int ascii){
        return metaFile.getCharacter(ascii);
    }

    public String getName() {
        return name;
    }

    public int getFontSize() {
        return fontSize;
    }

    public TextMeshData createString(String string, float size){
        char[] chars = string.toCharArray();

        List<Float> vertices = new ArrayList<>();
        List<Float> uvs = new ArrayList<>();

        List<Character> characters = new ArrayList<>();
        for (char aChar : chars) {
            characters.add(getChar(aChar));
        }

        double curserX = 0f;
        double curserY = 0f;

        for (Character character : characters) {
            if (character != null) {
                addVerticesForCharacter(curserX, curserY, character, size, vertices);
                addTexCoords(uvs, character.getxTextureCoord(), character.getyTextureCoord(),
                        character.getXMaxTextureCoord(), character.getYMaxTextureCoord());
                curserX += character.getxAdvance() * size;
            } else {
                curserX += metaFile.getSpaceWidth() * size;
            }
        }
        return new TextMeshData(listToArray(vertices), listToArray(uvs));
    }

    private static float[] listToArray(List<Float> listOfFloats) {
        float[] array = new float[listOfFloats.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = listOfFloats.get(i);
        }
        return array;
    }

    private void addVerticesForCharacter(double curserX, double curserY, Character character, double fontSize,
                                         List<Float> vertices) {
        double x = curserX + (character.getxOffset() * fontSize);
        double y = curserY + (character.getyOffset() * fontSize);
        double maxX = x + (character.getSizeX() * fontSize);
        double maxY = y + (character.getSizeY() * fontSize);
        double properX = (2 * x) - 1;
        double properY = (-2 * y) + 1;
        double properMaxX = (2 * maxX) - 1;
        double properMaxY = (-2 * maxY) + 1;
        addVertices(vertices, properX, properY, properMaxX, properMaxY);
    }

    private static void addVertices(List<Float> vertices, double x, double y, double maxX, double maxY) {
        vertices.add((float) x);
        vertices.add((float) y);
        vertices.add((float) x);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) y);
        vertices.add((float) x);
        vertices.add((float) y);
    }

    private static void addTexCoords(List<Float> texCoords, double x, double y, double maxX, double maxY) {
        texCoords.add((float) x);
        texCoords.add((float) y);
        texCoords.add((float) x);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) y);
        texCoords.add((float) x);
        texCoords.add((float) y);
    }

    public Texture getTexture() {
        return texture;
    }
}
