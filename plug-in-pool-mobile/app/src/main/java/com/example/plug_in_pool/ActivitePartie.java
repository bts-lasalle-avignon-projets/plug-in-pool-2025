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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class ActivitePartie extends AppCompatActivity
{
    private static final String TAG = "_ActivitePartie";

    private static final String ADRESSE_MAC_PC_B20 = "00:1A:7D:DA:71:15";
    private static final String ADRESSE_MAC_ECRAN  = "00:E0:4C:6D:20:45"; //Raspberry pi : 2C:CF:67:94:F2:3D

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;

    private CommunicationBluetooth communicationBluetoothEmission;
    private CommunicationBluetooth communicationBluetoothReception;

    private BluetoothDevice deviceEmission;
    private BluetoothDevice deviceReception;

    /**
     * Éléments de l'interface
     */
    private Joueur       joueur1;
    private Joueur       joueur2;
    private String       nbParties;
    private CouleurBille couleurBille;
    private Match match;

    int          idGagnant;
    List<Joueur> joueurs           = new ArrayList<>();
    int          trameCouleurRecue = -2;
    int          tramePocheRecue;
    boolean      partieFinie = false;
    int          nbPartiesGestion;
    int          indexBilleRouge = 0;
    int          indexBilleJaune = 0;

    private TextView texteJoueur1;
    private TextView texteJoueur2;
    private TextView afficherPointsJoueur1;
    private TextView afficherPointsJoueur2;
    private TextView joueurActuel;
    private ImageView[] billesJaunes = new ImageView[7];
    private ImageView[] billesRouges = new ImageView[7];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_activite_partie);
        match = new Match("Match", nbPartiesGestion);
        match.setEtat(Match.EtatMatch.CREE);
        initialiserVues();
        recupererDonneesDeConfigurations();
        verifierEtDemarrerBluetooth();
    }
    void initialiserVues()
    {
        afficherPointsJoueur1 = findViewById(R.id.nbPointsDuJoueur1);
        afficherPointsJoueur2 = findViewById(R.id.nbPointsDuJoueur2);
        texteJoueur1    = findViewById(R.id.textePourNbPointsJoueur1);
        texteJoueur2    = findViewById(R.id.textePourNbPointsJoueur2);
        joueurActuel    = findViewById(R.id.nomDuJoueur);
        for(int i = 0; i < 6; i++)
        {
            int resID =
              getResources().getIdentifier("billeJaune" + (i + 1), "id", getPackageName());
            billesJaunes[i] = findViewById(resID);
        }
        for(int j = 0; j < 6; j++)
        {
            int resID =
              getResources().getIdentifier("billeRouge" + (j + 1), "id", getPackageName());
            billesRouges[j] = findViewById(resID);
        }
        preparerLeMatch();
    }
    void getNbParties()
    {
        if(nbParties != null && !nbParties.isEmpty())
        {
            try
            {
                nbPartiesGestion = Integer.parseInt(nbParties);
            }
            catch(NumberFormatException e)
            {
                nbPartiesGestion = 0;
                System.err.println("Valeur invalide pour nbParties : " + nbParties);
            }
        }
        else
        {
            nbPartiesGestion = 0;
            System.err.println("nbParties est null ou vide.");
        }
    }

    void recupererDonneesDeConfigurations()
    {
        Intent intent = getIntent();
        joueur1       = (Joueur)intent.getSerializableExtra("joueur1");
        Log.d(TAG, "recupererDonneesDeConfigurations: " + joueur1);
        joueur2 = (Joueur)intent.getSerializableExtra("joueur2");
        Log.d(TAG, "recupererDonneesDeConfigurations: " + joueur2);
        nbParties = intent.getSerializableExtra("nbParties").toString();

        if(joueur1 != null && joueur2 != null)
        {
            texteJoueur1.setText(joueur1.getNom() + " " + joueur1.getPrenom());
            texteJoueur2.setText(joueur2.getNom() + " " + joueur2.getPrenom());
        }
        getNbParties();
    }
    private void preparerLeMatch()
    {
        Button boutonDemarrer = findViewById(R.id.boutonDemarerMatch);
        boutonDemarrer.setOnClickListener(v -> { initialiserJoueurs(); });
    }
    private void initialiserJoueurs()
    {
        joueur1 = new Joueur(joueur1.getNom(), joueur1.getPrenom(), 0, 0);
        joueur2 = new Joueur(joueur2.getNom(), joueur2.getPrenom(), 0, 1);
        joueurs.add(joueur1);
        joueurs.add(joueur2);

        String envoyerConfigurationVersEcran =
          trameDemarrerMatch(CommunicationBluetooth.DEMARER_MATCH,
                             Integer.parseInt(nbParties),
                             joueur1.getPrenom(),
                             joueur2.getPrenom());
        envoyerTrame(envoyerConfigurationVersEcran);
    }
    private void casse()
    {
        boolean casseEstFini = false;
        if(!casseEstFini)
        {
            for(Joueur joueur: joueurs)
            {
                joueur.afficherJoueur();

                int id = joueur.getId();
                if(id == 0)
                {
                    Log.d(TAG, "Attente empochage joueur 1");
                    switch(trameCouleurRecue)
                    {
                        case 0:
                            retirerBilleRouge();
                            joueur.setCouleur(CouleurBille.ROUGE);
                            joueurs.get(1).setCouleur(CouleurBille.JAUNE);
                            String envoyerRougeVersEcran =
                              trameCasse("C",
                                         (nbPartiesGestion - nbPartiesGestion + 1),
                                         joueur.getId(),
                                         trameCouleurRecue,
                                         tramePocheRecue);
                            envoyerTrame(envoyerRougeVersEcran);
                            casseEstFini = true;
                            joueurActuel.setText(joueur2.getNom() + " " + joueur2.getPrenom());
                            joueurs.get(0).ajouterPoint();
                            afficherPointsJoueur1.setText(String.valueOf(joueurs.get(0).afficherPoint()));
                            afficherJoueursCreer();
                            break;
                        case 1:
                            retirerBilleJaune();
                            joueur.setCouleur(CouleurBille.JAUNE);
                            joueurs.get(1).setCouleur(CouleurBille.ROUGE);
                            String envoyerJauneVersEcran =
                              trameCasse("C",
                                         (nbPartiesGestion - nbPartiesGestion + 1),
                                         joueur.getId(),
                                         trameCouleurRecue,
                                         tramePocheRecue);
                            envoyerTrame(envoyerJauneVersEcran);
                            casseEstFini = true;
                            joueurActuel.setText(joueur2.getNom() + " " + joueur2.getPrenom());
                            joueurs.get(0).ajouterPoint();
                            afficherPointsJoueur1.setText(String.valueOf(joueurs.get(0).afficherPoint()));
                            afficherJoueursCreer();
                            break;
                        case 3:
                            String envoyerFauteNoireVersEcran = trameFaute("F", joueurs.get(0).getId(), "Noire");
                            envoyerTrame(envoyerFauteNoireVersEcran);
                            joueurs.get(1).ajouterPoint();
                            match.setEtat(Match.EtatMatch.FINI);
                            return;
                        default:
                            String envoyerFauteVersEcran = trameFaute("F", id, "");
                            envoyerTrame(envoyerFauteVersEcran);
                            joueurActuel.setText(joueur2.getNom() + " " + joueur2.getPrenom());
                            break;
                    }
                }
                else if(id == 1)
                {
                    Log.d(TAG, "Attente empochage joueur 2");
                    switch(trameCouleurRecue)
                    {
                        case 0:
                            joueur.setCouleur(CouleurBille.ROUGE);
                            joueurs.get(0).setCouleur(CouleurBille.JAUNE);
                            retirerBilleRouge();
                            String envoyerRougeVersEcran =
                              trameCasse("C",
                                         (nbPartiesGestion - nbPartiesGestion + 1),
                                         joueur.getId(),
                                         trameCouleurRecue,
                                         tramePocheRecue);
                            envoyerTrame(envoyerRougeVersEcran);
                            casseEstFini = true;
                            joueurActuel.setText(joueur1.getNom() + " " + joueur1.getPrenom());
                            joueurs.get(1).ajouterPoint();
                            afficherPointsJoueur2.setText(String.valueOf(joueurs.get(1).afficherPoint()));
                            afficherJoueursCreer();
                            break;
                        case 1:
                            retirerBilleJaune();
                            joueur.setCouleur(CouleurBille.JAUNE);
                            joueurs.get(0).setCouleur(CouleurBille.ROUGE);
                            String envoyerJauneVersEcran =
                              trameCasse("C",
                                         (nbPartiesGestion - nbPartiesGestion + 1),
                                         joueur.getId(),
                                         trameCouleurRecue,
                                         tramePocheRecue);
                            envoyerTrame(envoyerJauneVersEcran);
                            casseEstFini = true;
                            joueurActuel.setText(joueur1.getNom() + " " + joueur1.getPrenom());
                            joueurs.get(1).ajouterPoint();
                            afficherPointsJoueur2.setText(String.valueOf(joueurs.get(1).afficherPoint()));
                            afficherJoueursCreer();
                            break;
                        case 3:
                            String envoyerFauteNoireVersEcran = trameFaute("F", joueurs.get(1).getId(), "Noire");
                            envoyerTrame(envoyerFauteNoireVersEcran);
                            joueurs.get(0).ajouterPoint();
                            match.setEtat(Match.EtatMatch.FINI);
                            return;
                        default:
                            String envoyerFauteVersEcran = trameFaute("F", id, "");
                            envoyerTrame(envoyerFauteVersEcran);
                            joueurActuel.setText(joueur1.getNom() + " " + joueur1.getPrenom());
                            break;
                    }
                }
                else
                {
                }
                trameCouleurRecue = -1;

                if(casseEstFini)
                {
                    match.setEtat(Match.EtatMatch.EN_COURS);
                    break;
                }
                try
                {
                    Thread.sleep(100);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private int indexJoueurActuel = 0;
    private void manche()
    {
        Log.d(TAG, "La casse est finie ! Maintenant place au match !");
        if (trameCouleurRecue == -1) return;

        Joueur joueur = joueurs.get(indexJoueurActuel);
        int id = joueur.getId();

        switch (trameCouleurRecue)
        {
            case 0:
                if (joueur.getCouleur() == CouleurBille.ROUGE)
                {
                    retirerBilleRouge();
                    String trameRouge = trameEmpochage("E", id, trameCouleurRecue, tramePocheRecue);
                    envoyerTrame(trameRouge);
                    joueur.ajouterPoint();
                }
                else
                {
                    String trameFaute = trameFaute("F", id, "Empoche bille autre joueur");
                    envoyerTrame(trameFaute);
                }
                break;

            case 1:
                if (joueur.getCouleur() == CouleurBille.JAUNE)
                {
                    retirerBilleJaune();
                    String trameJaune = trameEmpochage("E", id, trameCouleurRecue, tramePocheRecue);
                    envoyerTrame(trameJaune);
                    joueur.ajouterPoint();
                }
                else
                {
                    String trameFaute = trameFaute("F", id, "Empoche bille autre joueur");
                    envoyerTrame(trameFaute);
                }
                break;

            case 3:
                String trameFauteNoire = trameFaute("F", id, "Noire");
                envoyerTrame(trameFauteNoire);
                joueurs.get((indexJoueurActuel + 1) % 2).ajouterPoint();
                match.setEtat(Match.EtatMatch.FINI);
                runOnUiThread(this::gestionDuMatch);
                return;

            default:
                String trameFaute = trameFaute("F", id, "");
                envoyerTrame(trameFaute);
                break;
        }

        int finalIndex = indexJoueurActuel;
        runOnUiThread(() -> {
            if (finalIndex == 0)
            {
                afficherPointsJoueur1.setText(String.valueOf(joueurs.get(0).afficherPoint()));
            }
            else
            {
                afficherPointsJoueur2.setText(String.valueOf(joueurs.get(1).afficherPoint()));
            }
        });

        indexJoueurActuel = (indexJoueurActuel + 1) % 2;
        Joueur suivant = joueurs.get(indexJoueurActuel);
        runOnUiThread(() -> joueurActuel.setText(suivant.getNom() + " " + suivant.getPrenom()));
        trameCouleurRecue = -1;

        if (indexBilleRouge == 7 || indexBilleJaune == 7)
        {
            match.setEtat(Match.EtatMatch.FINI);
            Log.d(TAG, "Match terminé !");
            runOnUiThread(this::gestionDuMatch);
        }
    }
    public void retirerBilleRouge()
    {
        if (indexBilleRouge < billesRouges.length)
        {
            billesRouges[indexBilleRouge].setVisibility(ImageView.GONE);
            indexBilleRouge++;
        }
    }

    public void retirerBilleJaune()
    {
        if (indexBilleJaune < billesJaunes.length)
        {
            billesJaunes[indexBilleJaune].setVisibility(ImageView.GONE);
            indexBilleJaune++;
        }
    }
    public void afficherJoueursCreer()
    {
        Log.d(TAG, "Joueur 1 : " + joueurs.get(0).getCouleur());
        Log.d(TAG, "Joueur 2 : " + joueurs.get(1).getCouleur());
    }
    private void gestionDuMatch()
    {
        switch (match.getEtat())
        {
            case CREE:
                casse();
                break;
            case EN_COURS:
                Log.d(TAG, "Lancement de la partie");
                manche();
                break;
            case FINI:
                finDeMatch();
                break;
        }
    }
    private void verifierEtDemarrerBluetooth()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) !=
               PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(
                  this,
                  new String[] { Manifest.permission.BLUETOOTH_CONNECT },
                  REQUEST_BLUETOOTH_PERMISSION);
                return;
            }
        }
        initialiserBluetooth();
    }

    private void initialiserBluetooth()
    {
        BluetoothAdapter adaptateurBluetooth = BluetoothAdapter.getDefaultAdapter();
        if(adaptateurBluetooth == null || !adaptateurBluetooth.isEnabled())
        {
            Log.e(TAG, "Bluetooth non disponible ou désactivé");
            return;
        }

        deviceEmission                 = adaptateurBluetooth.getRemoteDevice(ADRESSE_MAC_ECRAN);
        communicationBluetoothEmission = new CommunicationBluetooth(deviceEmission);
        communicationBluetoothEmission.start();

        deviceReception                 = adaptateurBluetooth.getRemoteDevice(ADRESSE_MAC_PC_B20);
        communicationBluetoothReception = new CommunicationBluetooth(deviceReception);
        communicationBluetoothReception.setReceptionListener(message -> {
            runOnUiThread(() -> {
                if(estTrameValide(message))
                {
                    decomposerTrameEmpochage(message);
                }
                else
                {
                    Toast.makeText(ActivitePartie.this, "Trame invalide reçue", Toast.LENGTH_SHORT)
                      .show();
                }
            });
        });
        communicationBluetoothReception.start();

        new Handler().postDelayed(() -> {
            if(communicationBluetoothEmission != null &&
               communicationBluetoothEmission.estConnecte())
            {
                Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast
                  .makeText(this, "Bluetooth (écran) non connecté, envoi annulé", Toast.LENGTH_LONG)
                  .show();
            }
        }, 2000);
    }

    public void envoyerTrame(String trame)
    {
        if(communicationBluetoothEmission != null && communicationBluetoothEmission.estConnecte())
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
        if(trame == null)
        {
            return false;
        }

        trame = trame.trim();
        if(!trame.startsWith(CommunicationBluetooth.ENTETE) ||
           !trame.endsWith(CommunicationBluetooth.DELIMITATEUR_FIN))
        {
            return false;
        }

        String contenu    = trame.substring(1, trame.length() - 1);
        String[] morceaux = contenu.split("/");

        if(morceaux.length != 2)
        {
            return false;
        }

        try
        {
            Integer.parseInt(morceaux[0]);
            Integer.parseInt(morceaux[1]);
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }

    public void decomposerTrameEmpochage(String trame)
    {
        if(trame == null)
        {
            return;
        }

        trame             = trame.trim();
        String contenu    = trame.substring(1, trame.length() - 1);
        String[] morceaux = contenu.split("/");

        try
        {
            trameCouleurRecue = Integer.parseInt(morceaux[0]);
            tramePocheRecue   = Integer.parseInt(morceaux[1]);

            Log.d(TAG, "Couleur : " + trameCouleurRecue + ", Poche : " + tramePocheRecue);
            Toast
              .makeText(this,
                        "Trame reçue : Couleur " + trameCouleurRecue + " | Poche " +
                          tramePocheRecue,
                        Toast.LENGTH_LONG)
              .show();
            gestionDuMatch();

        }
        catch(NumberFormatException e)
        {
            Log.e(TAG, "Erreur de format inattendue dans une trame pourtant validée");
        }
    }

    public String trameEtatPourTable(String etat)
    {
        return CommunicationBluetooth.ENTETE + etat + CommunicationBluetooth.DELIMITATEUR_FIN;
    }

    public String trameDemarrerMatch(String type,
                                     int    nbParties,
                                     String prenomJoueur1,
                                     String prenomJoueur2)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR +
          nbParties + CommunicationBluetooth.SEPARATEUR + prenomJoueur1 +
          CommunicationBluetooth.SEPARATEUR + prenomJoueur2 +
          CommunicationBluetooth.DELIMITATEUR_FIN;
    }

    public String trameCasse(String type, int idPartie, int idJoueur, int couleurBille, int idPoche)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR + idPartie +
          CommunicationBluetooth.SEPARATEUR + idJoueur + CommunicationBluetooth.SEPARATEUR +
          couleurBille + CommunicationBluetooth.SEPARATEUR + idPoche +
          CommunicationBluetooth.DELIMITATEUR_FIN;
    }

    public String trameEmpochage(String type, int idJoueur, int couleurBille, int idPoche)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR + idJoueur +
          CommunicationBluetooth.SEPARATEUR + couleurBille + CommunicationBluetooth.SEPARATEUR +
          idPoche + CommunicationBluetooth.DELIMITATEUR_FIN;
    }

    public String trameFaute(String type, int idJoueur, String faute)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR + idJoueur +
          CommunicationBluetooth.SEPARATEUR + faute + CommunicationBluetooth.DELIMITATEUR_FIN;
    }

    public String trameFinDePartie(String type, int idPartie, int idJoueur)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR + idPartie +
          CommunicationBluetooth.SEPARATEUR + idJoueur + CommunicationBluetooth.DELIMITATEUR_FIN;
    }

    public String trameFinDeMatch(String type, int nbPartiesJoueur1, int nbPartiesJoueur2)
    {
        return CommunicationBluetooth.ENTETE + type + CommunicationBluetooth.SEPARATEUR +
          nbPartiesJoueur1 + CommunicationBluetooth.SEPARATEUR + nbPartiesJoueur2 +
          CommunicationBluetooth.DELIMITATEUR_FIN;
    }

    public void finDeMatch()
    {
        if (joueurs.get(0).afficherPoint() < joueurs.get(1).afficherPoint())
        {
            Toast
                .makeText(this,
                        "Joueur Gagant : " + joueurs.get(1).afficherPoint(),
                        Toast.LENGTH_LONG)
                .show();
            String envoyerFinVersEcran =
                    trameFinDeMatch("T",
                            joueurs.get(1).afficherPoint(),
                            joueurs.get(0).afficherPoint());
            envoyerTrame(envoyerFinVersEcran);
        }
        else if (joueurs.get(0).afficherPoint() > joueurs.get(1).afficherPoint())
        {
            Toast
                .makeText(this,
                        "Joueur Gagant : " + joueurs.get(1).afficherPoint(),
                        Toast.LENGTH_LONG)
                .show();
            String envoyerFinVersEcran =
                    trameFinDeMatch("T",
                            joueurs.get(1).afficherPoint(),
                            joueurs.get(0).afficherPoint());
            envoyerTrame(envoyerFinVersEcran);
        }
        else
        {
            Log.d(TAG, "finDeMatch: Erreur pas de points ou égalité");
        }
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(communicationBluetoothEmission != null)
            communicationBluetoothEmission.close();
        if(communicationBluetoothReception != null)
            communicationBluetoothReception.close();
    }

    @Override
    public void onRequestPermissionsResult(int      requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        if(requestCode == REQUEST_BLUETOOTH_PERMISSION)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
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