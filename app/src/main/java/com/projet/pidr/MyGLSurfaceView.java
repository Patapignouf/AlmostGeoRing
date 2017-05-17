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
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;

/*
 * Custom GL view by extending GLSurfaceView so as
 * to override event handlers such as onKeyUp(), onTouchEvent()
 */
public class MyGLSurfaceView extends GLSurfaceView {
    MyGLRenderer renderer;    // Custom GL Renderer

    // For touch event
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320.0f;
    private float previousX;
    private float previousY;

    //On va récupérer ici les points de la BDD et si possible le rayon ^^

    ArrayList<ArrayList<ArrayList<Point3D>>> test = new ArrayList<ArrayList<ArrayList<Point3D>>>();
    ArrayList<ArrayList<Point3D>> listePoints ;
    float rayon;



    // Constructor - Allocate and set the renderer
    public MyGLSurfaceView(Context context, ArrayList<ArrayList<ArrayList<Point3D>>> data) {
        //On récupère ici les données de la BDD

        super(context);
        renderer = new MyGLRenderer(context,data);
        //renderer = new MyGLRenderer(context,listePoints);
        this.setRenderer(renderer);
        // Request focus, otherwise key/button won't react
        this.requestFocus();
        this.setFocusableInTouchMode(true);
    }

    // Handler for key event
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent evt) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:   // Decrease Y-rotational speed
                renderer.speedY -= 0.1f;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:  // Increase Y-rotational speed
                renderer.speedY += 0.1f;
                break;
            case KeyEvent.KEYCODE_DPAD_UP:     // Decrease X-rotational speed
                renderer.speedX -= 0.1f;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:   // Increase X-rotational speed
                renderer.speedX += 0.1f;
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:           // Zoom out (decrease z)
                renderer.z -= 0.2f;
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:           // Zoom in (increase z)
                renderer.z += 0.2f;
                break;
        }
        return true;  // Event handled
    }

    // Handler for touch event
    @Override
    public boolean onTouchEvent(final MotionEvent evt) {
        float currentX = evt.getX();
        float currentY = evt.getY();
        float deltaX, deltaY;
        switch (evt.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // Modify rotational angles according to movement
                deltaX = currentX - previousX;
                deltaY = currentY - previousY;
                renderer.angleX += deltaY * TOUCH_SCALE_FACTOR;
                renderer.angleY += deltaX * TOUCH_SCALE_FACTOR;
        }
        // Save current x, y
        previousX = currentX;
        previousY = currentY;
        return true;  // Event handled
    }
}