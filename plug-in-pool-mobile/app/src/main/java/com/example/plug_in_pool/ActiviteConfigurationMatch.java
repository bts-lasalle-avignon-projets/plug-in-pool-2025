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
    private static final int  DEMANDE_PERMISSIONS_BLUETOOTH = 1;
    private static final UUID UUID_BLUETOOTH =
      UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /**
     * Éléments de l'interface
     */
    private Button               boutonLancerMatch;
    private AutoCompleteTextView choixNomJoueur1;
    private AutoCompleteTextView choixNomJoueur2;
    private AutoCompleteTextView choixBluetoothEcran;
    private AutoCompleteTextView choixBluetoothTable;

    /**
     * Bluetooth
     */
    private BluetoothSocket  socketBluetooth = null;
    private BluetoothDevice  peripheriqueBluetooth;
    private BluetoothAdapter adaptateurBluetooth;

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

        demanderPermissionsBluetooth();
        configurerBluetooth();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume()");

        if(ActivityCompat.checkSelfPermission(this,
                                              android.Manifest.permission.BLUETOOTH_CONNECT) ==
           PackageManager.PERMISSION_GRANTED)
        {
            listerPeripheriquesBluetooth();
        }

        joueurs.clear();
    }

    private void initialiserVue()
    {
        Log.d(TAG, "initialiserVue()");
        boutonLancerMatch   = findViewById(R.id.boutonLancerMatch);
        choixNomJoueur1     = findViewById(R.id.choixNomJoueur1);
        choixNomJoueur2     = findViewById(R.id.choixNomJoueur2);
        choixBluetoothEcran = findViewById(R.id.choixBluetoothEcran);
        choixBluetoothTable = findViewById(R.id.choixBluetoothTable);

        baseDonnees = BaseDeDonnees.getInstance(this);

        initialiserListeJoueurs();
        jouerMatch();
    }

    private void demanderPermissionsBluetooth()
    {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)
        {
            if(ActivityCompat.checkSelfPermission(this,
                                                  android.Manifest.permission.BLUETOOTH_CONNECT) !=
                 PackageManager.PERMISSION_GRANTED ||
               ActivityCompat.checkSelfPermission(this,
                                                  android.Manifest.permission.BLUETOOTH_SCAN) !=
                 PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(
                  this,
                  new String[] { android.Manifest.permission.BLUETOOTH_CONNECT,
                                 android.Manifest.permission.BLUETOOTH_SCAN },
                  DEMANDE_PERMISSIONS_BLUETOOTH);
            }
        }
    }

    private void configurerBluetooth()
    {
        adaptateurBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(adaptateurBluetooth == null)
        {
            Log.e(TAG, "Bluetooth non supporté sur cet appareil");
            return;
        }
        if(!adaptateurBluetooth.isEnabled())
        {
            Intent activerBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(activerBluetooth, 1);
        }
    }

    private void listerPeripheriquesBluetooth()
    {
        if(adaptateurBluetooth == null)
        {
            Log.e(TAG, "Bluetooth non disponible !");
            return;
        }

        if(ActivityCompat.checkSelfPermission(this,
                                              android.Manifest.permission.BLUETOOTH_CONNECT) !=
           PackageManager.PERMISSION_GRANTED)
        {
            Log.e(TAG, "Permissions Bluetooth refusées !");
            return;
        }

        Set<BluetoothDevice> appareilsAppaires = adaptateurBluetooth.getBondedDevices();
        List<String>         nomsAppareils     = new ArrayList<>();

        for(BluetoothDevice appareil: appareilsAppaires)
        {
            nomsAppareils.add(appareil.getName() + " (" + appareil.getAddress() + ")");
        }

        ArrayAdapter<String> adaptateur =
          new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nomsAppareils);
        choixBluetoothEcran.setAdapter(adaptateur);
        choixBluetoothTable.setAdapter(adaptateur);

        choixBluetoothEcran.setOnItemClickListener((parent, view, position, id) -> {
            String selection  = (String)parent.getItemAtPosition(position);
            String adresseMac = extraireAdresseMac(selection);
            connecterBluetooth(adresseMac);
        });

        choixBluetoothTable.setOnItemClickListener((parent, view, position, id) -> {
            String selection  = (String)parent.getItemAtPosition(position);
            String adresseMac = extraireAdresseMac(selection);
            connecterBluetooth(adresseMac);
        });
    }

    private void connecterBluetooth(String adresseMac)
    {
        if(ActivityCompat.checkSelfPermission(this,
                                              android.Manifest.permission.BLUETOOTH_CONNECT) !=
           PackageManager.PERMISSION_GRANTED)
        {
            Log.e(TAG, "Permissions Bluetooth refusées !");
            return;
        }

        peripheriqueBluetooth = adaptateurBluetooth.getRemoteDevice(adresseMac);
        Log.d(TAG, "Tentative de connexion à l'adresse MAC : " + adresseMac);

        try
        {
            Log.d(TAG, "Création du socket Bluetooth...");
            socketBluetooth =
              peripheriqueBluetooth.createInsecureRfcommSocketToServiceRecord(UUID_BLUETOOTH);

            Log.d(TAG, "Socket créée, démarrage de la connexion...");
            socketBluetooth.connect();
            Log.d(TAG, "Connexion Bluetooth réussie à " + adresseMac);
        }
        catch(IOException e)
        {
            Log.e(TAG, "Échec de connexion (méthode normale) : " + e.getMessage());

            try
            {
                Log.d("Bluetooth", "Tentative de connexion alternative...");
                socketBluetooth = (BluetoothSocket)peripheriqueBluetooth.getClass()
                                    .getMethod("createRfcommSocket", new Class[] { int.class })
                                    .invoke(peripheriqueBluetooth, 1);
                socketBluetooth.connect();
                Log.d(TAG, "Connexion alternative réussie !");
            }
            catch(Exception ex)
            {
                Log.e(TAG, "Échec de la connexion alternative : " + ex.getMessage());
            }
        }
    }

    private String extraireAdresseMac(String texte)
    {
        return texte.substring(texte.lastIndexOf('(') + 1, texte.length() - 1);
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

    private boolean ajouterJoueur1()
    {
        String nomJoueur       = choixNomJoueur1.getText().toString().trim();
        String[] donneesJoueur = nomJoueur.split(" ");
        if(donneesJoueur.length == 2)
        {
            String prenom = donneesJoueur[0];
            String nom    = donneesJoueur[1];
            Joueur joueur = new Joueur(nom, prenom);
            joueurs.add(joueur);
            Log.d(TAG, "ajouterJoueur1() " + joueur.getPrenom() + " " + joueur.getNom());
            baseDonnees.ajouterJoueur(nom, prenom);
            return true;
        }

        return false;
    }

    private boolean ajouterJoueur2()
    {
        String nomJoueur       = choixNomJoueur2.getText().toString().trim();
        String[] donneesJoueur = nomJoueur.split(" ");
        if(donneesJoueur.length == 2)
        {
            String prenom = donneesJoueur[0];
            String nom    = donneesJoueur[1];
            Joueur joueur = new Joueur(nom, prenom);
            joueurs.add(joueur);
            Log.d(TAG, "ajouterJoueur2() " + joueur.getPrenom() + " " + joueur.getNom());
            baseDonnees.ajouterJoueur(nom, prenom);
            return true;
        }

        return false;
    }

    private void jouerMatch()
    {
        boutonLancerMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(estMatchConfigure())
                {
                    Log.d(TAG,
                          "jouerMatch() " + choixNomJoueur1.getText().toString() + " vs " +
                            choixNomJoueur2.getText().toString());
                    Log.d(TAG, "jouerMatch() écran : " + choixBluetoothEcran.getText().toString());
                    Log.d(TAG, "jouerMatch() table : " + choixBluetoothTable.getText().toString());

                    ajouterJoueur1();
                    ajouterJoueur2();
                    Log.d(TAG, "jouerMatch() nbJoueurs : " + joueurs.size());
                    Intent changerVue =
                      new Intent(ActiviteConfigurationMatch.this, ActiviteGestionMatch.class);
                    changerVue.putExtra("joueur1", joueurs.elementAt(0));
                    changerVue.putExtra("joueur2", joueurs.elementAt(1));
                    startActivity(changerVue);
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