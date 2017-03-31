package com.projet.pidr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String PRISE_KEY = "ID";
    public static final String PRISE_INTITULE = "title";
    public static final String PRISE_LONG = "longitude";
    public static final String PRISE_LAT = "latitude";
    public static final String PRISE_ALT = "altitude";
    public static final String TABLE = "toutprojet";
    public static final String PRISE_AZIMUTH ="azimuth";
    public static final String PRISE_PENDAGE ="pendage";
    public static final String PRISE_PROJECT ="project";
    public static final String EST_FAILLE = "est_faille";
    public static final String METIER_TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE + " ;";
    public static final String PRISE_TABLE_CREATE =
            "CREATE TABLE " + TABLE + " (" +
                    PRISE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    PRISE_INTITULE + " TEXT, " +
                    PRISE_LONG + " REAL, "+
                    PRISE_LAT+" REAL, "+
                    PRISE_ALT+" REAL, "+
                    PRISE_AZIMUTH+" REAL, "+
                    PRISE_PENDAGE+" REAL, "+
                    EST_FAILLE + " INTEGER, " +
                    PRISE_PROJECT +" TEXT);";

    public DatabaseHandler(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d("base", "base creee");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(PRISE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(METIER_TABLE_DROP);
        onCreate(db);
    }
}