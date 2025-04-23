package com.example.plug_in_pool;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActiviteGestionPartie extends AppCompatActivity
{
    /**
     * Constantes
     */
    private TextView textViewJoueur1;
    private TextView textViewJoueur2;
    private static final String TAG = "_ActiviteGestionPartie"; //!< TAG pour les logs

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_partie);
        initialiserVues();
        recupererJoueurs();
    }
    void initialiserVues()
    {
        textViewJoueur1 = findViewById(R.id.afficherJoueur1);
        textViewJoueur2 = findViewById(R.id.afficherJoueur2);
    }
    void recupererJoueurs()
    {
        Intent intent = getIntent();
        Joueur joueur1 = (Joueur) intent.getSerializableExtra("joueur1");
        Joueur joueur2 = (Joueur) intent.getSerializableExtra("joueur2");

        if (joueur1 != null && joueur2 != null)
        {
            textViewJoueur1.setText("Joueur 1: " + joueur1.getNom() + " " + joueur1.getPrenom());
            textViewJoueur2.setText("Joueur 2: " + joueur2.getNom() + " " + joueur2.getPrenom());
        }
    }
}