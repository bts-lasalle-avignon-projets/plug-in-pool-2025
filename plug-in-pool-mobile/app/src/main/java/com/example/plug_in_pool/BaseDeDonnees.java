package com.example.plug_in_pool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDonnees extends SQLiteOpenHelper
{
    private static final int    VERSION_BDD = 1;
    private static final String NOM_BDD     = "plugin_pool.db";

    public static final String TABLE_JOUEURS = "joueurs";
    public static final String TABLE_MATCHS  = "matchs";
    public static final String TABLE_MANCHES = "manches";

    private static final String CREER_TABLE_JOUEURS =
      "CREATE TABLE IF NOT EXISTS " + TABLE_JOUEURS + " ("
      + "idJoueur INTEGER PRIMARY KEY AUTOINCREMENT, "
      + "nom VARCHAR, "
      + "prenom VARCHAR, "
      + "points INTEGER DEFAULT 0, "
      + "UNIQUE(nom, prenom));";

    private static final String CREER_TABLE_MATCHS =
      "CREATE TABLE IF NOT EXISTS " + TABLE_MATCHS + " ("
      + "idMatch INTEGER PRIMARY KEY AUTOINCREMENT, "
      + "idJoueur1 INTEGER NOT NULL, "
      + "idJoueur2 INTEGER NOT NULL, "
      + "nbManchesGagnantes INTEGER DEFAULT 0, "
      + "fini INTEGER DEFAULT 0, "
      + "horodatage DATETIME NOT NULL, "
      + "FOREIGN KEY (idJoueur1) REFERENCES joueurs(idJoueur) ON DELETE CASCADE, "
      + "FOREIGN KEY (idJoueur2) REFERENCES joueurs(idJoueur) ON DELETE CASCADE);";

    private static final String CREER_TABLE_MANCHES =
      "CREATE TABLE IF NOT EXISTS " + TABLE_MANCHES + " ("
      + "idManche INTEGER PRIMARY KEY AUTOINCREMENT, "
      + "idMatch INTEGER DEFAULT 0, "
      + "idGagnant INTEGER, "
      + "idPerdant INTEGER, "
      + "numeroTable INTEGER, "
      + "horodatage DATETIME UNIQUE NOT NULL, "
      + "FOREIGN KEY (idGagnant) REFERENCES joueurs(idJoueur) ON DELETE CASCADE, "
      + "FOREIGN KEY (idPerdant) REFERENCES joueurs(idJoueur) ON DELETE CASCADE);";

    public BaseDeDonnees(Context context)
    {
        super(context, NOM_BDD, null, VERSION_BDD);
    }

    @Override
    public void onCreate(SQLiteDatabase baseDeDonnees)
    {
        baseDeDonnees.execSQL(CREER_TABLE_JOUEURS);
        baseDeDonnees.execSQL(CREER_TABLE_MATCHS);
        baseDeDonnees.execSQL(CREER_TABLE_MANCHES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase baseDeDonnees, int oldVersion, int newVersion)
    {
        baseDeDonnees.execSQL("DROP TABLE IF EXISTS " + TABLE_MANCHES);
        baseDeDonnees.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHS);
        baseDeDonnees.execSQL("DROP TABLE IF EXISTS " + TABLE_JOUEURS);

        onCreate(baseDeDonnees);
    }
}