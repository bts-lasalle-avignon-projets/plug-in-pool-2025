package com.example.plug_in_pool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActiviteConfigurationMatch extends AppCompatActivity
{
    /**
     * Constantes
     */
    Button boutonLancerUnMatch;

    private static final String TAG = "_ActiviteConfigurationMatch"; //!< TAG pour les logs

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuration_match);
        boutonLancerUnMatch = findViewById(R.id.boutonLancerMatch);
        jouerMatchActivite();
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