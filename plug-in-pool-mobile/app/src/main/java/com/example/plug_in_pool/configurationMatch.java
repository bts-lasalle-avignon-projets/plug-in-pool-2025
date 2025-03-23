package com.example.plug_in_pool;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.time.LocalDateTime;

public class configurationMatch extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuration_match);
    }
    public class Manche
    {
        int numeroTable;
        LocalDateTime horodatage;
        int gagnant;
        int joueurTable;
        boolean couleursDefinies;
        int duree;
    }
}