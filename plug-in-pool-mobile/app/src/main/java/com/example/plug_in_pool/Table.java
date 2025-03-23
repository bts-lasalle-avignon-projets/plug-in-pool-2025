package com.example.plug_in_pool;

public class Table {
    public class Joueur
    {
        String nom;
        String prenom;
        int points;
        int id;
        int nbBillesRestantes = 7;
        private CouleurBille couleur;
    }
    public class Empoche
    {
        CouleurBille couleur;
        int poche;
    }
    public enum CouleurBille
    {
        ROUGE(0),
        JAUNE(1),
        BLANCHE(2),
        NOIRE(3);
        private final int valeur;
        CouleurBille(int valeur)
        {
            this.valeur = valeur;
        }
    }
}