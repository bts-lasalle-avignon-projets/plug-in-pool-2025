package com.example.plug_in_pool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActiviteConfigurationMatch extends AppCompatActivity
{
    /**
     * Constantes
     */
    private Button   boutonLancerUnMatch;
    private EditText saisieNomJoueur1;
    private EditText saisiePrenomJoueur1;
    private EditText saisieNomJoueur2;
    private EditText saisiePrenomJoueur2;

    private static final String TAG = "_ActiviteConfigurationMatch"; //!< TAG pour les logs

    Joueur joueur1;
    Joueur joueur2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuration_match);
        initialiserVues();
        jouerMatchActivite();
    }
    void initialiserVues()
    {
        boutonLancerUnMatch = findViewById(R.id.boutonLancerMatch);
        saisieNomJoueur1    = findViewById(R.id.saisieNomJoueur1);
        saisiePrenomJoueur1 = findViewById(R.id.saisiePrenomJoueur1);
        saisieNomJoueur2    = findViewById(R.id.saisieNomJoueur2);
        saisiePrenomJoueur2 = findViewById(R.id.saisiePrenomJoueur2);
    }
    void creerJoueurs()
    {
        String nomJoueur1    = saisieNomJoueur1.getText().toString();
        String prenomJoueur1 = saisiePrenomJoueur1.getText().toString();
        joueur1 = new Joueur(nomJoueur1, prenomJoueur1);

        String nomJoueur2    = saisieNomJoueur2.getText().toString();
        String prenomJoueur2 = saisiePrenomJoueur2.getText().toString();
        joueur2 = new Joueur(nomJoueur2, prenomJoueur2);
    }
    void jouerMatchActivite()
    {
        boutonLancerUnMatch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                creerJoueurs();
                Intent changerDeVue =
                  new Intent(ActiviteConfigurationMatch.this, ActiviteGestionMatch.class);
                changerDeVue.putExtra("joueur1", joueur1);
                changerDeVue.putExtra("joueur2", joueur2);
                startActivity(changerDeVue);
            }
        });
    }
}