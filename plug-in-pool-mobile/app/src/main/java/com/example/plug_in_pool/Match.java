package com.example.plug_in_pool;

import java.time.LocalDateTime;

public class Match
{
    public enum EtatMatch
    {
        CREE(0),
        EN_COURS(1),
        FINI(2);

        private final int valeur;

        EtatMatch(int valeur)
        {
            this.valeur = valeur;
        }

        public int getValeur()
        {
            return valeur;
        }
    }

    private String nom;
    private int nbManchesGagnantes;
    private LocalDateTime horodatage;
    private int gagnant;
    private EtatMatch etat;

    public Match(String nom, int nbManchesGagnantes)
    {
        this.nom = nom;
        this.nbManchesGagnantes = nbManchesGagnantes;
        this.horodatage = LocalDateTime.now();
        this.etat = EtatMatch.CREE;
        this.gagnant = -1;
    }

    public String getNom()
    {
        return nom;
    }
    public int getNbManchesGagnantes() {
        return nbManchesGagnantes;
    }
    public LocalDateTime getHorodatage() {
        return horodatage;
    }
    public int getGagnant() {
        return gagnant;
    }
    public EtatMatch getEtat() {
        return etat;
    }
    public void setGagnant(int gagnant) {
        this.gagnant = gagnant;
    }
    public void setEtat(EtatMatch etat) {
        this.etat = etat;
    }
}