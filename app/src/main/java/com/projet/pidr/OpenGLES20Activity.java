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

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class OpenGLES20Activity extends Activity {

    Button buttonback2;
    private GLSurfaceView mGLView;
    static ArrayList<ArrayList<ArrayList<Point3D>>> data = new ArrayList<ArrayList<ArrayList<Point3D>>>();

    public static ArrayList<ArrayList<ArrayList<Point3D>>> getData() {
        return data;
    }

    public static void setData(ArrayList<ArrayList<ArrayList<Point3D>>> data) {
        OpenGLES20Activity.data = data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threedview);

        /*
        Intent intent = getIntent();
        Log.d("debug1","Je suis prêt à recevoir le colis");
        data3D data = (data3D)intent.getSerializableExtra("data");
        Log.d("debug1","J'ai réussi à récupérer le colis");
        */
        /*
        ArrayList<ArrayList<ArrayList<Point3D>>> data = new ArrayList<ArrayList<ArrayList<Point3D>>>();
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

        data.add(test2);
        */



        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new MyGLSurfaceView(this, data);
        //Il faut qu'on récupère ici la MegaListe de points



        //setContentView(mGLView);

        FrameLayout frame = (FrameLayout)findViewById(R.id.visualizer);
        Button buttonback2 = (Button) findViewById(R.id.buttonback2);
        Button buttonZoomIn = (Button) findViewById(R.id.buttonZI);
        Button buttonZoomOut = (Button) findViewById(R.id.buttonZO);
        frame.addView(mGLView, 0);

        //MyGLRenderer.setZ(4);


        buttonback2.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                Intent intent = new Intent(OpenGLES20Activity.this, MainPrise.class);
                intent.putExtra("Folder" , MainPrise.getProjetName());
                startActivity(intent);
            }
        });

        buttonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                MyGLRenderer.setZ(MyGLRenderer.getZ()+1);
            }
        });

        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                MyGLRenderer.setZ(MyGLRenderer.getZ()-1);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
}