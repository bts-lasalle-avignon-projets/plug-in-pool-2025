package com.example.plug_in_pool;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ActiviteConfigurationMatch extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final int DEMANDE_PERMISSIONS_BLUETOOTH = 1;

    private Button boutonLancerMatch;
    private EditText saisieNomJoueur1;
    private EditText saisiePrenomJoueur1;
    private EditText saisieNomJoueur2;
    private EditText saisiePrenomJoueur2;
    private AutoCompleteTextView choixBluetoothEcran;
    private AutoCompleteTextView choixBluetoothTable;

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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            listerPeripheriquesBluetooth();
        }
        jouerMatch();
    }

    private void initialiserVues()
    {
        boutonLancerMatch = findViewById(R.id.boutonLancerMatch);
        saisieNomJoueur1 = findViewById(R.id.saisieNomJoueur1);
        saisiePrenomJoueur1 = findViewById(R.id.saisiePrenomJoueur1);
        saisieNomJoueur2 = findViewById(R.id.saisieNomJoueur2);
        saisiePrenomJoueur2 = findViewById(R.id.saisiePrenomJoueur2);
        choixBluetoothEcran = findViewById(R.id.choixBluetoothEcran);
        choixBluetoothTable = findViewById(R.id.choixBluetoothTable);
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

    private void listerPeripheriquesBluetooth()
    {
        if (adaptateurBluetooth == null)
        {
            Log.e("Bluetooth", "Bluetooth non disponible !");
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("Bluetooth", "Permissions Bluetooth refusées !");
            return;
        }

        Set<BluetoothDevice> appareilsAppaires = adaptateurBluetooth.getBondedDevices();
        List<String> nomsAppareils = new ArrayList<>();

        for (BluetoothDevice appareil : appareilsAppaires)
        {
            nomsAppareils.add(appareil.getName() + " (" + appareil.getAddress() + ")");
        }

        ArrayAdapter<String> adaptateur = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nomsAppareils);
        choixBluetoothEcran.setAdapter(adaptateur);
        choixBluetoothTable.setAdapter(adaptateur);
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

    private void envoyerDonnees(String message)
    {
        if (socketBluetooth != null)
        {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == DEMANDE_PERMISSIONS_BLUETOOTH)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.d("Bluetooth", "Permission Bluetooth accordée");
                listerPeripheriquesBluetooth();
            }
            else
            {
                Log.e("Bluetooth", "Permission Bluetooth refusée");
            }
        }
    }
}