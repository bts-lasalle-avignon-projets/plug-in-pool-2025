/**
 * @file ActivitePlugInPool.java
 * @brief Déclaration de l'activité principale
 * @author MILLOT Pierre
 */

package com.example.plug_in_pool;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * @class EcranPrincipal
 * @brief L'activité principale
 */
public class ActivitePlugInPool extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_ActivitePlugInPool"; //!< TAG pour les logs (cf. Logcat)

    /**
     * Ressources GUI
     */
    Button boutonConfigurationRencontre;
    Button boutonHistorique;

    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate()");

        initialiserVue();
    }

    /**
     * @brief Méthode appelée au démarrage après le onCreate() ou un restart
     * après un onStop()
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    /**
     * @brief Méthode appelée après onStart() ou après onPause()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    /**
     * @brief Méthode appelée après qu'une boîte de dialogue s'est affichée (on
     * reprend sur un onResume()) ou avant onStop() (activité plus visible)
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    /**
     * @brief Méthode appelée lorsque l'activité n'est plus visible
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    /**
     * @brief Méthode appelée à la destruction de l'application (après onStop()
     * et détruite par le système Android)
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    private void initialiserVue()
    {
        boutonConfigurationRencontre = findViewById(R.id.configuration_rencontre);
        boutonHistorique             = findViewById(R.id.historique);
        allerConfigurerActivite();
        allerHistoriqueActivite();
    }

    private void allerConfigurerActivite()
    {
        boutonConfigurationRencontre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent changerDeVue =
                  new Intent(ActivitePlugInPool.this, ActiviteConfigurationMatch.class);
                startActivity(changerDeVue);
            }
        });
    }

    private void allerHistoriqueActivite()
    {
        boutonHistorique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent changerDeVue = new Intent(ActivitePlugInPool.this, ActiviteHistorique.class);
                startActivity(changerDeVue);
            }
        });
    }
}