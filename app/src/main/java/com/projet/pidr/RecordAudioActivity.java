package com.projet.pidr;


import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RecordAudioActivity extends Activity implements OnClickListener, DialogInterface.OnClickListener
  {

   private MediaRecorder myAudioRecorder;
   private String outputFile = "";
   private Button start,stop,play;
   private Button save,again,cancel;
   private AlertDialog.Builder alert;
   private String name;
   private EditText input;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.record);
      start = (Button)findViewById(R.id.start);
      stop = (Button)findViewById(R.id.stop);
      play = (Button)findViewById(R.id.play);
      save = (Button)findViewById(R.id.newComponents);
      again = (Button)findViewById(R.id.again);
      cancel =(Button)findViewById(R.id.cancel);
      
      
      
      alert = new AlertDialog.Builder(this);
      alert.setTitle("what's the name of your recording ?");
      alert.setMessage("Name");

      // Set an EditText view to get user input 
      input = new EditText(this);
      alert.setView(input);
      alert.setPositiveButton("Ok",this);


      alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
      {
        public void onClick(DialogInterface dialog, int whichButton) 
        {
			Intent intent2 = new Intent(RecordAudioActivity.this, RecordingComponents.class);
			startActivity(intent2);
			finish();
        }
      });
      
      
      
      save.setOnClickListener(this);
      again.setOnClickListener(this);
      cancel.setOnClickListener(this);
      
      stop.setEnabled(false);
      play.setEnabled(false);
      save.setEnabled(false);
      again.setEnabled(false);
      
      alert.show();

   }

   public void start(View view){
      try {
         myAudioRecorder.prepare();
         myAudioRecorder.start();
      } catch (IllegalStateException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      start.setEnabled(false);
      save.setEnabled(true);
      again.setEnabled(true);
      stop.setEnabled(true);
      Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();

   }

   public void stop(View view){
      myAudioRecorder.stop();
      myAudioRecorder.release();
      myAudioRecorder  = null;
      stop.setEnabled(false);
      play.setEnabled(true);
      Toast.makeText(getApplicationContext(), "Audio recorded successfully",
      Toast.LENGTH_LONG).show();
   }
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
   public void play(View view) throws IllegalArgumentException,   
   SecurityException, IllegalStateException, IOException{
   
   MediaPlayer m = new MediaPlayer();
   m.setDataSource(outputFile);
   m.prepare();
   m.start();
   Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
   }

@Override
public void onClick(View v) {
	int id = v.getId();
	if (id == R.id.newComponents) {
		Intent intent = new Intent(RecordAudioActivity.this, RecordingComponents.class);
		startActivity(intent);
		finish();
	} else if (id == R.id.again) {
		outputFile = AllClass.lastPath+"/"+name+".mp3";
		Log.d("record", outputFile);
		start.setEnabled(true);
		stop.setEnabled(false);
		play.setEnabled(false);
		save.setEnabled(false);
		again.setEnabled(false);
		myAudioRecorder = new MediaRecorder();
		myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		myAudioRecorder.setOutputFile(outputFile);
	} else if (id == R.id.cancel) {
		File file = new File(outputFile);
		if(file.exists())
           {

               boolean result = file.delete();
              // file.delete();
           }
		Intent intent2 = new Intent(RecordAudioActivity.this, RecordingComponents.class);
		startActivity(intent2);
		finish();
	}
	
}

@Override
public void onClick(DialogInterface dialog, int which) 
{
    name = input.getText().toString();
    outputFile = AllClass.lastPath+"/"+name+".mp3";
    Log.d("record", outputFile);
    myAudioRecorder = new MediaRecorder();
    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
    myAudioRecorder.setOutputFile(outputFile);
}

}
