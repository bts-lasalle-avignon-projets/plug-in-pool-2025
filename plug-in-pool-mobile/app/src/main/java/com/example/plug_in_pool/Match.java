package com.example.plug_in_pool;

import java.time.LocalDateTime;

public class Match
{
    public enum EtatMatch {
        Cree(0),
        EnCours(1),
        Fini(2);
        private final int etatMatch;
        private Match     match;
        EtatMatch(int etatMatch)
        {
            this.etatMatch = etatMatch;
        }
    }
    private String        nom;
    private int           nbManchesGagnantes;
    private LocalDateTime horodatage;
    private int           gagnant;
    private EtatMatch     etat;
}
