package com.projet.pidr;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GeoMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Point> points=null;
    private HashMap<Marker, Point> markerToPoint= new HashMap<Marker, Point>();
    private TextView projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get path and folders
        points=MainPrise.pointbdd.getPointsByProject(AllClass.project);

        //ArrayList<Integer> listIDPoints = MainPrise.pointbdd.getIdWithProject(AllClass.project);
        for(Point p: points){
            Log.d("Point "+p.getId(),p.getTitle());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.info_window, null);
                TextView title = (TextView) v.findViewById(R.id.point_title);
                TextView latitude = (TextView) v.findViewById(R.id.point_lat);
                TextView longitude = (TextView) v.findViewById(R.id.point_long);
                TextView azimuth = (TextView) v.findViewById(R.id.point_azimuth);
                TextView pendage = (TextView) v.findViewById(R.id.point_pendage);
                title.setText(markerToPoint.get(marker).getTitle());
                latitude.setText("Latitude : " + marker.getPosition().latitude);
                longitude.setText("Longitude : " + marker.getPosition().longitude);
                azimuth.setText("Azimuth : " + Double.toString(markerToPoint.get(marker).getAzimuth()));
                pendage.setText("Pendage : " + Double.toString(markerToPoint.get(marker).getPendage()));
                return v;
            }
        });

        LatLng pointAdd=null;
        // Add all markers
        for(Point p : points){
            if(p.getLat()!=0.0 && p.getLong()!=0.0) {
                pointAdd = new LatLng(p.getLat(), p.getLong());
                Marker m = mMap.addMarker(new MarkerOptions()
                        .position(pointAdd)
                        .title(p.getTitle() + "\n Lon : " + Double.toString(p.getLong()) +
                                "\n Latitude : " + Double.toString(p.getLat()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                markerToPoint.put(m, p);
            }
        }
        //Focus on the last point added
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pointAdd));
    }
}
