package com.projet.pidr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class PointBDD {
    // Nous sommes �� la premi��re version de la base
    // Si je d��cide de la mettre �� jour, il faudra changer cet attribut
    public static final String PRISE_KEY = "id";
    public static final int NUM_COL_KEY = 0;

    public static final String PRISE_INTITULE = "title";
    public static final int NUM_COL_INTITULE = 1;

    public static final String PRISE_LONG = "longitude";
    public static final int NUM_COL_LONG = 2;

    public static final String PRISE_LAT = "latitude";
    public static final int NUM_COL_LAT = 3;

    public static final String PRISE_ALT = "altitude";
    public static final int NUM_COL_ALT = 4;

    public static final String PRISE_AZIMUTH = "azimuth";
    public static final int NUM_COL_AZIMUTH = 5;

    public static final String PRISE_PENDAGE = "pendage";
    public static final int NUM_COL_PENDAGE = 6;

    public static final String PRISE_PROJECT = "project";
    public static final int NUM_COL_PROJECT = 7;

    public static final String PRISE_TYPE = "type_point";
    public  static final int NUM_COL_TYPE_POINT = 8;

    protected int VERSION = 150;

    // Le nom du fichier qui repr��sente ma base
    protected final static String NOM = "pointFinal.db";
    public static final String TABLE = "toutprojet";

    protected SQLiteDatabase bdd = null;
    protected DatabaseHandler mHandler = null;

    public PointBDD() {
    }

    public PointBDD(Context pContext) {
        this.mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
        //this.mHandler.onCreate(mDb);
    }

    public SQLiteDatabase open() {// Pas besoin de fermer la derni��re base puisque getWritableDatabase s'en charge
        bdd = mHandler.getWritableDatabase();
        return bdd;
    }

    public DatabaseHandler getDataBaseHandler() {
        return this.mHandler;
    }

    public void close() {
        bdd.close();
    }

    public SQLiteDatabase getDb() {
        return bdd;
    }

    public long insertPoint(Point point) {
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(PRISE_INTITULE, point.getTitle());
        values.put(PRISE_LONG, point.getLong());
        values.put(PRISE_LAT, point.getLat());
        values.put(PRISE_ALT, point.getAltitude());
        values.put(PRISE_AZIMUTH, point.getAzimuth());
        values.put(PRISE_PENDAGE, point.getPendage());
        values.put(PRISE_PROJECT, point.getProject());
        values.put(PRISE_TYPE, point.getType_point());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE, null, values);

    }

    public int removePointWithID(int id) {
        //Suppression d'un livre de la BDD grâce à l'ID
        return bdd.delete(TABLE, PRISE_KEY + " = " + id, null);
    }

    public int updatePoint(int id, Point point) {
        //La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simple préciser quelle livre on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(PRISE_INTITULE, point.getTitle());
        values.put(PRISE_LONG, point.getLong());
        values.put(PRISE_LAT, point.getLat());
        values.put(PRISE_ALT, point.getAltitude());
        values.put(PRISE_AZIMUTH, point.getAzimuth());
        values.put(PRISE_PENDAGE, point.getPendage());
        values.put(PRISE_PROJECT, AllClass.project);
        values.put(PRISE_TYPE, point.getType_point());
        return bdd.update(TABLE, values, PRISE_KEY + " = " + id, null);
    }

    public Point getPointWithTitre(String titre, String project) {
        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE, new String[]{PRISE_KEY, PRISE_INTITULE, PRISE_LONG, PRISE_LAT, PRISE_ALT, PRISE_AZIMUTH, PRISE_PENDAGE, PRISE_PROJECT, PRISE_TYPE}, PRISE_INTITULE + " LIKE ? AND " + PRISE_PROJECT + " LIKE ?", new String[]{"%" + titre + "%", "%" + project + "%"}, null, null, null, null);
        return cursorToPoint(c);
    }

    //Cette méthode permet de convertir un cursor en un livre
    private Point cursorToPoint(Cursor c) {
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un livre
        Point point = new Point();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        point.setId(c.getInt(NUM_COL_KEY));
        point.setTitle(c.getString(NUM_COL_INTITULE));
        point.setLongi(c.getDouble(NUM_COL_LONG));
        point.setLat(c.getDouble(NUM_COL_LAT));
        point.setAltitude(c.getDouble(NUM_COL_ALT));
        point.setAzimuth(c.getDouble(NUM_COL_AZIMUTH));
        point.setPendage(c.getDouble(NUM_COL_PENDAGE));
        point.setProject(c.getString(NUM_COL_PROJECT));
        point.setType_point(c.getString(NUM_COL_TYPE_POINT));
        //On ferme le cursor
        c.close();
        //On retourne le livre
        return point;
    }

    public ArrayList<Integer> getIdWithProject(String project) {

        ArrayList<Integer> output = new ArrayList<Integer> ();

        String[] colonnesARecup = new String[]{PRISE_KEY};

        Cursor c = bdd.query(TABLE, new String[]{PRISE_KEY, PRISE_INTITULE, PRISE_LONG, PRISE_LAT, PRISE_ALT, PRISE_AZIMUTH, PRISE_PENDAGE, PRISE_PROJECT, PRISE_TYPE}, PRISE_PROJECT + " = ?", new String[]{project}, null, null, null, null);

        if (null != c) {
            if (c.moveToFirst()) {
                do {
                    int columnIdxNom = c.getColumnIndex(PRISE_KEY);

                    int nomDb = c.getInt(columnIdxNom);

                    output.add(nomDb);
                } while (c.moveToNext());
            } // end§if
        }
        return output;
    }

    public ArrayList<Point> getPointsByProject(String project) {
        ArrayList<Point> points = new ArrayList<Point>();
        Cursor c = bdd.query(TABLE, new String[]{PRISE_KEY, PRISE_INTITULE, PRISE_LONG, PRISE_LAT, PRISE_ALT, PRISE_AZIMUTH, PRISE_PENDAGE, PRISE_PROJECT, PRISE_TYPE}, PRISE_PROJECT + "=?", new String[]{project}, null, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Point currentPoint = new Point();
                    currentPoint.setTitle(c.getString(c.getColumnIndex(PRISE_INTITULE)));
                    //currentPoint.setId(Integer.parseInt(context.getString(context.getColumnIndex(PRISE_KEY))));
                    currentPoint.setAltitude(Double.parseDouble(c.getString(c.getColumnIndex(PRISE_ALT))));
                    currentPoint.setLat(Double.parseDouble(c.getString(c.getColumnIndex(PRISE_LAT))));
                    currentPoint.setLongi(Double.parseDouble(c.getString(c.getColumnIndex(PRISE_LONG))));
                    currentPoint.setAltitude(Double.parseDouble(c.getString(c.getColumnIndex(PRISE_ALT))));
                    currentPoint.setAzimuth(Double.parseDouble(c.getString(c.getColumnIndex(PRISE_AZIMUTH))));
                    currentPoint.setPendage(Double.parseDouble(c.getString(c.getColumnIndex(PRISE_PENDAGE))));
                    currentPoint.setType_point(c.getString(c.getColumnIndex(PRISE_TYPE)));
                    points.add(currentPoint);
                } while (c.moveToNext());
            }
        }
        return points;
    }

    public boolean checkNameNewPoint(String nom, String project){

        ArrayList<String> output = new ArrayList<String>();

        Cursor c = bdd.query(TABLE, new String[]{PRISE_KEY, PRISE_INTITULE, PRISE_LONG, PRISE_LAT, PRISE_ALT, PRISE_AZIMUTH, PRISE_PENDAGE, PRISE_PROJECT, PRISE_TYPE}, PRISE_PROJECT + " = ?", new String[]{project}, null, null, null, null);

        if (null != c) {
            if (c.moveToFirst()) {
                do {
                    int columnIdxNom = c.getColumnIndex(PRISE_INTITULE);

                    String nomDb = c.getString(columnIdxNom);

                    output.add(nomDb);
                } while (c.moveToNext());
            } // end§if
        }
        // Parcours du curseur pour verifier la presence du point

        int listSize = output.size();
        int som = 0;
        for (int i = 0; i<listSize; i++) {
            if (nom.equals(output.get(i)))
            {
                som = (som + 1);
            }
        }
        if (som == 0) {
            return true;}
        else {
            return false;}
        }





    public ArrayList<String> getCreator() {
        ArrayList<String> output = new ArrayList<String>();

        String[] colonnesARecup = new String[]{PRISE_PROJECT};

        Cursor cursorResults = bdd.query(TABLE, colonnesARecup, null,
                null, null, null, PRISE_PROJECT, null);
        if (null != cursorResults) {
            if (cursorResults.moveToFirst()) {
                do {
                    int columnIdxNom = cursorResults.getColumnIndex(PRISE_PROJECT);

                    String nomDb = cursorResults.getString(columnIdxNom);

                    output.add(nomDb);
                } while (cursorResults.moveToNext());
            } // end§if
        }
        int listSize = output.size();

        for (int i = 0; i<listSize; i++){
            Log.i("Nom : ", output.get(i));
        }

    return output;
    }


}

