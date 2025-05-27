package com.example.plug_in_pool;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActiviteHistorique extends AppCompatActivity
{
    private BaseDeDonnees baseDeDonnees;
    private ListView listeMatchs;
    private Button boutonPurger;
    private Button retourAccueil;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        baseDeDonnees = BaseDeDonnees.getInstance(this);

        initialiserVues();
        purgerHistorique();
        changerDeVue();
        afficherHistorique();
    }

    public void initialiserVues()
    {
        listeMatchs   = findViewById(R.id.listeMatchs);
        boutonPurger  = findViewById(R.id.boutonPurger);
        retourAccueil = findViewById(R.id.retourAccueil);
    }

    public void afficherHistorique()
    {
        ArrayList<String> infosMatchs = baseDeDonnees.getInfosMatchs();
        ArrayAdapter<String> adaptateur = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                infosMatchs
        );
        listeMatchs.setAdapter(adaptateur);
    }

    public void purgerHistorique()
    {
        boutonPurger.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(ActiviteHistorique.this)
                        .setTitle("Confirmation")
                        .setMessage("Voulez-vous vraiment supprimer tout l’historique des matchs ?")
                        .setPositiveButton("Oui", (dialog, which) -> {
                            baseDeDonnees.purgerHistorique();
                            afficherHistorique();
                        })
                        .setNegativeButton("Annuler", null)
                        .show();
            }
        });
    }

    public void changerDeVue()
    {
        retourAccueil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent changerDeVue = new Intent(ActiviteHistorique.this, ActivitePlugInPool.class);
                startActivity(changerDeVue);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}