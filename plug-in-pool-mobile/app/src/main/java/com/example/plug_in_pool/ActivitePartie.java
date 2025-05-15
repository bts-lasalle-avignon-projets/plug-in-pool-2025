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
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Vector;

public class ActivitePartie extends AppCompatActivity
{

    private static final String TAG = "_ActivitePartie";

    private static final String ADRESSE_MAC_PC_B20 = "00:1A:7D:DA:71:15";
    private static final String ADRESSE_MAC_ECRAN = "00:E0:4C:6D:20:45";

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;

    private CommunicationBluetooth communicationBluetoothEmission;
    private CommunicationBluetooth communicationBluetoothReception;

    private BluetoothDevice deviceEmission;
    private BluetoothDevice deviceReception;

    /**
     * Éléments de l'interface
     */
    Joueur joueur1;
    Joueur joueur2;
    String nbParties;

    int idGagnant;
    List<Joueur> joueurs = new ArrayList<>();
    int trameCouleurRecue;
    int tramePocheRecue;
    boolean partieFinie = false;

    private TextView texteJoueur1;
    private TextView texteJoueur2;
    private TextView nbPointsJoueur1;
    private TextView nbPointsJoueur2;
    private TextView joueurActuel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_activite_partie);
        initialiserVues();
        recupererDonneesDeConfigurations();
        verifierEtDemarrerBluetooth();
    }
    void initialiserVues()
    {
        texteJoueur1     = findViewById(R.id.textePourNbPointsJoueur1);
        texteJoueur2     = findViewById(R.id.textePourNbPointsJoueur2);
        nbPointsJoueur1  = findViewById(R.id.nbPointsDuJoueur1);
        nbPointsJoueur2  = findViewById(R.id.nbPointsDuJoueur2);
        joueurActuel     = findViewById(R.id.nomDuJoueur);
        preparerLeMatch();
    }
    void recupererDonneesDeConfigurations()
    {
        Intent intent = getIntent();
        joueur1   = (Joueur)intent.getSerializableExtra("joueur1");
        Log.d(TAG, "recupererDonneesDeConfigurations: " + joueur1);
        joueur2   = (Joueur)intent.getSerializableExtra("joueur2");
        Log.d(TAG, "recupererDonneesDeConfigurations: " + joueur2);
        nbParties = intent.getSerializableExtra("nbParties").toString();

        if(joueur1 != null && joueur2 != null)
        {
            texteJoueur1.setText(joueur1.getNom() + " " + joueur1.getPrenom());
            texteJoueur2.setText(joueur2.getNom() + " " + joueur2.getPrenom());
        }
    }
    private void preparerLeMatch()
    {
        Button boutonDemarrer = findViewById(R.id.boutonDemarerMatch);
        boutonDemarrer.setOnClickListener(v -> {
            initialiserJoueurs();
        });
    }
    private void initialiserJoueurs()
    {
        joueur1 = new Joueur(joueur1.getNom(), joueur1.getPrenom(), 0, 0);
        joueur2 = new Joueur(joueur2.getNom(), joueur2.getPrenom(), 0,1);
        joueurs.add(joueur1);
        joueurs.add(joueur2);

        String envoyerConfigurationVersEcran =
                trameDemarrerMatch(CommunicationBluetooth.DEMARER_MATCH ,
                        Integer.parseInt(nbParties), joueur1.getPrenom(), joueur2.getPrenom());
        envoyerTrame(envoyerConfigurationVersEcran);

    }
    private void nbPartie()
    {
        for (int i = 0; Integer.parseInt(nbParties) > i; i++)
        {
            lancerPartie();

        }
    }
    private void nbPartiesGagner()
    {
        if (idGagnant == 0)
        {
            joueur1.ajouterPoint();
        }
        else if (idGagnant == 1)
        {
            joueur2.ajouterPoint();
        }
        else
        {
        }
    }
    private void lancerPartie()
    {
        boolean casseFinie = false;
        while (casseFinie =! true)
        {
            casseFinie = casse();
        }
        manche();
        finPartie();
    }
    private boolean casse()
    {
        for (Joueur joueur : joueurs)
        {
            joueur.afficherJoueur();

            int id = joueur.getId();
            if (id == 0)
            {
                Log.d(TAG, "Attente empochage joueur 1");
                demarrerReceptionBluetooth();
            }
            else if (id == 1)
            {
                Log.d(TAG, "Attente empochage joueur 2");
                demarrerReceptionBluetooth();
            }
            else
            {
            }
        }
        return true;
    }
    private void manche()
    {
        for (Joueur joueur : joueurs)
        {
            joueur.afficherJoueur();

            int id = joueur.getId();
            if (id == 0)
            {
                Log.d(TAG, "Attente empochage joueur 1");
                demarrerReceptionBluetooth();
            }
            else if (id == 1)
            {
                Log.d(TAG, "Attente empochage joueur 2");
                demarrerReceptionBluetooth();
            }
            else
            {
            }
        }
    }
    private void finPartie()
    {
        nbPartiesGagner();
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

        deviceEmission = adaptateurBluetooth.getRemoteDevice(ADRESSE_MAC_ECRAN);
        communicationBluetoothEmission = new CommunicationBluetooth(deviceEmission);
        communicationBluetoothEmission.start();

        deviceReception = adaptateurBluetooth.getRemoteDevice(ADRESSE_MAC_PC_B20);
        communicationBluetoothReception = new CommunicationBluetooth(deviceReception);
        communicationBluetoothReception.setReceptionListener(message -> {
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
        communicationBluetoothReception.start();

        new Handler().postDelayed(() -> {
            if (communicationBluetoothEmission != null && communicationBluetoothEmission.estConnecte())
            {
                Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Bluetooth (écran) non connecté, envoi annulé", Toast.LENGTH_LONG).show();
            }
        }, 2000);
    }
    private void demarrerReceptionBluetooth()
    {
        BluetoothAdapter adaptateurBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (adaptateurBluetooth == null || !adaptateurBluetooth.isEnabled())
        {
            Log.e(TAG, "Bluetooth non disponible ou désactivé");
            return;
        }

        BluetoothDevice appareilReception = adaptateurBluetooth.getRemoteDevice(ADRESSE_MAC_PC_B20);
        CommunicationBluetooth receptionBluetooth = new CommunicationBluetooth(appareilReception);

        receptionBluetooth.setReceptionListener(message -> {
            runOnUiThread(() -> {
                if (estTrameValide(message))
                {
                    decomposerTrameEmpochage(message);
                }
                else
                {
                    Toast.makeText(ActivitePartie.this,
                            "Trame invalide reçue",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        receptionBluetooth.start();

        new Handler().postDelayed(() -> {
            if (receptionBluetooth.estConnecte())
            {
                Toast.makeText(this,
                        "Réception Bluetooth connectée",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,
                        "Réception Bluetooth non connectée",
                        Toast.LENGTH_LONG).show();
            }
        }, 2000);
    }

    public void envoyerTrame(String trame)
    {
        if (communicationBluetoothEmission != null && communicationBluetoothEmission.estConnecte())
        {
            Log.d(TAG, "Envoi de la trame : " + trame);
            communicationBluetoothEmission.envoyerTrameAsync(trame);
        }
        else
        {
            Log.e(TAG, "Bluetooth (écran) non connecté pour envoyer la trame");
        }
    }

    public boolean estTrameValide(String trame)
    {
        if (trame == null)
        {
            return false;
        }

        trame = trame.trim();
        if (!trame.startsWith(CommunicationBluetooth.ENTETE) ||
                !trame.endsWith(CommunicationBluetooth.DELIMITATEUR_FIN))
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
            trameCouleurRecue = Integer.parseInt(morceaux[0]);
            tramePocheRecue = Integer.parseInt(morceaux[1]);

            Log.d(TAG, "Couleur : " + trameCouleurRecue + ", Poche : " + tramePocheRecue);
            Toast.makeText(this, "Trame reçue : Couleur " + trameCouleurRecue +
                    " | Poche " + tramePocheRecue, Toast.LENGTH_LONG).show();
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

    public String trameDemarrerMatch(String type, int nbParties, String prenomJoueur1, String prenomJoueur2)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR + nbParties
                + CommunicationBluetooth.SEPARATEUR + prenomJoueur1 + CommunicationBluetooth.SEPARATEUR + prenomJoueur2
                + CommunicationBluetooth.DELIMITATEUR_FIN;
    }

    public String trameCasse(String type, int idPartie, int idJoueur, int couleurBille, int idPoche)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR + idPartie
                + CommunicationBluetooth.SEPARATEUR + idJoueur + CommunicationBluetooth.SEPARATEUR + couleurBille
                + CommunicationBluetooth.SEPARATEUR + idPoche + CommunicationBluetooth.DELIMITATEUR_FIN;
    }

    public String trameEmpochage(String type, int couleurBille, int idPoche)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR + couleurBille
                + CommunicationBluetooth.SEPARATEUR + idPoche + CommunicationBluetooth.DELIMITATEUR_FIN;
    }

    public String trameFaute(String type, int idJoueur, String faute)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR + idJoueur
                + CommunicationBluetooth.SEPARATEUR + faute + CommunicationBluetooth.DELIMITATEUR_FIN;
    }

    public String trameFinDePartie(String type, int idPartie, int idJoueur)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR + idPartie
                + CommunicationBluetooth.SEPARATEUR + idJoueur + CommunicationBluetooth.DELIMITATEUR_FIN;
    }

    public String trameFinDeMatch(String type, int nbPartiesJoueur1, int nbPartiesJoueur2)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR + nbPartiesJoueur1
                + CommunicationBluetooth.SEPARATEUR + nbPartiesJoueur2 + CommunicationBluetooth.DELIMITATEUR_FIN;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (communicationBluetoothEmission != null) communicationBluetoothEmission.close();
        if (communicationBluetoothReception != null) communicationBluetoothReception.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
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