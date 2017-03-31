package com.projet.pidr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
//import com.example.projet_pidr.R;

public class RecordingComponents extends ListComponents {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.mode=1;
    	super.path = AllClass.lastPath;
    	super.mode=1;
        super.onCreate(savedInstanceState);
        super.button1.setText("New recording");
        super.button1.setOnClickListener(this);
		super.titleList.setText("Enregistrements");
        super.setTitle("recording");
    }

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(RecordingComponents.this, RecordAudioActivity.class);
		startActivity(intent);
	}
}
