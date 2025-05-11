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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ActivitePartie extends AppCompatActivity
{

    private static final String ADRESSE_MAC_PC_B20 = "00:1A:7D:DA:71:15";
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
    }

    void recupererDonneesDeConfigurations()
    {
        Intent intent    = getIntent();
        adresseMacTable = intent.getStringExtra("adresseMac");
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

        String adresseMac = ADRESSE_MAC_PC_B20; //Pour les tests
        //String adresseMac = adresseMacTable;
        BluetoothDevice peripheriqueBluetooth = adaptateurBluetooth.getRemoteDevice(adresseMac);

        communicationBluetooth = new CommunicationBluetooth(peripheriqueBluetooth);
        communicationBluetooth.start();

        //new Handler().postDelayed(this::envoyerTrame, 2000);
    }

    public void envoyerTrame(String trame)
    {
        if (communicationBluetooth != null)
        {
            Log.d(TAG, "Envoi de la trame : " + trame);
            communicationBluetooth.envoyerTrameAsync(trame);
        }
        else
        {
            Log.e(TAG, "Bluetooth non encore connecté pour envoyer la trame");
        }
    }

    public String trameDemarrerMatch(int etat, int nbParties, String prenomJoueur1, String prenomJoueur2)
    {
        String trame = CommunicationBluetooth.ENTETE + etat + CommunicationBluetooth.SEPARATEUR + nbParties
                + CommunicationBluetooth.SEPARATEUR + prenomJoueur1 + CommunicationBluetooth.SEPARATEUR
                + prenomJoueur2 + CommunicationBluetooth.DELIMITATEUR_FIN;
        return trame;
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