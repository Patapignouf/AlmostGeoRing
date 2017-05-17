package com.projet.pidr;



import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Patapignouf on 17/05/2017.
 */

public class data3D implements Serializable {
    private ArrayList<ArrayList<ArrayList<Point3D>>> data = new ArrayList<ArrayList<ArrayList<Point3D>>>();


    public ArrayList<ArrayList<ArrayList<Point3D>>> getData() {
        return data;
    }

    public void setData(ArrayList<ArrayList<ArrayList<Point3D>>> data) {
        this.data = data;
    }

    public data3D(ArrayList<ArrayList<ArrayList<Point3D>>> data){
        this.data=data;

    }

}
