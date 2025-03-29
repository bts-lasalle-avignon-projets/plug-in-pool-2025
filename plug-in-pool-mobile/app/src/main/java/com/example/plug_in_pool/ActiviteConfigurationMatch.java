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
    Button boutonLancerUnMatch;
    Button creerJoueur1;
    Button creerJoueur2;
    EditText saisieNomJoueur1;
    EditText saisiePrenomJoueur1;
    EditText saisieNomJoueur2;
    EditText saisiePrenomJoueur2;

    private static final String TAG = "_ActiviteConfigurationMatch"; //!< TAG pour les logs

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuration_match);
        initialiserVues();
        configurerMatch();
        jouerMatchActivite();
    }
    void initialiserVues()
    {
        boutonLancerUnMatch = findViewById(R.id.boutonLancerMatch);
        creerJoueur1 = findViewById(R.id.creerJoueur1);
        creerJoueur2 = findViewById(R.id.creerJoueur2);
        saisieNomJoueur1 = findViewById(R.id.saisieNomJoueur1);
        saisiePrenomJoueur1 = findViewById(R.id.saisiePrenomJoueur1);
        saisieNomJoueur2 = findViewById(R.id.saisieNomJoueur2);
        saisiePrenomJoueur2 = findViewById(R.id.saisiePrenomJoueur2);
    }
    void configurerMatch()
    {
        creerDesJoueurs();
    }
    void creerDesJoueurs()
    {
        creerJoueur1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String nomJoueur1 = saisieNomJoueur1.getText().toString().trim();
                String prenomJoueur1 = saisiePrenomJoueur1.getText().toString().trim();
                Joueur joueur1 = new Joueur(nomJoueur1, prenomJoueur1);
                joueur1.getJoueur();
                joueur1.afficherJoueur();
                Toast.makeText(ActiviteConfigurationMatch.this,"Votre nom : " + nomJoueur1 + "\n" + "Votre prénom : " + prenomJoueur1, Toast.LENGTH_SHORT).show();
            }
        });
        creerJoueur2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String nomJoueur2 = saisieNomJoueur2.getText().toString().trim();
                String prenomJoueur2 = saisiePrenomJoueur2.getText().toString().trim();
                Joueur joueur2 = new Joueur(nomJoueur2, prenomJoueur2);
                joueur2.getJoueur();
                joueur2.afficherJoueur();
                Toast.makeText(ActiviteConfigurationMatch.this,"Votre nom : " + nomJoueur2 + "\n" + "Votre prénom : " + prenomJoueur2, Toast.LENGTH_SHORT).show();
            }
        });
    }
    void jouerMatchActivite()
    {
        boutonLancerUnMatch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent changerDeVue = new Intent(ActiviteConfigurationMatch.this, ActiviteGestionMatch.class);
                startActivity(changerDeVue);
            }
        });
    }
}