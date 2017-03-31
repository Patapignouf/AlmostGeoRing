package com.projet.pidr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Event implements OnClickListener,OnCreateContextMenuListener {
	ImageButton buttonPhoto;
	ImageButton buttonRecording;
	ImageButton buttonNote;
	ImageButton buttonMap;

	ImageButton choixGps;
	ImageButton choixMesuresdor;
	private AlertDialog.Builder alert;
	private EditText input;

	/*ImageButton choix3;*/
	View view;
	Context context;
	private String choix="";

	public Event(View v,Context c) {
		this.view=v;
		this.context=c;
		this.gestionEvent(this.view);
	}

	private void gestionEvent(View view2) {
		buttonPhoto = (ImageButton) view2.findViewById(R.id.photo);
		//choixGps =(ImageButton)  view2.findViewById(R.id.choixGps);
		//choixMesuresdor =(ImageButton)  view2.findViewById(R.id.choixMesuresdor);
        /*choix3=(ImageButton)  view2.findViewById(R.id.choixFaille);*/
		buttonRecording =(ImageButton) view2.findViewById(R.id.recording);
		buttonNote =(ImageButton) view2.findViewById(R.id.note);
		buttonMap = (ImageButton) view2.findViewById(R.id.map);

		//choixGps.setOnClickListener(this);
		//choixMesuresdor.setOnClickListener(this);
        /*choix3.setOnClickListener(this);*/
		buttonRecording.setOnClickListener(this);
		buttonNote.setOnClickListener(this);
		buttonPhoto.setOnClickListener(this);
		buttonMap.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (MainPrise.nbr > 0) {
			int id = v.getId();

			if (id == R.id.photo) {
				AllClass.lastPath=Environment.getExternalStorageDirectory().getAbsolutePath().toString()+AllClass.path+"/"+AllClass.prise+"/Photo";
				Intent intent = new Intent(this.context, PhotoComponents.class);
				this.context.startActivity(intent);
			}/* else if (id == R.id.choixGps) {
				AllClass.lastPath=Environment.getExternalStorageDirectory().getAbsolutePath().toString()+AllClass.path+"/"+AllClass.prise+"/Gps";
				AllClass.CreateAllFolders(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+AllClass.path+"/"+AllClass.prise+"/Gps");
				choix(R.id.choixGps);
			} else if (id == R.id.choixMesuresdor) {
				AllClass.lastPath=Environment.getExternalStorageDirectory().getAbsolutePath().toString()+AllClass.path+"/"+AllClass.prise+"/Mesures d'orientation";
				AllClass.CreateAllFolders(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+AllClass.path+"/"+AllClass.prise+"/Mesures d'orientation");
				choix(R.id.choixMesuresdor);
			} */else if (id == R.id.recording) {
				AllClass.lastPath=Environment.getExternalStorageDirectory().getAbsolutePath().toString()+AllClass.path+"/"+AllClass.prise+"/Recording";
				Intent intent2 = new Intent(this.context, RecordingComponents.class);
				this.context.startActivity(intent2);
			} else if (id == R.id.note) {
				AllClass.lastPath=Environment.getExternalStorageDirectory().getAbsolutePath().toString()+AllClass.path+"/"+AllClass.prise+"/Note";
				Intent intent3 = new Intent(this.context, NoteComponents.class );
				this.context.startActivity(intent3);
			} else if (id == R.id.add) {
			} else if (id == R.id.delete) {
			} else if (id == R.id.map){
				Intent mapIntent = new Intent(this.context, GeoMapActivity.class);
				this.context.startActivity(mapIntent);
			}
		}
		else {
			CharSequence text = "Il faut sélectionner au moins un point d'observation!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}


	private void choix(final int bouton) {

		//On demande au service la liste des sources disponibles.

		final String[] sources ={"Manual","Photo","Recording","Note"};
		//on stock le nom de ces source dans un tableau de string

		//On affiche la liste des sources dans une fenêtre de dialog
		//Pour plus d'infos sur AlertDialog, vous pouvez suivre le guide
		//http://developer.android.com/guide/topics/ui/dialogs.html
		new AlertDialog.Builder(this.context)
				.setItems(sources, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						choix = sources[which];

						if (choix.equals("Photo")) {
							AllClass.lastPath += "/Photo";
							Intent intent = new Intent(context, PhotoComponents.class);
							context.startActivity(intent);
						} else if (choix.equals("Recording")) {
							AllClass.lastPath += "/Recording";
							Intent intent2 = new Intent(context, RecordingComponents.class);
							context.startActivity(intent2);
						} else if (choix.equals("Note")) {
							AllClass.lastPath += "/Note";
							Intent intent3 = new Intent(context, NoteComponents.class);
							context.startActivity(intent3);
						} /*else if (choix.equals("Manual")) {
							alert = new AlertDialog.Builder(context);
							LinearLayout layout = new LinearLayout(context);
							layout.setOrientation(LinearLayout.VERTICAL);
							final EditText input1 = new EditText(context);
							final EditText input2 = new EditText(context);

							if (bouton == R.id.choixGps) {
								input1.setHint("Latitude");
								layout.addView(input1);
								input2.setHint("Longitude");
								layout.addView(input2);

								final EditText input3 = new EditText(context);
								input3.setHint("Altitude");
								layout.addView(input3);

								alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										String latitude_string = input1.getText().toString();
										String longitude_string = input2.getText().toString();
										String altitude_string = input3.getText().toString();
										double latitude = 0;
										double longitude = 0;
										double altitude = 0;
										Point pointFromBDD = MainPrise.pointbdd.getPointWithTitre(AllClass.prise,AllClass.project);

										if (isNumeric(latitude_string)) {
											latitude=Double.parseDouble(latitude_string);
											pointFromBDD.setLat(latitude);
										}
										if (isNumeric(longitude_string)) {
											longitude=Double.parseDouble(longitude_string);
											pointFromBDD.setLongi(longitude);
										}
										if (isNumeric(altitude_string)) {
											altitude=Double.parseDouble(altitude_string);
											pointFromBDD.setAltitude(altitude);
										}

										MainPrise.pointbdd.updatePoint(pointFromBDD.getId(),pointFromBDD);
										MainPrise.point=MainPrise.pointbdd.getPointWithTitre(AllClass.prise,AllClass.project);
										updateTextView();
									}
								});
							} else if (bouton == R.id.choixMesuresdor) {
								input1.setHint("Azimuth");
								layout.addView(input1);
								input2.setHint("Pendage");
								layout.addView(input2);
								alert.setPositiveButton("Ok",new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog, int whichButton)
									{
										String Azimuth = input1.getText().toString();
										String Pendage = input2.getText().toString();
										double azimuth = 0;
										double pendage = 0;
										Point pointFromBDD = MainPrise.pointbdd.getPointWithTitre(AllClass.prise,AllClass.project);

										if (isNumeric(Azimuth)) {
											azimuth=Double.parseDouble(Azimuth);
											pointFromBDD.setAzimuth(azimuth);
										}
										if (isNumeric(Pendage)) {
											pendage=Double.parseDouble(Pendage);
											pointFromBDD.setPendage(pendage);
										}

										MainPrise.pointbdd.updatePoint(pointFromBDD.getId(),pointFromBDD);
										MainPrise.point=MainPrise.pointbdd.getPointWithTitre(AllClass.prise,AllClass.project);
										updateTextView();
									}
								});
							}
							alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int whichButton)
								{
									dialog.cancel();
								}});
							alert.setView(layout);
							alert.show();
						}*/
					}
				})
				.create().show();
	}

	@Override
	public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		int id = v.getId();
		if (id == R.id.listView2) {
			//super.onCreateContextMenu(menu, v, menuInfo);
			menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Supprimer cet element");
			menu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "Retour");
		}
	}

	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		}
		catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private  void updateTextView() {
		((TextView)this.view.findViewById(R.id.latitude)).setText(String.valueOf("Latitude : "+MainPrise.point.getLat()));
		((TextView)this.view.findViewById(R.id.longitude)).setText(String.valueOf("Longitude : "+MainPrise.point.getLong()));
		((TextView)this.view.findViewById(R.id.altitude)).setText(String.valueOf("Altitude : "+MainPrise.point.getAltitude()));
		((TextView)this.view.findViewById(R.id.mesAz)).setText(String.valueOf("Azimuth : "+MainPrise.point.getAzimuth()));
		((TextView)this.view.findViewById(R.id.mesRoll)).setText(String.valueOf("Pendage : "+MainPrise.point.getPendage()));
	}

}
