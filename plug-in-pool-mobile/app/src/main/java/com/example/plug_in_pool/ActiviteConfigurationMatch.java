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
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
=======
import java.util.UUID;
>>>>>>> df4c786b22eb5b1d470594d7c8124ae9a4d00789

public class ActiviteConfigurationMatch extends AppCompatActivity
{
    /**
     * Constantes
     */
<<<<<<< HEAD
=======
    private static final String TAG = "_ActiviteConfigurationMatch"; //!< TAG pour les logs
    private static final String ADRESSE_MAC = "00:E0:4C:63:18:56"; // Module Bluetooth écran
>>>>>>> df4c786b22eb5b1d470594d7c8124ae9a4d00789
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
<<<<<<< HEAD
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            listerPeripheriquesBluetooth();
        }
=======
        connecterPeripherique();
>>>>>>> df4c786b22eb5b1d470594d7c8124ae9a4d00789
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

<<<<<<< HEAD
        Set<BluetoothDevice> appareilsAppaires = adaptateurBluetooth.getBondedDevices();
        List<String> nomsAppareils = new ArrayList<>();
=======
        peripheriqueBluetooth = adaptateurBluetooth.getRemoteDevice(ADRESSE_MAC);
>>>>>>> df4c786b22eb5b1d470594d7c8124ae9a4d00789

        for (BluetoothDevice appareil : appareilsAppaires)
        {
<<<<<<< HEAD
            nomsAppareils.add(appareil.getName() + " (" + appareil.getAddress() + ")");
=======
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
>>>>>>> df4c786b22eb5b1d470594d7c8124ae9a4d00789
        }

        ArrayAdapter<String> adaptateur = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nomsAppareils);
        choixBluetoothEcran.setAdapter(adaptateur);
        choixBluetoothTable.setAdapter(adaptateur);
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

<<<<<<< HEAD
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
=======
    private void jouerMatch()
    {
>>>>>>> df4c786b22eb5b1d470594d7c8124ae9a4d00789
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