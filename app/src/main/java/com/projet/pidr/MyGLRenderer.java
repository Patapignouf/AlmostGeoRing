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
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import java.util.ArrayList;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Pyramid[] pyramid;    // (NEW)
    private Pyramid pyramid2;
    private Pyramid pyramid3;
    private Pyramid[][] pyramid6;
    private Pyramid[] pyramid4= new Pyramid[9];
    //private Cube cube;          // (NEW)

    private static float anglePyramid = 0; // Rotational angle in degree for pyramid (NEW)
    private static float angleCube = 0;    // Rotational angle in degree for cube (NEW)
    private static float speedPyramid = 2.0f; // Rotational speed for pyramid (NEW)
    private static float speedCube = -1.5f;   // Rotational speed for cube (NEW)
    float angleX = 0;   // (NEW)
    float angleY = 0;   // (NEW)
    float speedX = 0;   // (NEW)
    float speedY = 0;   // (NEW)
    public static float z = -6.0f;    // (NEW)
    private float[] vertices = {  // Vertices of the 6 faces
            // FRONT
            -1.0f, -1.0f,  1.0f,  // 0. left-bottom-front    D/H/P
            1.0f, -1.0f,  1.0f,  // 1. right-bottom-front
            -1.0f,  1.0f,  1.0f,  // 2. left-top-front
            1.0f,  1.0f,  1.0f,  // 3. right-top-front
            // BACK
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            1.0f,  1.0f, -1.0f,  // 7. right-top-back
            -1.0f,  1.0f, -1.0f,  // 5. left-top-back
            // LEFT
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            -1.0f, -1.0f,  1.0f,  // 0. left-bottom-front
            -1.0f,  1.0f, -1.0f,  // 5. left-top-back
            -1.0f,  1.0f,  1.0f,  // 2. left-top-front
            // RIGHT
            1.0f, -1.0f,  1.0f,  // 1. right-bottom-front
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            1.0f,  1.0f,  1.0f,  // 3. right-top-front
            1.0f,  1.0f, -1.0f,  // 7. right-top-back
            // TOP
            -1.0f,  1.0f,  1.0f,  // 2. left-top-front
            1.0f,  1.0f,  1.0f,  // 3. right-top-front
            -1.0f,  1.0f, -1.0f,  // 5. left-top-back
            1.0f,  1.0f, -1.0f,  // 7. right-top-back
            // BOTTOM
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            -1.0f, -1.0f,  1.0f,  // 0. left-bottom-front
            1.0f, -1.0f,  1.0f,   // 1. right-bottom-front

            1.0f, 1.0f, 1.0f,  // D/H/P
            0.0f, 1.5f, 1.0f,  //
            1.0f, 1.0f, -1.0f,  //
            0.0f, 1.5f, -1.0f,   //


            -1.0f, 1.0f, 1.0f,  //
            0.0f, 1.5f, 1.0f,  //
            -1.0f, 1.0f, -1.0f,  //
            0.0f, 1.5f, -1.0f,   //

            1.0f, 1.0f, 1.0f,  //
            -1.0f, 1.0f, 1.0f,  //
            0.0f, 1.5f,  1.0f,  //
            0.0f, 1.5f,  1.0f,  //

    };

    private float[] vertices2 = {  // Vertices of the 6 faces
            // FRONT
            2.0f, -1.0f,  1.0f,  // 0. left-bottom-front    D/H/P
            4.0f, -1.0f,  1.0f,  // 1. right-bottom-front
            2.0f,  1.0f,  1.0f,  // 2. left-top-front
            4.0f,  1.0f,  1.0f,  // 3. right-top-front
            // BACK
            4.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            2.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            4.0f,  1.0f, -1.0f,  // 7. right-top-back
            2.0f,  1.0f, -1.0f,  // 5. left-top-back
            // LEFT
            2.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            2.0f, -1.0f,  1.0f,  // 0. left-bottom-front
            2.0f,  1.0f, -1.0f,  // 5. left-top-back
            2.0f,  1.0f,  1.0f,  // 2. left-top-front
            // RIGHT
            4.0f, -1.0f,  1.0f,  // 1. right-bottom-front
            4.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            4.0f,  1.0f,  1.0f,  // 3. right-top-front
            4.0f,  1.0f, -1.0f,  // 7. right-top-back
            // TOP
            2.0f,  1.0f,  1.0f,  // 2. left-top-front
            4.0f,  1.0f,  1.0f,  // 3. right-top-front
            2.0f,  1.0f, -1.0f,  // 5. left-top-back
            4.0f,  1.0f, -1.0f,  // 7. right-top-back
            // BOTTOM
            2.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            4.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            2.0f, -1.0f,  1.0f,  // 0. left-bottom-front
            4.0f, -1.0f,  1.0f,   // 1. right-bottom-front

            4.0f, 1.0f, 1.0f,  // D/H/P
            3.0f, 1.5f, 1.0f,  //
            4.0f, 1.0f, -1.0f,  //
            3.0f, 1.5f, -1.0f,   //


            2.0f, 1.0f, 1.0f,  //
            3.0f, 1.5f, 1.0f,  //
            2.0f, 1.0f, -1.0f,  //
            3.0f, 1.5f, -1.0f,   //

            4.0f, 1.0f, 1.0f,  //
            2.0f, 1.0f, 1.0f,  //
            3.0f, 1.5f,  1.0f,  //
            3.0f, 1.5f,  1.0f,  //
    };

    private float[][] vertices3 = {{  // Vertices of the 6 faces
            // FRONT
            2.0f, -1.0f,  1.0f,  // 0. left-bottom-front    D/H/P
            4.0f, -1.0f,  1.0f,  // 1. right-bottom-front
            2.0f,  1.0f,  1.0f,  // 2. left-top-front
            4.0f,  1.0f,  1.0f },{  // 3. right-top-front
            // BACK
            4.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            2.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            4.0f,  1.0f, -1.0f,  // 7. right-top-back
            2.0f,  1.0f, -1.0f},{  // 5. left-top-back
            // LEFT
            2.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            2.0f, -1.0f,  1.0f,  // 0. left-bottom-front
            2.0f,  1.0f, -1.0f,  // 5. left-top-back
            2.0f,  1.0f,  1.0f},{  // 2. left-top-front
            // RIGHT
            4.0f, -1.0f,  1.0f,  // 1. right-bottom-front
            4.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            4.0f,  1.0f,  1.0f,  // 3. right-top-front
            4.0f,  1.0f, -1.0f},{  // 7. right-top-back
            // TOP
            2.0f,  1.0f,  1.0f,  // 2. left-top-front
            4.0f,  1.0f,  1.0f,  // 3. right-top-front
            2.0f,  1.0f, -1.0f,  // 5. left-top-back
            4.0f,  1.0f, -1.0f},{  // 7. right-top-back
            // BOTTOM
            2.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            4.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            2.0f, -1.0f,  1.0f,  // 0. left-bottom-front
            4.0f, -1.0f,  1.0f},{   // 1. right-bottom-front

            4.0f, 1.0f, 1.0f,  // D/H/P
            3.0f, 1.5f, 1.0f,  //
            4.0f, 1.0f, -1.0f,  //
            3.0f, 1.5f, -1.0f},{   //


            2.0f, 1.0f, 1.0f,  //
            3.0f, 1.5f, 1.0f,  //
            2.0f, 1.0f, -1.0f,  //
            3.0f, 1.5f, -1.0f},{   //

            4.0f, 1.0f, 1.0f,  //
            2.0f, 1.0f, 1.0f,  //
            3.0f, 1.5f,  1.0f,  //
            3.0f, 1.5f,  1.0f}  //
    };


    public Pyramid[][] PPPP(ArrayList<ArrayList<ArrayList<Point3D>>> listlistlistPoints ) {
        Pyramid[][] pyramid5 = new Pyramid[listlistlistPoints.size()][];
        ArrayList<ArrayList<Point3D>> listlistPoints = new ArrayList<ArrayList<Point3D>>();

        Log.d("info","coucou");
        for (int i = 0; i < listlistlistPoints.size(); i++) {
            listlistPoints = listlistlistPoints.get(i);
            pyramid5 = new Pyramid[listlistlistPoints.size()][listlistPoints.size()];
            float colorsurface4 = ((float) (Math.random() * 1));
            float colorsurface2 = ((float) (Math.random() * 1));
            float colorsurface3 = ((float) (Math.random() * 1));
            Log.d("info", String.valueOf(listlistPoints.size()));
            for (int j = 0; j < listlistPoints.size(); j++) {
                Log.d("info", String.valueOf(j));
                pyramid5[i][j] = new Pyramid(PPP(listlistPoints)[j], colorsurface4, colorsurface2, colorsurface3, 1);

                Log.d("info2", String.valueOf(PPP(listlistPoints)[j]));


            }


        }
        return pyramid5;
    }




    public float[][] PPP(ArrayList<ArrayList<Point3D>> listlistPoints) {
        //Fonction permettant de réaliser les triangles OpenGL à partir du maillage.
        //On initialise les variables utiles

        ArrayList<Point3D> currentTriangle = new ArrayList<Point3D>();
        float[][] resultfin = new float[listlistPoints.size()][12];


        for (int j = 0; j <= listlistPoints.size() - 1; j++) {
                    //Log.d("info", "Nombre de figures affichées :" + String.valueOf(j));



                    currentTriangle = listlistPoints.get(j);
                    resultfin[j]  =  ConvertToArray(currentTriangle);
        }

        return resultfin;
    }


    public float[] ConvertToArray(ArrayList<Point3D> listPoints){
        float[] result = new float[listPoints.size()*4];
        //On va créer un tableau de float de trois fois la taille du tableau de points pour y mettre chaque coordonnée

        for (int i=0; i<listPoints.size(); i++){
            int j = i*3;
            result[j]=listPoints.get(i).x;
            result[j+1]=listPoints.get(i).y;
            result[j+2]=listPoints.get(i).z;

            //Chaque point est divisé de la sorte -> x,y,z pour pouvoir être tracé par OpenGL
        }
        result[9]=listPoints.get(0).getX();
        result[10]=listPoints.get(0).getY();
        result[11]=listPoints.get(0).getZ();
        Log.d("info", "result :"+Float.toString(result[0]));
        return result;
    }


    // Constructor
    public MyGLRenderer(Context context, ArrayList<ArrayList<Point3D>> listePoints, float rayon) {
        // Set up the buffers for these shapes
        pyramid = new Pyramid[listePoints.size()];
        float[] tampon;
        for (int i = 0; i<listePoints.size(); i++) {
            //pyramid[i] = new Pyramid(ConvertToArray(PPP(listePoints.get(i), rayon)), ConvertToArray(PPP(listePoints.get(i), rayon)).length / 12);   // (NEW)
        }

        //pyramid2 = new Pyramid(vertices2, 9);
        //cube = new Cube();         // (NEW)
    }
    public MyGLRenderer(Context context,  ArrayList<ArrayList<ArrayList<Point3D>>> data) {
        // Set up the buffers for these shapes
        float colorsurface4 = ((float) (Math.random()*1));
        float colorsurface2 = ((float) (Math.random()*1));
        float colorsurface3 = ((float) (Math.random()*1));

        //Pyramid[][] pyramid5 = new Pyramid[listlistlistPoints.size()][];*

        //pyramid2 = new Pyramid(vertices, 9);
        //pyramid3 = new Pyramid(vertices2, 9);
        ArrayList<ArrayList<ArrayList<Point3D>>> test = new ArrayList<ArrayList<ArrayList<Point3D>>>();
        ArrayList<ArrayList<Point3D>> test2 = new ArrayList<ArrayList<Point3D>>();
        ArrayList<Point3D> test3 = new ArrayList<Point3D>();
        ArrayList<Point3D> test4 = new ArrayList<Point3D>();
        ArrayList<Point3D> test5 = new ArrayList<Point3D>();
        test3.add(new Point3D(0.5f, 1.0f, 0.5f));
        test3.add(new Point3D(0.5f, 1.5f, 0.0f));
        test3.add(new Point3D(0.0f, 1.5f, 0.0f));
        test4.add(new Point3D(0.5f, 1.0f, 0.5f));
        test4.add(new Point3D(0.5f, 1.5f, 0.0f));
        test4.add(new Point3D(1.0f, 1.0f, 0.5f));


        test2.add(test3);
        test2.add(test4);
        //test2.add(test5);

        test.add(test2);
        pyramid6 = PPPP(data);

        //float colorsurface4 = ((float) (Math.random()*1));


        /*
        for (int i = 0; i<pyramid4.length; i++) {
            pyramid4[i] = new Pyramid(vertices3[i],colorsurface4,colorsurface2,colorsurface3, 1);   // (NEW)
        }
        */


        //cube = new Cube();         // (NEW)
    }



    // Call back when the surface is first created or re-created.
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // NO CHANGE - SKIP
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set color's clear-value to black
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);
    }

    // Call back after onSurfaceCreated() or whenever the window's size changes.
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // NO CHANGE - SKIP
        if (height == 0) height = 1;   // To prevent divide by zero
        float aspect = (float)width / height;

        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix
        // Use perspective projection
        GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();                 // Reset

        // You OpenGL|ES display re-sizing code here
        // ......
    }

    // Call back to draw the current frame.
    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear color and depth buffers
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // ----- Render the Pyramid -----
        gl.glLoadIdentity();                 // Reset the model-view matrix
        gl.glTranslatef(0.0f, 0.0f, z); // Translate left and into the screen
        gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f); // Rotate (NEW)
        gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f); // Rotate (NEW)

        /*
        for (int i = 0; i < pyramid.length; i++){
            pyramid[i].draw(gl);
            //On affiche toutes les surfaces
        }
        */
        /*
        for (int i = 0; i < pyramid4.length; i++){
            pyramid4[i].draw(gl);
            //On affiche toutes les surfaces
        }
        */
        for (int i = 0; i < pyramid6.length; i++){
            for (int j = 0; j < pyramid6[i].length; j++) {

                pyramid6[i][j].draw(gl);
            }
            //On affiche toutes les surfaces
        }


        /*
        pyramid2.draw(gl);
        pyramid3.draw(gl);
        */                        // Draw the pyramid (NEW)
        //pyramid2.draw(gl);
        // ----- Render the Color Cube -----
                 // Draw the cube (NEW)

        // Update the rotational angle after each refresh (NEW)
        anglePyramid += speedPyramid;   // (NEW)
        angleCube += speedCube;         // (NEW)
        angleX += speedX;  // (NEW)
        angleY += speedY;  // (NEW)
        //z += z ;
    }

    public static float getZ() {
        return z;
    }

    public static void setZ(float Z) {
        z = Z;
    }
}