package com.example.plug_in_pool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;

public class BaseDeDonnees extends SQLiteOpenHelper
{
    private static final String  TAG         = "_BaseDeDonnees"; //!< TAG pour les logs (cf. Logcat)
    private static final String  NOM_BDD     = "plugin_pool.db";
    private static final int     VERSION_BDD = 1;
    private static BaseDeDonnees baseDeDonnees =
      null;                              //!< L'instance unique de BaseDeDonnees (singleton)
    private final SQLiteDatabase sqlite; //<! L'accès à la base de données SQLite

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
      + "nbManchesGagnantes INTEGER DEFAULT 1, "
      + "fini INTEGER DEFAULT 0, "
      + "horodatage DATETIME NOT NULL, "
      + "FOREIGN KEY (idJoueur1) REFERENCES joueurs(idJoueur) ON DELETE CASCADE, "
      + "FOREIGN KEY (idJoueur2) REFERENCES joueurs(idJoueur) ON DELETE CASCADE);";

    private static final String CREER_TABLE_MANCHES =
      "CREATE TABLE IF NOT EXISTS " + TABLE_MANCHES + " ("
      + "idManche INTEGER PRIMARY KEY AUTOINCREMENT, "
      + "idMatch INTEGER, "
      + "idGagnant INTEGER, "
      + "idPerdant INTEGER, "
      + "numeroTable INTEGER, "
      + "horodatage DATETIME UNIQUE NOT NULL, "
      + "FOREIGN KEY (idGagnant) REFERENCES joueurs(idJoueur) ON DELETE CASCADE, "
      + "FOREIGN KEY (idPerdant) REFERENCES joueurs(idJoueur) ON DELETE CASCADE,"
      + "FOREIGN KEY (idMatch)   REFERENCES matchs(idMatch) ON DELETE CASCADE);";

    private static final String AJOUTER_JOUEURS = "INSERT INTO " + TABLE_JOUEURS +
                                                  "(idJoueur, nom, prenom) VALUES "
                                                  + " (1,'ARIATI','Axel'),\n"
                                                  + " (2,'BERNARD','Clément'),\n"
                                                  + " (3,'BLONDEL','Joshua'),\n"
                                                  + " (4,'BOUSQUET SOLFRINI','Valentin'),\n"
                                                  + " (5,'CLÉMENT','Aymeric'),\n"
                                                  + " (6,'GASSE','Lenny'),\n"
                                                  + " (7,'MILLOT','Pierre'),\n"
                                                  + " (8,'NAVARRO','Mattéo'),\n"
                                                  + " (9,'PESSINA','Nicolas'),\n"
                                                  + " (10,'RAFFIN','Louis'),\n"
                                                  + " (11,'SORIA-BONET','Enzo'),\n"
                                                  + " (12,'VALOBRA','Enzo'),\n"
                                                  + " (13,'VANDENBROUCKE','Théo'),\n"
                                                  + " (14,'VAUDAINE','Dylan');\n";

    private BaseDeDonnees(Context context)
    {
        super(context, NOM_BDD, null, VERSION_BDD);
        Log.d(TAG, "BaseDeDonnees()");
        sqlite = this.getWritableDatabase();
    }

    public synchronized static BaseDeDonnees getInstance(Context context)
    {
        Log.d(TAG, "getInstance()");
        if(baseDeDonnees == null)
        {
            baseDeDonnees = new BaseDeDonnees(context);
        }
        return baseDeDonnees;
    }

    public void effacer()
    {
        Log.d(TAG, "effacer()");
        onUpgrade(sqlite, sqlite.getVersion(), sqlite.getVersion() + 1);
    }

    @Override
    public void onCreate(SQLiteDatabase baseDeDonnees)
    {
        Log.d(TAG, "onCreate()");
        baseDeDonnees.execSQL(CREER_TABLE_JOUEURS);
        baseDeDonnees.execSQL(CREER_TABLE_MATCHS);
        baseDeDonnees.execSQL(CREER_TABLE_MANCHES);
        insererDonnees(baseDeDonnees);
    }

    @Override
    public void onUpgrade(SQLiteDatabase baseDeDonnees, int oldVersion, int newVersion)
    {
        Log.d(TAG, "onUpgrade()");
        baseDeDonnees.execSQL("DROP TABLE IF EXISTS " + TABLE_MANCHES);
        baseDeDonnees.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHS);
        baseDeDonnees.execSQL("DROP TABLE IF EXISTS " + TABLE_JOUEURS);
        baseDeDonnees.setVersion(newVersion);
        onCreate(baseDeDonnees);
    }

    private void insererDonnees(SQLiteDatabase baseDeDonnees)
    {
        Log.d(TAG, "insererDonnees()");
        // Pour les tests
        baseDeDonnees.execSQL(AJOUTER_JOUEURS);
    }

    public ArrayList<String> getNomsJoueurs()
    {
        ArrayList<String> joueurs = new ArrayList<String>();
        Cursor            curseur = sqlite.rawQuery("SELECT nom,prenom FROM joueurs", null);
        if(curseur.moveToFirst())
        {
            do
            {
                String nomJoueur    = curseur.getString(0);
                String prenomJoueur = curseur.getString(1);
                joueurs.add(prenomJoueur + " " + nomJoueur);
            } while(curseur.moveToNext());
        }
        curseur.close();
        Log.d(TAG, "getJoueurs() " + joueurs);

        return joueurs;
    }
}
