package com.example.plug_in_pool;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ActivitePartie extends AppCompatActivity
{

    private static final String ADRESSE_MAC_PC_B20 = "00:1A:7D:DA:71:15";
    private static final String ADRESSE_MAC_ESP = "D4:8C:49:69:64:8E";
    private static final String ADRESSE_MAC_SIMULATEUR = "3C:71:BF:6A:F5:D2";
    private String adresseMacTable;
    private static final String TAG = "_ActivitePartie";
    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    private CommunicationBluetooth communicationBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_activite_partie);

        recupererDonneesDeConfigurations();
        verifierEtDemarrerBluetooth();

        /* Test envoie de trame */
        Button boutonEnvoyer = findViewById(R.id.boutonFinMatch);
        boutonEnvoyer.setOnClickListener(v -> {
            String trame = trameDemarrerMatch("D", 3, "Alice", "Pierre");
            envoyerTrame(trame);
        });
    }

    void recupererDonneesDeConfigurations()
    {
        Intent intent = getIntent();
        adresseMacTable = intent.getStringExtra("adresseMac");
        Log.d(TAG, "Adresse MAC récupérer: " + adresseMacTable);
    }

    private void verifierEtDemarrerBluetooth()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                        REQUEST_BLUETOOTH_PERMISSION);
                return;
            }
        }
        initialiserBluetooth();
    }

    private void initialiserBluetooth()
    {
        BluetoothAdapter adaptateurBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (adaptateurBluetooth == null || !adaptateurBluetooth.isEnabled())
        {
            Log.e(TAG, "Bluetooth non disponible ou désactivé");
            return;
        }

        String adresseMac = ADRESSE_MAC_PC_B20; // Pour les tests
        BluetoothDevice peripheriqueBluetooth = adaptateurBluetooth.getRemoteDevice(adresseMac);

        communicationBluetooth = new CommunicationBluetooth(peripheriqueBluetooth);
        communicationBluetooth.start();

        communicationBluetooth.setReceptionListener(message -> {
            runOnUiThread(() -> {
                if (estTrameValide(message))
                {
                    decomposerTrameEmpochage(message);
                }
                else
                {
                    Toast.makeText(ActivitePartie.this, "Trame invalide reçue", Toast.LENGTH_SHORT).show();
                }
            });
        });

        new Handler().postDelayed(() -> {
            if (communicationBluetooth != null && communicationBluetooth.estConnecte())
            {
                String trame = trameEtatPourTable("A");
                envoyerTrame(trame);
                Toast.makeText(this, "Trame envoyée : " + trame, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Bluetooth non connecté, envoi annulé", Toast.LENGTH_LONG).show();
            }
        }, 2000);
    }

    public void envoyerTrame(String trame)
    {
        if (communicationBluetooth != null && communicationBluetooth.estConnecte())
        {
            Log.d(TAG, "Envoi de la trame : " + trame);
            communicationBluetooth.envoyerTrameAsync(trame);
        }
        else
        {
            Log.e(TAG, "Bluetooth non connecté pour envoyer la trame");
        }
    }

    public boolean estTrameValide(String trame)
    {
        if (trame == null)
        {
            return false;
        }
        trame = trame.trim();

        if (!trame.startsWith(CommunicationBluetooth.ENTETE) || !trame.endsWith(CommunicationBluetooth.DELIMITATEUR_FIN))
        {
            return false;
        }

        String contenu = trame.substring(1, trame.length() - 1);
        String[] morceaux = contenu.split("/");

        if (morceaux.length != 2)
        {
            return false;
        }

        try
        {
            Integer.parseInt(morceaux[0]);
            Integer.parseInt(morceaux[1]);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    public void decomposerTrameEmpochage(String trame)
    {
        if (trame == null)
        {
            return;
        }

        trame = trame.trim();
        String contenu = trame.substring(1, trame.length() - 1);
        String[] morceaux = contenu.split("/");

        try
        {
            int couleur = Integer.parseInt(morceaux[0]);
            int poche = Integer.parseInt(morceaux[1]);

            Log.d(TAG, "Couleur : " + couleur + ", Poche : " + poche);
            Toast.makeText(this, "Trame reçue : Couleur " + couleur + " | Poche " + poche, Toast.LENGTH_LONG).show();
        }
        catch (NumberFormatException e)
        {
            Log.e(TAG, "Erreur de format inattendue dans une trame pourtant validée");
        }
    }

    public String trameEtatPourTable(String etat)
    {
        return CommunicationBluetooth.ENTETE + etat + CommunicationBluetooth.DELIMITATEUR_FIN;
    }
    public String trameDemarrerMatch(String type, int nbParties, String prenomJoueur1,
                                     String prenomJoueur2)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR + nbParties
                + CommunicationBluetooth.SEPARATEUR + prenomJoueur1 +
                CommunicationBluetooth.SEPARATEUR + prenomJoueur2 +
                CommunicationBluetooth.DELIMITATEUR_FIN;
    }
    public String trameCasse(String type, int idPartie, int idJoueur, int couleurBille, int idPoche)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR + idPartie
                + CommunicationBluetooth.SEPARATEUR + idJoueur + CommunicationBluetooth.SEPARATEUR
                + couleurBille + CommunicationBluetooth.SEPARATEUR + idPoche
                + CommunicationBluetooth.DELIMITATEUR_FIN;
    }
    public String trameEmpochage(String type, int couleurBille, int idPoche)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR
                + couleurBille + CommunicationBluetooth.SEPARATEUR + idPoche
                + CommunicationBluetooth.DELIMITATEUR_FIN;
    }
    public String trameFaute(String type, int idJoueur, String faute)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR + idJoueur
                + CommunicationBluetooth.SEPARATEUR + faute
                + CommunicationBluetooth.DELIMITATEUR_FIN;
    }
    public String trameFinDePartie(String type, int idPartie, int idJoueur)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR + idPartie
                + CommunicationBluetooth.SEPARATEUR + idJoueur
                + CommunicationBluetooth.DELIMITATEUR_FIN;
    }
    public String trameFinDeMatch(String type, int nbPartiesJoueur1, int nbPartiesJoueur2)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR
                + nbPartiesJoueur1 + CommunicationBluetooth.SEPARATEUR + nbPartiesJoueur2
                + CommunicationBluetooth.DELIMITATEUR_FIN;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (communicationBluetooth != null)
        {
            communicationBluetooth.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION)
        {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                initialiserBluetooth();
            }
            else
            {
                Log.e(TAG, "Permission Bluetooth refusée");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}