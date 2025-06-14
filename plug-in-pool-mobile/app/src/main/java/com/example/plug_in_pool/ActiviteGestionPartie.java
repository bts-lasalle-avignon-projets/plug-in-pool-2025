package com.example.plug_in_pool;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ActiviteGestionPartie extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_ActiviteGestionPartie"; //!< TAG pour les logs
    private static final int    DEMANDE_PERMISSIONS_BLUETOOTH = 1;
    private static final UUID   UUID_BLUETOOTH =
      UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /**
     * Éléments de l'interface
     */
    private TextView             textViewJoueur1;
    private TextView             textViewJoueur2;
    private TextView             texteNbParties;
    private Spinner choixBluetoothEcran;
    private Spinner choixBluetoothTable;

    private Joueur joueur1;
    private Joueur joueur2;
    private String nbParties;
    private String adresseMacEcran;
    private String adresseMacTable;
    private int numeroTableSSID; /* SSID dois être sous la forme : pool-id , avec id en int*/

    /**
     * Ressources GUI
     */
    private Button boutonSuivant;

    /**
     * Bluetooth
     */
    private BluetoothAdapter adaptateurBluetooth;
    public int idMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_partie);
        initialiserVues();
        demanderPermissionsBluetooth();
        configurerBluetooth();
        recupererDonneesDeConfigurations();
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
    }

    void initialiserVues()
    {
        textViewJoueur1     = findViewById(R.id.nomJoueur1);
        textViewJoueur2     = findViewById(R.id.nomJoueur2);
        texteNbParties      = findViewById(R.id.texteNbParties);
        choixBluetoothEcran = findViewById(R.id.spinnerBluetoothEcran);
        choixBluetoothTable = findViewById(R.id.spinnerBluetoothTable);
        boutonSuivant = findViewById(R.id.boutonSuivant);
        lancerUnePartie();
    }

    void recupererDonneesDeConfigurations()
    {
        Intent intent    = getIntent();
        joueur1   = (Joueur)intent.getSerializableExtra("joueur1");
        joueur2   = (Joueur)intent.getSerializableExtra("joueur2");
        nbParties = intent.getSerializableExtra("nbParties").toString();
        idMatch   = intent.getIntExtra("idMatch", -1);

        if(joueur1 != null && joueur2 != null)
        {
            textViewJoueur1.setText(joueur1.getNom() + " " + joueur1.getPrenom());
            textViewJoueur2.setText(joueur2.getNom() + " " + joueur2.getPrenom());
            texteNbParties.setText("Nb partie(s) : " + nbParties);
        }
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
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[] { android.Manifest.permission.BLUETOOTH_CONNECT,
                                android.Manifest.permission.BLUETOOTH_SCAN },
                        DEMANDE_PERMISSIONS_BLUETOOTH);
                return;
            }
            startActivityForResult(activerBluetooth, 1);
        }
    }

    private List<BluetoothDevice> listeAppareils = new ArrayList<>();
    @SuppressLint("ClickableViewAccessibility")
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
        listeAppareils.clear();
        listeAppareils.addAll(appareilsAppaires);

        List<String> nomsAppareils = new ArrayList<>();
        nomsAppareils.add("Sélectionnez un appareil...");
        for (BluetoothDevice appareil : listeAppareils)
        {
            nomsAppareils.add(appareil.getName() + " (" + appareil.getAddress() + ")");
        }

        ArrayAdapter<String> adaptateur = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, nomsAppareils);
        adaptateur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        choixBluetoothEcran.setAdapter(adaptateur);
        choixBluetoothTable.setAdapter(adaptateur);

        choixBluetoothEcran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                {
                    return;
                }
                String selection = (String) parent.getItemAtPosition(position);
                String adresseMac = extraireAdresseMac(selection);
                adresseMacEcran = adresseMac;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        choixBluetoothTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                {
                    return;
                }
                String selection = (String) parent.getItemAtPosition(position);
                String adresseMac = extraireAdresseMac(selection);
                adresseMacTable = adresseMac;
                Log.d(TAG, "adreseeMac : " + adresseMac);
                numeroTableSSID = extraireNumeroTable(selection);
                Log.d(TAG, "numéroTable : " + numeroTableSSID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    private String extraireAdresseMac(String texte)
    {
        return texte.substring(texte.lastIndexOf('(') + 1, texte.length() - 1);
    }
    private int extraireNumeroTable(String texte)
    {
        if (texte == null || !texte.contains("(") || !texte.contains(")"))
        {
            Log.e("extraireNumeroTable", "Format invalide ou null : " + texte);
            return -1;
        }

        int indexParenthese = texte.lastIndexOf('(');
        if (indexParenthese <= 0)
        {
            Log.e("extraireNumeroTable", "Parenthèse mal placée : " + texte);
            return -1;
        }

        String ssid = texte.substring(0, indexParenthese).trim();

        int indexTiret = ssid.lastIndexOf('-');
        if (indexTiret == -1 || indexTiret == ssid.length() - 1)
        {
            Log.e("extraireNumeroTable", "Tiret manquant ou ID vide : " + ssid);
            return -1;
        }

        String idStr = ssid.substring(indexTiret + 1).trim();

        try
        {
            return Integer.parseInt(idStr);
        }
        catch (NumberFormatException e)
        {
            Log.e("extraireNumeroTable", "ID non numérique : " + idStr);
            return -1;
        }
    }

    private void lancerUnePartie()
    {
        boutonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent changerDeVue =
                        new Intent(ActiviteGestionPartie.this, ActivitePartie.class);
                changerDeVue.putExtra("joueur1", joueur1);
                changerDeVue.putExtra("joueur2", joueur2);
                changerDeVue.putExtra("nbParties", nbParties);
                changerDeVue.putExtra("adresseMacEcran", adresseMacEcran);
                changerDeVue.putExtra("adresseMacTable", adresseMacTable);
                changerDeVue.putExtra("idMatch", idMatch);
                changerDeVue.putExtra("numeroTable", numeroTableSSID);
                Log.d(TAG, "idMatch : " + idMatch);
                startActivity(changerDeVue);
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

}