package com.example.plug_in_pool;

public enum CouleurBille {
    AUCUNE(-1),
    ROUGE(0),
    JAUNE(1),
    BLANCHE(2),
    NOIRE(3);

    private final int valeur;

    CouleurBille(int valeur)
    {
        this.valeur = valeur;
    }
    public int getId()
    {
        return valeur;
    }
}