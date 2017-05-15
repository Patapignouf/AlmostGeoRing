/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.projet.pidr;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Pyramid {
    private FloatBuffer vertexBuffer;  // Buffer for vertex-array
    private FloatBuffer colorBuffer;   // Buffer for color-array
    private ByteBuffer indexBuffer;    // Buffer for index-array

    private float colorsurface1 = ((float) (Math.random()*1));
    private float colorsurface2 = ((float) (Math.random()*1));
    private float colorsurface3 = ((float) (Math.random()*1));
    private float colorsurface4 = ((float) (Math.random()*1));



    private float[] vertices;
    private int numFaces ;



    private float[] colors = {  // Colors of the 5 vertices in RGBA
            /*
            0.0f, 0.0f, 1.0f, 1.0f,  // 0. blue
            0.0f, 1.0f, 0.0f, 1.0f,  // 1. green
            0.0f, 0.0f, 1.0f, 1.0f,  // 2. blue
            0.0f, 1.0f, 0.0f, 1.0f,  // 3. green
            1.0f, 0.0f, 0.0f, 1.0f   // 4. red
            */

            0.0f, 0.0f, 1.0f, 1.0f,  // 0. blue
            0.0f, 0.0f, 1.0f, 1.0f,  // 0. blue
            0.0f, 0.0f, 1.0f, 1.0f,  // 0. blue
            0.0f, 0.0f, 1.0f, 1.0f,  // 0. blue
            0.0f, 0.0f, 1.0f, 1.0f,  // 0. blue
    };

    private byte[] indices = { // Vertex indices of the 4 Triangles
            2, 2, 2, 2,   // front face (CCW)
            1, 4, 2, 1,  // right face
            0, 4, 1, 3,  // back face
            4, 0, 3, 4,   // left face
            2, 2, 2, 2,
            2, 2, 2, 2,
            2, 2, 2, 2
    };

    // Constructor - Set up the buffers
    public Pyramid(float[] vertices, int numFaces) {
        this.vertices = vertices;
        this.numFaces = numFaces;
        // Setup vertex-array buffer. Vertices in float. An float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);           // Rewind

        // Setup color-array buffer. Colors in float. An float has 4 bytes
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        // Setup index-array buffer. Indices in byte.
        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    // Draw the shape
    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CCW);  // Front face in counter-clockwise orientation

        // Enable arrays and define their buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        //gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        gl.glColor4f( colorsurface1, colorsurface2, colorsurface3, colorsurface4);
        //gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE,indexBuffer);

        for (int face = 0; face < numFaces; face++) {
            // Set the color for each of the faces

            // Draw the primitive from the vertex-array directly
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, face*4, 4);
        }

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}