package com.example.plug_in_pool;

import android.os.Bundle;
import java.time.LocalDateTime;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class gestionMatch extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_match);
    }
    public class Match
    {
        String nom;
        int nbManchesGagnantes;
        LocalDateTime horodatage;
        int gagnant;
        private EtatMatch etat;
    }
    public enum EtatMatch
    {
        Cree(0),
        EnCours(1),
        Fini(2);
        private final int etatMatch;
        private Match match;
        EtatMatch(int etatMatch)
        {
            this.etatMatch = etatMatch;
        }
    }
}