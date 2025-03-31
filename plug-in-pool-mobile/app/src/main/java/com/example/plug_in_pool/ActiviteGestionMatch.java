package com.example.plug_in_pool;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActiviteGestionMatch extends AppCompatActivity
{
    /**
     * Constantes
     */
    TextView afficherjoueur;
    private static final String TAG = "_ActiviteGestionMatch"; //!< TAG pour les logs

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_match);
        initialiserVues();
    }
    void initialiserVues()
    {
        afficherjoueur        = findViewById(R.id.joueur);
    }
    void afficherJoueurs()
    {
        afficherjoueur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                for (int i = 0; i < BlackBall.NB_JOUEURS; i++)
                {
                    //TODO afficher les joueurs
                    //String nomJoueur = Joueur.id[i].afficherJoueur();
                    //afficherjoueur.setText(nomJoueur);
                }
            }
        });
    }
}