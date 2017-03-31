package com.projet.pidr;

import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OurAdapter extends ArrayAdapter{
	//attribut permettant de savoir si l'item est selectionne
	private int selectedPos = -1;
	
	View view;
	//Classe qui permet de griser la prise selectionnee, en plus des fonction de ArrayAdapter
	public OurAdapter(Context context, int resource, List objects,View v) {
		super(context, resource, objects);
		this.view=v;
		// TODO Auto-generated constructor stub
	}
	
	//met a jour la position de l'item selectionne
	public void setSelectedPosition (int pos){
		selectedPos = pos;
		notifyDataSetChanged();
	}
	
	public int getSelectedPosition(){
		return selectedPos;
	}
	
	//re-definition de getView, met en gras l'item selectionne
	@Override
	 public View getView(int position, View convertView, ViewGroup parent) {
	      if (convertView == null) {
	           convertView = LayoutInflater.from(parent.getContext()).inflate(
	                        R.layout.listview, null);
	       }

	      TextView title = (TextView) convertView.findViewById(R.id.rowTextView);
	       
	      title.setText(this.getItem(position).toString());

	      if (selectedPos == position) {
	    	  title.setTextColor(Color.BLACK);
	    	  title.setTypeface(null,Typeface.BOLD);
	      } else {
	    	  title.setTextColor(Color.parseColor("#000000"));
	    	  title.setTypeface(null,Typeface.NORMAL);
	      }
	      return convertView;
	    }
}
