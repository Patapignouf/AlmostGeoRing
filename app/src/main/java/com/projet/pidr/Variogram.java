package com.projet.pidr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.R.attr.colorBackgroundFloating;
import static android.R.attr.port;
import static android.media.CamcorderProfile.get;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

/**
 * Created by Paul on 15/05/2017.
 */

public class Variogram extends Activity{

    ArrayList<Integer> V = new ArrayList<Integer>();

    int dist = 0;

    public float mean(ArrayList<Integer> L) {
        float moy = 0;
        for (int i = 0; i < L.size(); i++) {
            moy += L.get(i);
        }
        return moy;
    }

    public float variance(ArrayList<Integer> L) {
        float var = 0;
        float moy = mean(L);
        for (int i = 0; i < L.size(); i++) {
            var += Math.pow(L.get(i) - moy, 2);
        }
        var /= L.size();
        return var;
    }

    public float variogram_one_calcul(ArrayList<Integer> V, int h, int dist) {
        int N = V.size() - (h / dist - 1);
        int step = h / dist;
        float gamma = 0;
        for (int i = 1; i < N - 1; i++) {
            gamma += 1. / (2. * N) * Math.pow(V.get(i) - V.get(i - 1), 2);
        }
        return gamma;
    }

    public ArrayList<Float> variogram_total_calcul(ArrayList<Integer> V, int dist) {
        ArrayList<Float> L = new ArrayList<Float>();
        int length = V.size() * dist;
        for (int h = 1; h < length; h++) {
            L.add(variogram_one_calcul(V, h, dist));
        }
        return L;
    }

    public int portee(ArrayList<Integer> L, float var){
        for (int h = 1; h<=L.size(); h++){
            if (L.get(h)<var){
                int port = h-1;
            }
        }
        return port;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        V.add(0);
        V.add(1);
        V.add(2);
        V.add(3);
        V.add(4);
        V.add(4);
        V.add(4);
        V.add(4);
        V.add(4);
        V.add(4);
        V.add(4);
        V.add(4);
        V.add(4);
        V.add(4);
        V.add(4);
        V.add(4);
        V.add(4);
        dist=1;
        Log.d("+++++++++++++++++++++","");
        setContentView(R.layout.activity_variogram);
        LineChart chart = (LineChart) findViewById(R.id.chart);
        ArrayList<Float> L = variogram_total_calcul(V, dist);
        ArrayList<Integer> H = new ArrayList<Integer>();
        for (int i = 0; i<L.size(); i++) {
            H.add(i);
        }

        List<Entry> entries = new ArrayList<Entry>();
        for (int i : H){
            entries.add(new Entry(i,L.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Experimental Data");

        LineData lineData = new LineData(dataSet);

        chart.setData(lineData);
        chart.invalidate();


    }
}