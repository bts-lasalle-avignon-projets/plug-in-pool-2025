package com.example.plug_in_pool;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

public class ActiviteConfigurationMatch extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG =
      "_ActiviteConfigurationMatch"; //!< TAG pour les logs (cf. Logcat)

    /**
     * Éléments de l'interface
     */
    private Button               boutonSuivant;
    private AutoCompleteTextView choixNomJoueur1;
    private AutoCompleteTextView choixNomJoueur2;

    private BaseDeDonnees baseDonnees; //!< Classe d'accès avec la base de données
    Vector<Joueur>        joueurs = new Vector<Joueur>();
    // Joueur                joueur1;
    // Joueur                joueur2;
    private ArrayList<String>
      nomJoueurs; //!< Tableau contenant les noms et prénoms des joueurs dans la base de données

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuration_match);

        Log.d(TAG, "onCreate()");

        initialiserVue();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume()");

        joueurs.clear();
    }

    private void initialiserVue()
    {
        Log.d(TAG, "initialiserVue()");
        boutonSuivant = findViewById(R.id.boutonSuivant);
        choixNomJoueur1   = findViewById(R.id.choixNomJoueur1);
        choixNomJoueur2   = findViewById(R.id.choixNomJoueur2);

        baseDonnees = BaseDeDonnees.getInstance(this);

        initialiserListeJoueurs();
        jouerMatch();
    }

    private void initialiserListeJoueurs()
    {
        Log.d(TAG, "ajouterJoueurs()");
        nomJoueurs = baseDonnees.getNomsJoueurs();
        ArrayAdapter<String> listeJoueurs =
          new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nomJoueurs);
        choixNomJoueur1.setAdapter(listeJoueurs);
        choixNomJoueur2.setAdapter(listeJoueurs);
    }

    private boolean ajouterJoueur(int numeroJoueur)
    {
        String nomJoueur = "";
        if(numeroJoueur == 1)
            nomJoueur = choixNomJoueur1.getText().toString().trim();
        else if(numeroJoueur == 2)
            nomJoueur = choixNomJoueur2.getText().toString().trim();

        String[] donneesJoueur = nomJoueur.split(" ");
        if(donneesJoueur.length == 2)
        {
            String prenom = donneesJoueur[0];
            String nom    = donneesJoueur[1];
            Joueur joueur = new Joueur(nom, prenom);
            joueurs.add(joueur);
            Log.d(TAG,
                  "ajouterJoueur(" + numeroJoueur + ") " + joueur.getPrenom() + " " +
                    joueur.getNom());
            baseDonnees.ajouterJoueur(nom, prenom);
            return true;
        }

        return false;
    }

    private int getValeurSaisie() {
        return 1;
    }

    private boolean enregistrerDonnees()
    {
        if(!ajouterLesDeuxJoueurs())
        {
            return false;
        }

        return creerLeMatch();
    }

    private boolean ajouterLesDeuxJoueurs()
    {
        return ajouterJoueur(1) && ajouterJoueur(2);
    }

    private boolean creerLeMatch()
    {
        int nbParties = getValeurSaisie();

        Joueur joueur1 = joueurs.get(0);
        Joueur joueur2 = joueurs.get(1);

        int id1 = baseDonnees.getIdJoueur(joueur1.getNom(), joueur1.getPrenom());
        int id2 = baseDonnees.getIdJoueur(joueur2.getNom(), joueur2.getPrenom());

        Log.d(TAG, "id1 : " + id1 + ", id2 : " + id2 + ", nbParties : " + nbParties);

        if(id1 != -1 && id2 != -1)
        {
            baseDonnees.creerMatch(id1, id2, nbParties);
            return true;
        }
        else
        {
            return false;
        }
    }

    private void jouerMatch()
    {
        boutonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(estMatchConfigure())
                {
                    Log.d(TAG,
                          "jouerMatch() " + choixNomJoueur1.getText().toString() + " vs " +
                            choixNomJoueur2.getText().toString());

                    if(enregistrerDonnees())
                    {
                        Log.d(TAG, "jouerMatch() nbJoueurs : " + joueurs.size());
                        Intent changerVue =
                          new Intent(ActiviteConfigurationMatch.this, ActiviteGestionPartie.class);
                        changerVue.putExtra("joueur1", joueurs.elementAt(0));
                        changerVue.putExtra("joueur2", joueurs.elementAt(1));
                        changerVue.putExtra("nbParties", getValeurSaisie());
                        startActivity(changerVue);
                    }
                    else
                    {
                    }
                }
                else
                {
                    Log.d(TAG, "jouerMatch() configuration incomplète ou invalide !");
                }
            }
        });
    }

    private Boolean estMatchConfigure()
    {
        Log.d(TAG, "estMatchConfigure()");

        // il faut un nom de joueur 1
        if(choixNomJoueur1.getText().toString().isEmpty())
            return false;

        // il faut un nom de joueur 2
        if(choixNomJoueur2.getText().toString().isEmpty())
            return false;

        // il faut que le nom de joueur 1 soit différent du nom de joueur 2
        if(choixNomJoueur1.getText().toString().equals(choixNomJoueur2.getText().toString()))
            return false;

        Log.d(TAG,
              "estMatchConfigure() joueur1 = " + choixNomJoueur1.getText().toString() +
                " - joueur2 = " + choixNomJoueur2.getText().toString());
        return true;
    }
}