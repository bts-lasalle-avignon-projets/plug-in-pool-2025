package com.example.plug_in_pool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteConstraintException;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
      + "nbPartiesGagnantes INTEGER DEFAULT 1, "
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

    public void ajouterJoueur(String nom, String prenom)
    {
        try
        {
            Log.d(TAG, "ajouterJoueur(" + nom + ", " + prenom + ")");
            sqlite.execSQL("INSERT INTO joueurs (nom, prenom) VALUES ('" + nom + "', '" + prenom +
                           "')");
        }
        catch(SQLiteConstraintException e)
        {
            Log.d(TAG, "ajouterJoueur() joueur déjà présent dans la base de données !");
        }
    }

    public int creerMatch(int idJoueur1, int idJoueur2, int nbPartiesGagnantes)
    {
        String horodatage = obtenirHorodatageActuel();
        int idCree = -1;

        try
        {
            Log.d(TAG, "ajouterMatch(" + idJoueur1 + ", " + idJoueur2 + ", " + nbPartiesGagnantes + ", " + horodatage + ")");
            sqlite.execSQL("INSERT INTO matchs (idJoueur1, idJoueur2, nbPartiesGagnantes, fini, horodatage) " +
                    "VALUES (" + idJoueur1 + ", " + idJoueur2 + ", " + nbPartiesGagnantes + ", 0, '" + horodatage + "')");

            Cursor c = sqlite.rawQuery("SELECT last_insert_rowid()", null);
            if (c.moveToFirst()) {
                idCree = c.getInt(0);
                Log.d(TAG, "Match inséré avec idMatch = " + idCree);
            }
            c.close();
        }
        catch (SQLiteConstraintException e)
        {
            Log.d(TAG, "ajouterMatch() : erreur de contrainte !");
        }

        return idCree;
    }
    public String obtenirHorodatageActuel()
    {
        SimpleDateFormat horodatage = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return horodatage.format(new Date());
    }
    public int getIdJoueur(String nom, String prenom)
    {
        int idJoueur = -1;

        try
        {
            Cursor curseur = sqlite.rawQuery(
                    "SELECT idJoueur FROM joueurs WHERE nom = ? AND prenom = ?",
                    new String[]{nom, prenom}
            );

            if (curseur.moveToFirst())
            {
                idJoueur = curseur.getInt(0);
            }

            curseur.close();
        }
        catch (Exception e)
        {
            Log.d(TAG, "getIdJoueur() : erreur - " + e.getMessage());
        }

        return idJoueur;
    }
    public void ajouterManche(int idMatch, int idGagnant, int idPerdant, int numeroTable)
    {
        String horodatage = obtenirHorodatageActuel();
        try
        {
            sqlite.execSQL("INSERT INTO manches (idMatch, idGagnant, idPerdant, numeroTable, horodatage) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    new Object[]{idMatch, idGagnant, idPerdant, numeroTable, horodatage});
            Log.d(TAG, "ajouterManche() ajoutée avec succès");
        }
        catch (Exception e)
        {
            Log.e(TAG, "ajouterManche() erreur : " + e.getMessage());
        }
    }
    public void terminerMatch(int idMatch, int valeurFini)
    {
        try
        {
            Log.d(TAG, "mettreAJourFini(" + idMatch + ", " + valeurFini + ")");

            String requete = "UPDATE matchs SET fini = " + valeurFini + " WHERE idMatch = " + idMatch;

            sqlite.beginTransaction();
            sqlite.execSQL(requete);
            sqlite.setTransactionSuccessful();
            sqlite.endTransaction();

            Cursor verif = sqlite.rawQuery("SELECT fini FROM matchs WHERE idMatch = " + idMatch, null);
            if (verif.moveToFirst()) {
                int fini = verif.getInt(0);
                Log.d(TAG, "Vérification après UPDATE : fini = " + fini);
            } else {
                Log.e(TAG, "Match id " + idMatch + " introuvable !");
            }
            verif.close();
        }
        catch (Exception e)
        {
            Log.e(TAG, "mettreAJourFini() : erreur - " + e.getMessage());
        }
    }
    public ArrayList<String> getInfosMatchs()
    {
        ArrayList<String> listeInfos = new ArrayList<>();
        SQLiteDatabase bd = getReadableDatabase();

        String requeteMatchs =
                "SELECT m.idMatch, m.horodatage, " +
                        "j1.nom AS nomJoueur1, j1.prenom AS prenomJoueur1, " +
                        "j2.nom AS nomJoueur2, j2.prenom AS prenomJoueur2 " +
                        "FROM matchs m " +
                        "LEFT JOIN joueurs j1 ON m.idJoueur1 = j1.idJoueur " +
                        "LEFT JOIN joueurs j2 ON m.idJoueur2 = j2.idJoueur " +
                        "ORDER BY m.horodatage DESC";

        Cursor curseurMatchs = bd.rawQuery(requeteMatchs, null);

        if (curseurMatchs.moveToFirst())
        {
            do
            {
                int idMatch = curseurMatchs.getInt(curseurMatchs.getColumnIndexOrThrow("idMatch"));
                String horodatageMatch = curseurMatchs.getString(curseurMatchs.getColumnIndexOrThrow("horodatage"));

                String nomJoueur1 = curseurMatchs.getString(curseurMatchs.getColumnIndexOrThrow("nomJoueur1"));
                String prenomJoueur1 = curseurMatchs.getString(curseurMatchs.getColumnIndexOrThrow("prenomJoueur1"));
                String nomJoueur2 = curseurMatchs.getString(curseurMatchs.getColumnIndexOrThrow("nomJoueur2"));
                String prenomJoueur2 = curseurMatchs.getString(curseurMatchs.getColumnIndexOrThrow("prenomJoueur2"));

                StringBuilder texte = new StringBuilder();
                texte.append("Match ").append(idMatch)
                        .append(" : ").append(horodatageMatch);

                String requeteManches =
                        "SELECT idManche, numeroTable, horodatage, idGagnant, idPerdant " +
                                "FROM manches WHERE idMatch = ? ORDER BY horodatage ASC";

                Cursor curseurManches = bd.rawQuery(requeteManches, new String[]{String.valueOf(idMatch)});

                while (curseurManches.moveToNext())
                {
                    int idManche = curseurManches.getInt(curseurManches.getColumnIndexOrThrow("idManche"));
                    int table = curseurManches.getInt(curseurManches.getColumnIndexOrThrow("numeroTable"));
                    String horodatageManche = curseurManches.getString(curseurManches.getColumnIndexOrThrow("horodatage"));
                    int idGagnant = curseurManches.getInt(curseurManches.getColumnIndexOrThrow("idGagnant"));
                    int idPerdant = curseurManches.getInt(curseurManches.getColumnIndexOrThrow("idPerdant"));

                    String gagnant = (idGagnant == 0) ? (prenomJoueur1 + " " + nomJoueur1) : (prenomJoueur2 + " " + nomJoueur2);
                    String perdant = (idPerdant == 0) ? (prenomJoueur1 + " " + nomJoueur1) : (prenomJoueur2 + " " + nomJoueur2);

                    texte.append("\n - Manche ").append(idManche)
                            .append(" : gagnant = ").append(gagnant)
                            .append(", perdant = ").append(perdant)
                            .append(", table ").append(table)
                            .append(", ").append(horodatageManche);
                }
                curseurManches.close();
                listeInfos.add(texte.toString());

            } while (curseurMatchs.moveToNext());
        }
        curseurMatchs.close();
        return listeInfos;
    }
    public void purgerHistorique()
    {
        try
        {
            Log.d(TAG, "purgerHistorique()");
            sqlite.execSQL("DELETE FROM manches");
            sqlite.execSQL("DELETE FROM matchs");
        }
        catch (Exception e)
        {
            Log.e(TAG, "purgerHistorique() : erreur - " + e.getMessage());
        }
    }

}
