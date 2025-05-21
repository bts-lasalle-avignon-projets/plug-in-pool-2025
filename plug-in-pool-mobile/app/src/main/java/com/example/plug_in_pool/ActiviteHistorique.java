package com.example.plug_in_pool;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActiviteHistorique extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_ActiviteHistorique"; //!< TAG pour les logs

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historique);
    }
}