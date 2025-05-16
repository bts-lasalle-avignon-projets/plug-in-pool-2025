package com.example.plug_in_pool;

import java.io.Serializable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Joueur implements Serializable
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

    public Joueur(String nom, String prenom)
    {
        this.nom    = nom;
        this.prenom = prenom;
        this.points = 0;
        this.id     = 0;
    }

    public String getNom()
    {
        return nom;
    }
    public String getPrenom()
    {
        return prenom;
    }
    public int ajouterPoint(){return points++;}
    public int ajouterCouleur(int couleur)
    {
        return couleur;
    }
    public int getId(){return id;}

    public void afficherJoueur()
    {
        Log.d("Joueur", "Nom : " + nom + " | Prénom : " + prenom);
    }

    @Override
    public String toString() {
        return nom + prenom;
    }
}