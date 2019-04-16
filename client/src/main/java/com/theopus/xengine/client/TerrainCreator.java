package com.theopus.xengine.client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.joml.Vector3f;

import com.theopus.xengine.wrapper.opengl.Loader;

import de.matthiasmann.twl.utils.PNGDecoder;

public class TerrainCreator  {

    int MAX_PIXEL_COLOUR = 256 + 256 + 256;
    public static final int TILE_SIZE = 100;


    public TerrainData loadTerrain() {

//        ByteBuffer buffer;
//        int width;
//        int height;

//        try (InputStream resourceAsStream = Loader.class.getClassLoader().getResourceAsStream("heightmap.png")) {
//            PNGDecoder decoder = new PNGDecoder(resourceAsStream);
//            width = decoder.getWidth();
//            height = decoder.getHeight();
//            buffer = ByteBuffer.allocate(4 * width * height);
//            decoder.decode(buffer, height * 4, PNGDecoder.Format.RGBA);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//        int xpoiner = 0;
//        int ypoiner = 0;
//        int[][] values = new int[width][height];
//        for (int i = 0; i < buffer.limit(); i += 4) {
//            int integer = buffer.get(i) & 0b11111111;
//            int integer1 = buffer.get(i + 1) & 0b11111111;
//            int integer2 = buffer.get(i + 2) & 0b11111111;
//            int integer3 = buffer.get(i + 3) & 0b11111111;
//
//            values[xpoiner][ypoiner] = integer + integer1 + integer2;
//            ypoiner++;
//            if (ypoiner % width == 0) {
//                ypoiner = 0;
//                xpoiner++;
//            }
//        }

        int VERTICES_PER_SIDE = 2;
        int TOTAL_NUMBER = VERTICES_PER_SIDE * VERTICES_PER_SIDE;


        int QUADS_PER_SIDE = VERTICES_PER_SIDE - 1;
        int TOTAL_QUADS = QUADS_PER_SIDE * QUADS_PER_SIDE;


        float[] vertexes = new float[TOTAL_NUMBER * 3];
        float[] uvs = new float[TOTAL_NUMBER * 2];
        float[] normals = new float[TOTAL_NUMBER * 3];

        int[] indexes = new int[TOTAL_QUADS * 6];

        int vertexesCount = 0;
        int normalsCount = 0;
        int uvsCount = 0;
        //i==x

//        float[][] heights = new float[width][height];
        //j==z
        for (int i = 0; i < VERTICES_PER_SIDE; i++) {
            for (int j = 0; j < VERTICES_PER_SIDE; j++) {
                vertexes[vertexesCount++] = (float) j / ((float) VERTICES_PER_SIDE - 1) * TILE_SIZE;
//                heights[j][i] = getHight(j, i, values, width, height);
//                vertexes[vertexesCount++] = heights[j][i];
                vertexes[vertexesCount++] = 0f;
                vertexes[vertexesCount++] = (float) i / ((float) VERTICES_PER_SIDE - 1) * TILE_SIZE;

                uvs[uvsCount++] = (float) j / ((float) VERTICES_PER_SIDE - 1);
                ;
                uvs[uvsCount++] = (float) i / ((float) VERTICES_PER_SIDE - 1);

//                Vector3f vector3f = calculateNormal(j, i, values, width, height);
//                normals[normalsCount++] = vector3f.x;
//                normals[normalsCount++] = vector3f.y;
//                normals[normalsCount++] = vector3f.z;
            }
        }

        int indexesCount = 0;
        for (int x = 0; x < VERTICES_PER_SIDE - 1; x++) {
            for (int z = 0; z < VERTICES_PER_SIDE - 1; z++) {
                int topLeft = z + (x * VERTICES_PER_SIDE);
                int topRight = topLeft + 1;
                int bottomLeft = z + ((x + 1) * VERTICES_PER_SIDE);
                int bottomRight = bottomLeft + 1;

                indexes[indexesCount++] = topLeft;
                indexes[indexesCount++] = bottomLeft;
                indexes[indexesCount++] = topRight;
                indexes[indexesCount++] = topRight;
                indexes[indexesCount++] = bottomLeft;
                indexes[indexesCount++] = bottomRight;
            }
        }


        float[] vertexes1 = vertexes;
        int[] indexes1 = indexes;
        float[] uvs1 = uvs;
        float[] normals1 = normals;
        return new TerrainData(vertexes1, uvs1, indexes);
    }


    private float getHight(int x, int z, int[][] image, int heiht, int width) {
        if (x < 0 || x >= heiht || z < 0 || z >= width) {
            return 0;
        }

        float height = image[z][x];

        height += MAX_PIXEL_COLOUR / 2f;
        height /= MAX_PIXEL_COLOUR / 2f;
        height *= 40;
        return height - 80;
    }

    private Vector3f calculateNormal(int x, int z, int[][] image, int height, int width) {
        float heidhtL = getHight(x - 1, z, image, height, width);
        float heidhtR = getHight(x + 1, z, image, height, width);
        float heidhtD = getHight(x, z - 1, image, height, width);
        float heidhtU = getHight(x, z + 1, image, height, width);
        return new Vector3f(heidhtL - heidhtR, 2f, heidhtD - heidhtU).normalize();
    }



}
