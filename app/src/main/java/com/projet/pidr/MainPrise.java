package com.projet.pidr;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;

import static android.R.attr.name;

public class MainPrise extends AppCompatActivity implements SensorEventListener,OnClickListener,OnItemClickListener, android.content.DialogInterface.OnClickListener {
	TextView readingAzimuth, readingRoll;
	Float azimut;  // View to draw a compass
	Float roll;
	Float pitch;
	static Float meanA = 0f;
	static Float meanR = 0f;
	private SensorManager mSensorManager;
	Sensor accelerometer;
	Sensor magnetometer;
	ArrayList<Float> meanAzimuth = new ArrayList<Float>();
	ArrayList<Float> meanRoll = new ArrayList<Float>();
	ArrayList<String> planetList = new ArrayList<String>();
	private AlertDialog.Builder alert;
	String date;


	private float[] mGravity;
	private float[] mGeomagnetic;

	View view;
	Button add;
	Button delete;
	Button Bvariogram;
	Button TDButton;
	Button buttonback3;
	ImageButton buttondate;
	OurAdapter listAdapter;
	OurAdapter listAdapter2;
	ListView list;
	ListView list2;
	public static String ProjetName;
	public int position=0;
	public static PointBDD pointbdd = new PointBDD();
    static Point point = new Point();
    private int yes=0;
    static int nbr=0;
	private AlertDialog.Builder confirm;



	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_ROGRESS);
		setContentView(R.layout.activity_main);
		AllClass.project=getIntent().getExtras().getString("Folder");
		MainPrise.pointbdd = new PointBDD(this);
		MainPrise.pointbdd.open();

		//Log.d("-------------------",TDButton.toString());

		TDButton = (Button) findViewById(R.id.TDButton);
        Log.d("-------------------",TDButton.toString());

		TDButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int nbp = pointbdd.getPointsByProject(AllClass.project).size();
				ArrayList<Point3D> listePointBDD = new ArrayList<Point3D>();



				//La on récupère les points du projet
				for (int i = 0; i<nbr; i++){
					Log.d("BDD","long :"+(float) pointbdd.getPointsByProject(AllClass.project).get(i).getLongi());
					Log.d("BDD","lat :"+(float) pointbdd.getPointsByProject(AllClass.project).get(i).getLat());
					Log.d("BDD","alt :"+(float) pointbdd.getPointsByProject(AllClass.project).get(i).getAltitude());

					listePointBDD.add(new Point3D( (float) pointbdd.getPointsByProject(AllClass.project).get(i).getLongi(),(float) pointbdd.getPointsByProject(AllClass.project).get(i).getLat(), (float)pointbdd.getPointsByProject(AllClass.project).get(i).getAltitude()));

				}


				ArrayList<ArrayList<ArrayList<Point3D>>> data2 = new ArrayList<ArrayList<ArrayList<Point3D>>>();
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

				data2.add(test2);
				OpenGLES20Activity.setData(data2);
				//data3D datadata = new data3D(data);
				Intent intent = new Intent(MainPrise.this, OpenGLES20Activity.class);
				//intent.putExtra("data", datadata);
				//Log.d("debug1","J'ai réussi à créer le colis");
				startActivity(intent);
			}
		});

		Bvariogram = (Button) findViewById(R.id.Bvariogram);
		Log.d("-------------------",TDButton.toString());

		Bvariogram.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainPrise.this, Variogram.class);
				startActivity(intent);
			}
		});


		buttonback3 = (Button) findViewById(R.id.buttonback3);
		buttonback3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainPrise.this, FolderComponents.class);
				startActivity(intent);
			}
		});






		nbr=pointbdd.getPointsByProject(AllClass.project).size();
		Log.d("debugBDD",String.valueOf(nbr));
		Log.d("debugBDD",String.valueOf(pointbdd.getPointsByProject(AllClass.project).get(0).getAltitude()));
		this.view = getWindow().getDecorView().findViewById(android.R.id.content);
		getSupportActionBar().setTitle("Projet: "+getIntent().getExtras().getString("Folder"));

		ProjetName=getIntent().getExtras().getString("Folder");
		Log.d("TEST", ("Projet: "+getIntent().getExtras().getString("Folder")));
        new Gps(this, this.view);
        new Mesure(this, this.view);
        /*new Faille(this, this.view);*/

        //On tient la sans doutes l'explication de pourquoi les failles ne fonctionnent pas ^^

        list = (ListView) findViewById(R.id.listView2);
        String[] planets = new String[] {};
		listAdapter = new OurAdapter(this, R.layout.listview, planetList,this.view);
		list.setAdapter( listAdapter );
		list.setOnItemClickListener(this);
        planetList.addAll( Arrays.asList(planets) );

        Load_Folder();
        add=(Button) this.findViewById(R.id.add);
        delete=(Button) this.findViewById(R.id.delete);
		buttondate = (ImageButton) findViewById(R.id.date);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
		buttondate.setOnClickListener(this);

        readingRoll = (TextView)findViewById(R.id.roll);
        readingAzimuth = (TextView)findViewById(R.id.azimuth);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);




        new Event(this.view,this);
	}

	public static String getProjetName() {
		return ProjetName;
	}

	public static void setProjetName(String projetName) {
		ProjetName = projetName;
	}

	  protected void onResume() {
	    super.onResume();
	    mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
	    mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
	  }

	  protected void onPause() {
	    super.onPause();
	    mSensorManager.unregisterListener(this);
	  }

	public void Load_Folder() {
		Log.d("chemin", Environment.getExternalStorageDirectory().getAbsolutePath().toString() + AllClass.path);
    	File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString() + AllClass.path);
    	// On r�cup�re la liste des fichiers dans le r�pertoire actuel
    	File[] fichiers = folder.listFiles();
    	//On prend le nombre de fichiers pr�sents
    	int filesLength=fichiers.length;
    	this.position=filesLength-1;
    	// On transforme le tableau en une structure de donn�es de taille variable
    	if (filesLength > 0) {
    		int i =0;
    		for(File f : fichiers) {
    		listAdapter.add(f.getName());
    		i++;
    		if (i==filesLength) {
    			AllClass.prise=f.getName();
    		}
    	  }
    	  listAdapter.notifyDataSetChanged();
    	}
    	//pas besoin d'avoir une prise 0
    	/*else if (this.position==-1)
    	{
    		this.position=0;
    		AllClass.CreateAFolder(AllClass.path, "Prise0");
    		listAdapter.add("Prise0");
			AllClass.CreateAllFolders(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+AllClass.path+"/"+AllClass.Prise);
    		AllClass.CreatePriseFolder(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+AllClass.path+"/"+AllClass.Prise);
    		listAdapter.notifyDataSetChanged();
    	}*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		AllClass.path="/Pidr";
		if (MainPrise.pointbdd != null) {
			MainPrise.pointbdd.close();
		}
	}

	public AlertDialog.Builder erreur;

	public void afficheErreur() {
		erreur = new AlertDialog.Builder(this);
		erreur.setTitle("Nom de point d'observation utilisé");
		erreur.setNegativeButton("Ok", this);
		erreur.show();
	}
	
	// ca context'est ok
	private AlertDialog.Builder prise;
	String nom;
	private EditText input;

	public void priseName() {
		prise = new AlertDialog.Builder(this);
	    prise.setTitle("Entrez le nom du point d'observation");
	    // prise.setMessage("Nom");
		// Set an EditText view to get user input
	    input = new EditText(this);
	    prise.setView(input);
	    MainPrise.point=new Point();
	    prise.setNegativeButton("Annuler", this);
	    prise.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				nom = input.getText().toString();
				if (MainPrise.pointbdd.checkNameNewPoint(nom, AllClass.project)) { // test sur le nom
					position++;
					nbr++;
					// listAdapter.addName(nom);
					listAdapter.setSelectedPosition(position);
					listAdapter.add(nom);
					AllClass.CreateAFolder(AllClass.path, nom);
					AllClass.prise = nom;
					AllClass.CreateAllFolders(Environment.getExternalStorageDirectory().getAbsolutePath().toString() + AllClass.path + "/" + AllClass.prise);
					Log.d("path", Environment.getExternalStorageDirectory().getAbsolutePath().toString() + AllClass.path + "/" + AllClass.prise);
					AllClass.CreatePriseFolder(Environment.getExternalStorageDirectory().getAbsolutePath().toString() + AllClass.path + "/" + AllClass.prise);
					listAdapter.notifyDataSetChanged();
					// Auto-generated method stub
					point.setTitle(AllClass.prise);
					point.setAzimuth(0);
					point.setLat(0);
					point.setLongi(0);
					point.setAltitude(0);
					point.setPendage(0);
					point.setType_point("Sans_type");
					point.setProject(AllClass.project);
					MainPrise.pointbdd.insertPoint(point);
					MainPrise.point = MainPrise.pointbdd.getPointWithTitre(AllClass.prise, AllClass.project);
					updateTextView();
				} else {
					afficheErreur();
					;
				}
			}
		});
	      prise.show();
	}



	public void debug(){

	}

	public void confirmDelete() {
		confirm = new AlertDialog.Builder(this);
		confirm.setTitle("Voulez-vous supprimer ce point d'observation ?");

		confirm.setNegativeButton("Non",this);
		confirm.setPositiveButton("Oui", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				nbr--;
				Point pointFromBDD = MainPrise.pointbdd.getPointWithTitre(AllClass.prise,AllClass.project);
				MainPrise.pointbdd.removePointWithID(pointFromBDD.getId());
				AllClass.DeletePriseFolder(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+AllClass.path + "/" + AllClass.prise);
				position --;
				listAdapter.remove(AllClass.prise) ;
			}
		});
		confirm.show();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.add) {
			this.priseName();
		} else if (id == R.id.delete) {
			this.confirmDelete();
		} else if (id == (R.id.date)) {

			Log.d("infodebug","On a cliqué sur le bouton date !");
			alert = new AlertDialog.Builder(this);
			alert.setTitle("Veuillez entrez le type de roche");
			alert.setMessage("Type");
			// Set an EditText view to get user input
			input = new EditText(this);
			alert.setView(input);
			alert.setNegativeButton("Annuler", this);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					date = input.getText().toString();
					Point pointFromBDD = MainPrise.pointbdd.getPointWithTitre(AllClass.prise,AllClass.project);
					pointFromBDD.setType_point(date);
					Log.d("BDD","type point courrant :"+pointFromBDD.getType_point());


					//Log.d("infodebug","On a cliqué sur le bouton date 2 !");
				}
			});
			alert.show();
		}



	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	    String name =listAdapter.getItem(position).toString();
	    AllClass.prise=name;
	    listAdapter.setSelectedPosition(position);
		MainPrise.point=MainPrise.pointbdd.getPointWithTitre(AllClass.prise,AllClass.project);
		updateTextView();

	}


	@Override
	public void onClick(DialogInterface dialog, int which) {

	}

	private  void updateTextView() {
		((TextView)this.view.findViewById(R.id.latitude)).setText(String.valueOf("Latitude : "+MainPrise.point.getLat()));
		((TextView)this.view.findViewById(R.id.longitude)).setText(String.valueOf("Longitude : "+MainPrise.point.getLong()));
		((TextView)this.view.findViewById(R.id.altitude)).setText(String.valueOf("Altitude : "+MainPrise.point.getAltitude()));
		((TextView)this.view.findViewById(R.id.mesAz)).setText(String.valueOf("Azimuth : "+MainPrise.point.getAzimuth()));
		((TextView)this.view.findViewById(R.id.mesRoll)).setText(String.valueOf("Pendage : "+MainPrise.point.getPendage()));
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {  }

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			  mGravity = event.values;

		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			  mGeomagnetic = event.values;

		if (mGravity != null && mGeomagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);

			 if (success) {
				 float orientation[] = new float[3];
				 SensorManager.getOrientation(R, orientation);
				 azimut = orientation[0]; // orientation contains: azimut, pitch and roll
				 pitch = orientation[1];
				 roll = orientation[2];
				 Float a = (azimut*360/(2*3.14159f));
				 Float b = (pitch*360/(2*3.14159f));
				 Float c = (roll*360/(2*3.14159f));
				 //On tronque les deux derniers chiffres pour ne pas en afficher de trop
				 String displayedAzimuth = a.toString().substring(0,a.toString().length());
				 String displayedRoll = c.toString().substring(0,c.toString().length());
				 readingAzimuth.setText("Azimuth : "+displayedAzimuth);
				 readingRoll.setText("Pendage : " + displayedRoll);
				 meanAzimuth.add(a);
				 meanRoll.add(c);
				 if(meanAzimuth.size()>50) {
					 for(int i = 0;i<meanAzimuth.size();i++){
						 meanA+=meanAzimuth.get(i);
					 }
					 meanA=meanA/Float.parseFloat(meanAzimuth.size()+"");
					 meanAzimuth = new ArrayList<Float>();
				 }
				 if(meanRoll.size()>50) {
					 for(int i = 0;i<meanRoll.size();i++){
						 meanR+=meanRoll.get(i);
					 }
					 meanR=meanR/Float.parseFloat(meanRoll.size()+"");
					 meanRoll = new ArrayList<Float>();
				 }
			  }
		  }
	  }

}



