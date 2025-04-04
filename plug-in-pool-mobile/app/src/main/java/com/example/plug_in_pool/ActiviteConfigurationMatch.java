package com.example.plug_in_pool;

import static com.example.plug_in_pool.BlackBall.CONFIGURATION_FINI;

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
import java.util.UUID;

public class ActiviteConfigurationMatch extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_ActiviteConfigurationMatch"; //!< TAG pour les logs (cf. Logcat)
    private static final int DEMANDE_PERMISSIONS_BLUETOOTH = 1;
    private static final UUID UUID_BLUETOOTH = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /**
     * Éléments de l'interface
     */
    private Button boutonLancerMatch;
    private EditText saisieNomJoueur1;
    private EditText saisiePrenomJoueur1;
    private EditText saisieNomJoueur2;
    private EditText saisiePrenomJoueur2;
    private AutoCompleteTextView choixBluetoothEcran;
    private AutoCompleteTextView choixBluetoothTable;

    /**
     * Bluetooth
     */
    private BluetoothSocket socketBluetooth = null;
    private BluetoothDevice peripheriqueBluetooth;
    private BluetoothAdapter adaptateurBluetooth;

    /**
     * @todo Mettre en oeuvre un Vector ou une List
     */
    Joueur joueur1;
    Joueur joueur2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuration_match);

        Log.d(TAG, "onCreate()");

        initialiserVue();
        demanderPermissionsBluetooth();
        configurerBluetooth();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            listerPeripheriquesBluetooth();
        }
        jouerMatch();
    }

    private void initialiserVue()
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
            Log.e(TAG, "Bluetooth non supporté sur cet appareil");
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
            Log.e(TAG, "Bluetooth non disponible !");
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
        {
            Log.e(TAG, "Permissions Bluetooth refusées !");
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

        choixBluetoothEcran.setOnItemClickListener((parent, view, position, id) -> {
            String selection = (String) parent.getItemAtPosition(position);
            String adresseMac = extraireAdresseMac(selection);
            connecterBluetooth(adresseMac);
        });

        choixBluetoothTable.setOnItemClickListener((parent, view, position, id) -> {
            String selection = (String) parent.getItemAtPosition(position);
            String adresseMac = extraireAdresseMac(selection);
            connecterBluetooth(adresseMac);
        });
    }

    private void connecterBluetooth(String adresseMac)
    {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
        {
            Log.e(TAG, "Permissions Bluetooth refusées !");
            return;
        }

        peripheriqueBluetooth = adaptateurBluetooth.getRemoteDevice(adresseMac);
        Log.d(TAG, "Tentative de connexion à l'adresse MAC : " + adresseMac);

        try {
            Log.d(TAG, "Création du socket Bluetooth...");
            socketBluetooth = peripheriqueBluetooth.createInsecureRfcommSocketToServiceRecord(UUID_BLUETOOTH);

            Log.d(TAG, "Socket créée, démarrage de la connexion...");
            socketBluetooth.connect();
            Log.d(TAG, "Connexion Bluetooth réussie à " + adresseMac);
        } catch (IOException e) {
            Log.e(TAG, "Échec de connexion (méthode normale) : " + e.getMessage());

            try {
                Log.d("Bluetooth", "Tentative de connexion alternative...");
                socketBluetooth = (BluetoothSocket) peripheriqueBluetooth.getClass()
                        .getMethod("createRfcommSocket", new Class[]{int.class})
                        .invoke(peripheriqueBluetooth, 1);
                socketBluetooth.connect();
                Log.d(TAG, "Connexion alternative réussie !");
            } catch (Exception ex) {
                Log.e(TAG, "Échec de la connexion alternative : " + ex.getMessage());
            }
        }
    }

    private String extraireAdresseMac(String texte)
    {
        return texte.substring(texte.lastIndexOf('(') + 1, texte.length() - 1);
    }

    private void creerJoueurs()
    {
        String nomJoueur1    = saisieNomJoueur1.getText().toString();
        String prenomJoueur1 = saisiePrenomJoueur1.getText().toString();
        joueur1 = new Joueur(nomJoueur1, prenomJoueur1);

        String nomJoueur2    = saisieNomJoueur2.getText().toString();
        String prenomJoueur2 = saisiePrenomJoueur2.getText().toString();
        joueur2 = new Joueur(nomJoueur2, prenomJoueur2);

        envoyerDonnees(CONFIGURATION_FINI);
    }

    private void envoyerDonnees(int message)
    {
        if (socketBluetooth != null)
        {
            try
            {
                OutputStream fluxSortie = socketBluetooth.getOutputStream();
                fluxSortie.write(message);
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
}