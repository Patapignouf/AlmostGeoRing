package com.projet.pidr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class NoteComponents extends ListComponents implements DialogInterface.OnClickListener{

	private AlertDialog.Builder alert;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.mode=1;
    	super.path = AllClass.lastPath;
    	Log.d("path",super.path);
        super.onCreate(savedInstanceState);
        super.button1.setText("Nouvelle note");
		super.titleList.setText("Notes");
        super.button1.setOnClickListener(this);
        super.setTitle("Note");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		alert = new AlertDialog.Builder(this);
	    alert.setTitle("Entrez le nom de la note");
	    final EditText inputName = new EditText(this);
	    final EditText inputText = new EditText(this);
	    //alert.setView(input);
	    inputName.setHint("Nom");
	    inputText.setHint("Texte");
	    LinearLayout lay = new LinearLayout(this);
	    lay.setOrientation(LinearLayout.VERTICAL);
	    lay.addView(inputName);
	    lay.addView(inputText);
	    alert.setView(lay);
	    
	    alert.setNegativeButton("Annuler",this);
	    Log.d("test","oui on entre");
	    alert.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() 
	    {
	       public void onClick(DialogInterface dialog, int whichButton) 
	       {
	    	   String name = inputName.getText().toString();
	    	   String fileName = name +".txt"; 
	    	   String text = inputText.getText().toString();
	    	   File mFile = new File(AllClass.lastPath.toString()+"/"+fileName);
		   		try {
		   	          // Flux interne
		   			
		   	        FileOutputStream output = openFileOutput(fileName, MODE_PRIVATE);
		   	        output.write(text.getBytes());
					if(output != null)
						output.close();
		   	          
		   	          // Si le fichier est lisible et qu'on peut �crire dedans
					if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
		   	              && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
		   	            // On cr�e un nouveau fichier. Si le fichier existe d�j�, il ne sera pas cr��
		   	            mFile.createNewFile();
		   	            output = new FileOutputStream(mFile);
		   	            output.write(text.getBytes());
						if(output != null)
							output.close();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		   		Intent intent =getIntent();
		   		finish();
		   		startActivity(intent);
		       	}
	       		
		    });
			alert.show();
	   }
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
	}

}
