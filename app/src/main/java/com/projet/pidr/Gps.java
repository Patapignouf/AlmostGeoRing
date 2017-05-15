package com.projet.pidr;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Gps implements OnClickListener,LocationListener  {
	Location location;
	private LocationManager lManager;
	private String choix_source="";
	Context context;
	View v;
    double altitude2 = 0.0;
    private LocationProvider _locationProvider;
    private LocationManager _locationManager;

    /*

	_locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
	_locationProvider = _locationManager.getProvider(LocationManager.GPS_PROVIDER);
	location = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
     _locationProvider.supportsAltitude();
	hasaltitude = location.hasAltitude();
	double altitude = location.getAltitude();
	System.out.println("HasAltitude" + hasaltitude+"-"+altitude);
	*/

	public Gps(Context context, View v) {
		this.context = context;
		this.v=v;
        lManager = (LocationManager)this.context.getSystemService(this.context.LOCATION_SERVICE);
        this.v.findViewById(R.id.gps).setOnClickListener(this); 
	}
	
	private void obtenirPosition() {
		//on démarre le cercle de chargement
		//setProgressBarIndeterminateVisibility(true);
 
		//On demande au service de localisation de nous notifier tout changement de position
		//sur la source (le provider) choisie, toute les minutes (60000millisecondes).
		//Le paramètre this spécifie que notre classe implémente LocationListener et recevra
		//les notifications.
		lManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER , 0, 0, this);
	}
	
	//Réinitialisation de l'écran
	private void reinitialisationEcran(){
		((TextView)this.v.findViewById(R.id.latitude)).setText("Latitude : 0.0");
		((TextView)this.v.findViewById(R.id.longitude)).setText("Longitude : 0.0");
		((TextView)this.v.findViewById(R.id.altitude)).setText("Altitude : 0.0");
	}
	
	private void choisirSource() {
		reinitialisationEcran();
 
		//On demande au service la liste des sources disponibles.
		List <String> providers = lManager.getProviders(true);
		final String[] sources = new String[providers.size()];
		int i =0;
		//on stock le nom de ces source dans un tableau de string
		for(String provider : providers)
			sources[i++] = provider;
 
		//On affiche la liste des sources dans une fenêtre de dialog
		//Pour plus d'infos sur AlertDialog, vous pouvez suivre le guide
		//http://developer.android.com/guide/topics/ui/dialogs.html
		new AlertDialog.Builder(this.context)
		.setItems(sources, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//on stock le choix de la source choisi
				choix_source = sources[which];
				//on ajoute dans la barre de titre de l'application le nom de la source utilisé
			}
		})
		.create().show();
	}
	



	private void afficherLocation() {
		//On affiche les informations de la position a l'écran
		Point pointFromBDD = MainPrise.pointbdd.getPointWithTitre(AllClass.prise,AllClass.project);
        location = lManager.getLastKnownLocation(lManager.GPS_PROVIDER);

		pointFromBDD.setLat(location.getLatitude());
		pointFromBDD.setLongi(location.getLongitude());
		pointFromBDD.setAltitude(location.getAltitude());

        MainPrise.pointbdd.updatePoint(pointFromBDD.getId(),pointFromBDD);
		((TextView)this.v.findViewById(R.id.latitude)).setText(String.valueOf("Latitude : "+location.getLatitude()));
		((TextView)this.v.findViewById(R.id.longitude)).setText(String.valueOf("Longitude : "+location.getLongitude()));
		((TextView)this.v.findViewById(R.id.altitude)).setText(String.valueOf("Altitude : "+location.getAltitude()));
		Log.d((String.valueOf(location.hasAltitude())),"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	}
 
	@Override
	public void onLocationChanged(Location location) {
		//Lorsque la position change...
		//... on stop le cercle de chargement		//... on active le bouton pour afficher l'adresse		//... on sauvegarde la position
		this.location = location;
		//... on l'affiche
		afficherLocation();
		//... et on spécifie au service que l'on ne souhaite plus avoir de mise à jour
		lManager.removeUpdates(this);
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		//Lorsque la source (GSP ou réseau GSM) est désactivé
		//...on affiche un Toast pour le signaler à l'utilisateur
		Toast.makeText(this.context,
				String.format("La source \"%s\" a été désactivé", provider),
				Toast.LENGTH_SHORT).show();
		//... et on spécifie au service que l'on ne souhaite plus avoir de mise à jour
		lManager.removeUpdates(this);
		//... on stop le cercle de chargement
		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.gps) {
			if (MainPrise.nbr>0)
			{
				choisirSource();
				lManager = (LocationManager)this.context.getSystemService(this.context.LOCATION_SERVICE);
				obtenirPosition();
			}
		} else {
		}
	}
	
}
