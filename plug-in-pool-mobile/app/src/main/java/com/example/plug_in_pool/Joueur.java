package com.example.plug_in_pool;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Joueur
{
    /**
     * Constantes
     */
    private static final String TAG = "_Produit"; //!< TAG pour les logs

    private String        nom;
    private String        prenom;
    private int           points;
    private int           id;
    private int           nbBillesRestantes = BlackBall.NB_BILLES_COULEUR;
    private CouleurBille  couleur;
    private List<Empoche> empoches;

    public Joueur(String nom, String prenom, int points, int id)
    {
        Log.d(TAG,
              "Produit() nom = " + nom + " - prenom = " + prenom + " - points = " + points +
                " - id = " + id);
        this.nom    = nom;
        this.prenom = prenom;
        this.points = points;
        this.id     = id;
    }
}