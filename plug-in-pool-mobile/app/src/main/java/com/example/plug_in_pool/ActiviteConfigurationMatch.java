package com.example.plug_in_pool;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class ActiviteConfigurationMatch extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_ActiviteConfigurationMatch"; //!< TAG pour les logs
    private static final String ADRESSE_MAC = "00:E0:4C:63:18:56"; // Module Bluetooth écran
    private static final int DEMANDE_PERMISSIONS_BLUETOOTH = 1;

    private Button boutonLancerMatch;
    private EditText saisieNomJoueur1;
    private EditText saisiePrenomJoueur1;
    private EditText saisieNomJoueur2;
    private EditText saisiePrenomJoueur2;

    private BluetoothSocket socketBluetooth = null;
    private BluetoothDevice peripheriqueBluetooth;
    private BluetoothAdapter adaptateurBluetooth;

    Joueur joueur1;
    Joueur joueur2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuration_match);

        initialiserVues();
        demanderPermissionsBluetooth();
        configurerBluetooth();
    }

    @Override
    protected void onResume() {
        super.onResume();
        connecterPeripherique();
        jouerMatch();
    }

    private void initialiserVues()
    {
        boutonLancerMatch = findViewById(R.id.boutonLancerMatch);
        saisieNomJoueur1 = findViewById(R.id.saisieNomJoueur1);
        saisiePrenomJoueur1 = findViewById(R.id.saisiePrenomJoueur1);
        saisieNomJoueur2 = findViewById(R.id.saisieNomJoueur2);
        saisiePrenomJoueur2 = findViewById(R.id.saisiePrenomJoueur2);
    }

    private void demanderPermissionsBluetooth()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)
        {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.BLUETOOTH_CONNECT,
                        android.Manifest.permission.BLUETOOTH_SCAN
                }, DEMANDE_PERMISSIONS_BLUETOOTH);
            }
        }
    }

    private void configurerBluetooth()
    {
        adaptateurBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (adaptateurBluetooth == null)
        {
            Log.e("Bluetooth", "Bluetooth non supporté sur cet appareil");
            return;
        }
        if (!adaptateurBluetooth.isEnabled())
        {
            Intent activerBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(activerBluetooth, 1);
        }
    }

    private void connecterPeripherique()
    {
        if (adaptateurBluetooth == null)
        {
            Log.e("Bluetooth", "Bluetooth non configuré !");
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("Bluetooth", "Permission BLUETOOTH_CONNECT refusée !");
            return;
        }

        peripheriqueBluetooth = adaptateurBluetooth.getRemoteDevice(ADRESSE_MAC);

        try
        {
            socketBluetooth = peripheriqueBluetooth.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socketBluetooth.connect();
            Log.d("Bluetooth", "Connexion réussie avec " + ADRESSE_MAC);
        }
        catch (IOException e)
        {
            Log.e("Bluetooth", "Échec de connexion : " + e.getMessage());
            try
            {
                if (socketBluetooth != null)
                {
                    socketBluetooth.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("Bluetooth", "Erreur lors de la fermeture du socket : " + ex.getMessage());
            }
        }
    }
    private void envoyerDonnees(String message)
    {
        if (socketBluetooth != null) {
            try
            {
                OutputStream fluxSortie = socketBluetooth.getOutputStream();
                fluxSortie.write(message.getBytes());
                Log.d("Bluetooth", "Données envoyées : " + message);
            } catch (IOException e)
            {
                Log.e("Bluetooth", "Erreur lors de l'envoi", e);
            }
        }
    }
    private void creerJoueurs()
    {
        String nomJoueur1    = saisieNomJoueur1.getText().toString();
        String prenomJoueur1 = saisiePrenomJoueur1.getText().toString();
        joueur1 = new Joueur(nomJoueur1, prenomJoueur1);

        String nomJoueur2    = saisieNomJoueur2.getText().toString();
        String prenomJoueur2 = saisiePrenomJoueur2.getText().toString();
        joueur2 = new Joueur(nomJoueur2, prenomJoueur2);

        envoyerDonnees("Joueurs créés");
    }

    private void jouerMatch()
    {
        boutonLancerMatch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                creerJoueurs();
                Intent changerVue = new Intent(ActiviteConfigurationMatch.this, ActiviteGestionMatch.class);
                changerVue.putExtra("joueur1", joueur1);
                changerVue.putExtra("joueur2", joueur2);
                startActivity(changerVue);
            }
        });
    }
}