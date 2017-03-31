package com.projet.pidr;

import android.content.Context;
import android.hardware.Sensor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Mesure implements OnClickListener{

	  Float azimut;  // View to draw a compass
	  Float roll;
	  Float pitch;
	  double meanA;
	  double meanR;
	  Sensor accelerometer;
	  Sensor magnetometer;
		Context c;
		View v;
	  
		public Mesure(Context c, View v) {
			this.c=c;
			this.v=v;
	        this.v.findViewById(R.id.mesu).setOnClickListener(this); 
		}
		
		private void reinitialisationEcran() {
			Point pointFromBDD = MainPrise.pointbdd.getPointWithTitre(AllClass.prise,AllClass.project);
			meanA=(double)((int)(meanA*10000))/10000;
			meanR=(double)((int)(meanR*10000))/10000;
			pointFromBDD.setAzimuth(meanA);
			pointFromBDD.setPendage(meanR);
			MainPrise.pointbdd.updatePoint(pointFromBDD.getId(),pointFromBDD);
			((TextView)this.v.findViewById(R.id.mesAz)).setText("Azimuth : "+meanA);
			((TextView)this.v.findViewById(R.id.mesRoll)).setText("Pendage : "+meanR);
		}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (MainPrise.nbr>0) {
			this.meanA=MainPrise.meanA;
			this.meanR=MainPrise.meanR;
			reinitialisationEcran();
		}
	}

}
