package com.example.plug_in_pool;

import java.time.LocalDateTime;

public class Manche
{
    public enum EtatManche
    {
        Debut(0),
        EnCours(1),
        Fini(2);
        private final int etatManche;
        private Manche    manche;
        EtatManche(int etatManche)
        {
            this.etatManche = etatManche;
        }
    }
    private int           numeroTable;
    private LocalDateTime horodatage;
    private int           gagnant;
    private int           joueurTable;
    private boolean       couleursDefinies;
    private int           duree;
}