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
        for (int i = 0; i < VERTICES_PER_SIDE; i++) {
            for (int j = 0; j < VERTICES_PER_SIDE; j++) {
                vertexes[vertexesCount++] = (float) j / ((float) VERTICES_PER_SIDE - 1) * TILE_SIZE;
                vertexes[vertexesCount++] = 0f;
                vertexes[vertexesCount++] = (float) i / ((float) VERTICES_PER_SIDE - 1) * TILE_SIZE;

                uvs[uvsCount++] = (float) j / ((float) VERTICES_PER_SIDE - 1);
                ;
                uvs[uvsCount++] = (float) i / ((float) VERTICES_PER_SIDE - 1);
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
