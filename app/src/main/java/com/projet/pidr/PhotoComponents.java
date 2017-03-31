package com.projet.pidr;

import java.io.File;
//import com.example.projet_pidr.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class PhotoComponents extends ListComponents implements DialogInterface.OnClickListener{

	private AlertDialog.Builder alert;
	private EditText input;
	private String name;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.mode=1;
    	super.path = AllClass.lastPath;
    	Log.d("path",super.path);
        super.onCreate(savedInstanceState);
        super.button1.setText("New photo");
        super.titleList.setText("Photographies");
        super.button1.setOnClickListener(this);
        super.setTitle("Photo");
    }
    
	@Override
	public void onClick(View v) {
        alert = new AlertDialog.Builder(this);
        alert.setTitle("what's the name of the photo ?");
        alert.setMessage("Name");

        // Set an EditText view to get user input
        input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Ok",this);

        Log.d("test","oui on entre");
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
        name = input.getText().toString();
		// TODO Auto-generated method stub
		Uri uriSavedImage=Uri.fromFile(new File(super.path+"/"+name+".jpeg"));	 
		// On lance l'activit� Camera
		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra("return-data", true);
		i.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage); 
		// Enfin, on lance l'intent pour que l'application de photo se lance
		startActivityForResult(i, 0);
	}
}
