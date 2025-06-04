package com.example.plug_in_pool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import androidx.core.content.ContextCompat;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class ActivitePartie extends AppCompatActivity
{
    private static final String TAG = "_ActivitePartie";
    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;

    private CommunicationBluetooth communicationBluetoothEcran;
    private CommunicationBluetooth communicationBluetoothTable;

    private BluetoothDevice deviceEmission;
    private BluetoothDevice deviceReception;
    private BaseDeDonnees   baseDonnees;

    /**
     * Éléments de l'interface
     */
    private Joueur       joueur1;
    private Joueur       joueur2;
    private String       nbParties;
    private String       adresseMacEcran;
    private String       adresseMacTable;
    private Match        match;

    List<Joueur> joueurs           = new ArrayList<>();
    private int          tramePocheRecue;
    private int          nbPartiesGestion;
    private int          trameCouleurRecue      = -2;
    private int          nbPartiesCompteur      = 0;
    private int          indexBilleRouge        = 0;
    private int          indexBilleJaune        = 0;
    private int          indexJoueurActuel      = 0;
    private int          nbPartiesGagnerJoueur1 = 0;
    private int          nbPartiesGagnerJoueur2 = 0;
    private int           idMatch;
    private int          numeroTable;

    private TextView tableStatut;
    private TextView ecranStatut;
    private TextView texteJoueur1;
    private TextView texteJoueur2;
    private TextView afficherPointsJoueur1;
    private TextView afficherPointsJoueur2;
    private TextView joueurActuel;
    private TextView idPartieEnCours;
    private TextView joueurGagnant;
    private TextView messageEmpochage;
    private ImageView[] billesJaunes = new ImageView[7];
    private ImageView[] billesRouges = new ImageView[7];
    private Button boutonDemarrer;
    private Button boutonTirLoupe;
    private Button boutonTerminer;
    private TextView idTable;

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
        initialiserBluetooth();
        preparerLeMatch();
    }
    private void initialiserVues()
    {
        elementsGUI();
        for(int i = 0; i < 7; i++)
        {
            int resID =
              getResources().getIdentifier("billeJaune" + (i + 1), "id", getPackageName());
            billesJaunes[i] = findViewById(resID);
        }
        for(int j = 0; j < 7; j++)
        {
            int resID =
              getResources().getIdentifier("billeRouge" + (j + 1), "id", getPackageName());
            billesRouges[j] = findViewById(resID);
        }
        baseDonnees = BaseDeDonnees.getInstance(this);
    }
    private void elementsGUI()
    {
        tableStatut           = findViewById(R.id.tableStatut);
        ecranStatut           = findViewById(R.id.ecranStatut);
        afficherPointsJoueur1 = findViewById(R.id.nbPointsDuJoueur1);
        afficherPointsJoueur2 = findViewById(R.id.nbPointsDuJoueur2);
        texteJoueur1          = findViewById(R.id.textePourNbPointsJoueur1);
        texteJoueur2          = findViewById(R.id.textePourNbPointsJoueur2);
        joueurActuel          = findViewById(R.id.nomDuJoueur);
        idPartieEnCours       = findViewById(R.id.idPartieEnCours);
        joueurGagnant         = findViewById(R.id.joueurGagnant);
        messageEmpochage      = findViewById(R.id.empochage);
        boutonDemarrer        = findViewById(R.id.boutonDemarerMatch);
        boutonTirLoupe        = findViewById(R.id.boutonTirLoupe);
        boutonTerminer        = findViewById(R.id.boutonFinMatch);
        idTable               = findViewById(R.id.numeroDeLaTable);
    }
    private void getNbParties()
    {
        if(nbParties != null && !nbParties.isEmpty())
        {
            try
            {
                nbPartiesGestion  = Integer.parseInt(nbParties);
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
    @SuppressLint("SetTextI18n")
    private void recupererDonneesDeConfigurations()
    {
        Intent intent   = getIntent();
        joueur1         = (Joueur)intent.getSerializableExtra("joueur1");
        Log.d(TAG, "recupererDonneesDeConfigurations: " + joueur1);
        joueur2         = (Joueur)intent.getSerializableExtra("joueur2");
        Log.d(TAG, "recupererDonneesDeConfigurations: " + joueur2);
        nbParties       = intent.getSerializableExtra("nbParties").toString();
        Log.d(TAG, "recupererDonneesDeConfigurations: " + nbParties);
        adresseMacEcran = intent.getSerializableExtra("adresseMacEcran").toString();
        Log.d(TAG, "recupererDonneesDeConfigurations: " + adresseMacEcran + " (adresse mac ecran)");
        adresseMacTable = intent.getStringExtra("adresseMacTable");
        Log.d(TAG, "recupererDonneesDeConfigurations: " + adresseMacTable + " (adresse mac table)");
        idMatch         = intent.getIntExtra("idMatch", -1);
        Log.d(TAG, "idMatch : " + idMatch);
        numeroTable     = intent.getIntExtra("numeroTable", -1);
        idTable.setText("Table n°" + numeroTable);
        Log.d(TAG, "numeroTable : " + numeroTable);

        if(joueur1 != null && joueur2 != null)
        {
            texteJoueur1.setText(joueur1.getNom() + " " + joueur1.getPrenom());
            texteJoueur2.setText(joueur2.getNom() + " " + joueur2.getPrenom());
        }
        getNbParties();
    }
    private void preparerLeMatch()
    {
        boutonDemarrer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                initialiserJoueurs();
                demarrerDetectionTable();
                boutonDemarrer.setVisibility(Button.GONE);
                boutonTirLoupe.setVisibility(Button.VISIBLE);
                tirLoupe();
                messageEmpochage.setVisibility(TextView.VISIBLE);
            }
        });
    }
    private void tirLoupe()
    {
        boutonTirLoupe.setOnClickListener(view -> {
            int joueurSuivant = (indexJoueurActuel + 1) % 2;
            indexJoueurActuel = joueurSuivant;

            int aucunePoche = 0;

            Log.d(TAG, "joueur" + (1 - joueurSuivant + 1) + " a loupé son tir");
            joueurActuel.setText(joueurs.get(joueurSuivant).getNom() + " " + joueurs.get(joueurSuivant).getPrenom());

            String envoyerTirLoupeVersEcran =
                    trameEmpochage(
                            CommunicationBluetooth.EMPOCHAGE,
                            joueurs.get((indexJoueurActuel + 1) % 2).getId(),
                            CouleurBille.AUCUNE.getId(),
                            aucunePoche
                    );
            connexionEcran(envoyerTirLoupeVersEcran);
        });
    }
    private void initialiserJoueurs()
    {
        joueur1 = new Joueur(joueur1.getNom(), joueur1.getPrenom(), 0, 0);
        joueur2 = new Joueur(joueur2.getNom(), joueur2.getPrenom(), 0, 1);
        joueurs.add(joueur1);
        joueurs.add(joueur2);
        joueurActuel.setText(joueur1.getNom() + " " + joueur1.getPrenom());

        String envoyerConfigurationVersEcran =
          trameDemarrerMatch(CommunicationBluetooth.DEMARER_MATCH,
                             Integer.parseInt(nbParties),
                             joueur1.getPrenom(),
                             joueur2.getPrenom());
        connexionEcran(envoyerConfigurationVersEcran);
    }
    private void demarrerDetectionTable()
    {
        String trameDetection = trameEtatPourTable(CommunicationBluetooth.DEMARRER_DETECTION);
        connexionTable(trameDetection);
    }
    private void arreterDetectionTable()
    {
        String trameDesactivation = trameEtatPourTable(CommunicationBluetooth.DESACTIVER_DETECTION);
        connexionTable(trameDesactivation);
    }
    private void casse()
    {
        boolean casseEstFini = false;
        nbPartiesCompteur++;
        idPartieEnCours.setVisibility(TextView.VISIBLE);
        idPartieEnCours.setText("Partie n° " + nbPartiesCompteur + " en cours");

        if (!casseEstFini)
        {
            Joueur joueur = joueurs.get(indexJoueurActuel);
            int id = joueur.getId();
            joueur.afficherJoueur();

            switch (trameCouleurRecue)
            {
                case 0:
                    casseEstFini = attribuerCouleurRouge(id);
                    connexionEcran(trameCasse(CommunicationBluetooth.CASSE, nbPartiesCompteur, id, trameCouleurRecue, tramePocheRecue));
                    if (id == 0)
                    {
                        afficherPointsJoueur1.setText(String.valueOf(joueur.afficherPoint()));
                    }
                    else
                    {
                        afficherPointsJoueur2.setText(String.valueOf(joueur.afficherPoint()));
                    }
                    break;

                case 1:
                    casseEstFini = attribuerCouleurJaune(id);
                    connexionEcran(trameCasse(CommunicationBluetooth.CASSE, nbPartiesCompteur, id, trameCouleurRecue, tramePocheRecue));
                    if (id == 0)
                    {
                        afficherPointsJoueur1.setText(String.valueOf(joueur.afficherPoint()));
                    }
                    else
                    {
                        afficherPointsJoueur2.setText(String.valueOf(joueur.afficherPoint()));
                    }
                    break;

                case 2:
                    String trameFauteBlanche = trameFaute(CommunicationBluetooth.FAUTE, id, "Blanche");
                    connexionEcran(trameFauteBlanche);
                    indexJoueurActuel = (indexJoueurActuel + 1) % 2;
                    Joueur joueurSuivantBlanche = joueurs.get(indexJoueurActuel);
                    runOnUiThread(() -> joueurActuel.setText(joueurSuivantBlanche.getNom() + " " + joueurSuivantBlanche.getPrenom()));
                    break;

                case 3:
                    String trameFauteNoire = trameFaute(CommunicationBluetooth.FAUTE, id, "Noire");
                    connexionEcran(trameFauteNoire);
                    joueurs.get(indexJoueurActuel).retirerPointsEmpochageBilleNoire();
                    joueurs.get((indexJoueurActuel + 1) % 2).ajouterPoint();
                    indexJoueurActuel = (indexJoueurActuel + 1) % 2;
                    match.setEtat(Match.EtatMatch.FINI);
                    new Handler().postDelayed(()->{
                        runOnUiThread(this::gestionDuMatch);
                    },1000);
                    return;

                default:
                    String trameFaute = trameFaute(CommunicationBluetooth.FAUTE, id, "");
                    connexionEcran(trameFaute);
                    indexJoueurActuel = (indexJoueurActuel + 1) % 2;
                    Joueur joueurSuivantNoire = joueurs.get(indexJoueurActuel);
                    runOnUiThread(() -> joueurActuel.setText(joueurSuivantNoire.getNom() + " " + joueurSuivantNoire.getPrenom()));
                    break;
            }

            trameCouleurRecue = -1;

            if (casseEstFini)
            {
                match.setEtat(Match.EtatMatch.EN_COURS);
            }

            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean attribuerCouleurRouge(int id)
    {
        joueurs.get(id).setCouleur(CouleurBille.ROUGE);
        joueurs.get((id + 1) % 2).setCouleur(CouleurBille.JAUNE);
        retirerBilleRouge();
        joueurs.get(id).ajouterPoint();
        afficherJoueursCreer();

        return true;
    }
    private boolean attribuerCouleurJaune(int id)
    {
        joueurs.get(id).setCouleur(CouleurBille.JAUNE);
        joueurs.get((id + 1) % 2).setCouleur(CouleurBille.ROUGE);
        retirerBilleJaune();
        joueurs.get(id).ajouterPoint();
        afficherJoueursCreer();

        return true;
    }
    private void partie()
    {
        if (trameCouleurRecue == -1) return;

        Joueur joueur = joueurs.get(indexJoueurActuel);
        int id = joueur.getId();
        boolean rejouer = false;

        switch (trameCouleurRecue)
        {
            case 0:
                if (joueur.getCouleur() == CouleurBille.ROUGE)
                {
                    retirerBilleRouge();
                    String trameRouge = trameEmpochage(CommunicationBluetooth.EMPOCHAGE, id, trameCouleurRecue, tramePocheRecue);
                    connexionEcran(trameRouge);
                    joueur.ajouterPoint();
                    rejouer = true;
                }
                else
                {
                    retirerBilleRouge();
                    String trameFaute = trameFaute(CommunicationBluetooth.FAUTE, id, "Empoche bille autre joueur");
                    connexionEcran(trameFaute);
                }
                break;

            case 1:
                if (joueur.getCouleur() == CouleurBille.JAUNE)
                {
                    retirerBilleJaune();
                    String trameJaune = trameEmpochage(CommunicationBluetooth.EMPOCHAGE, id, trameCouleurRecue, tramePocheRecue);
                    connexionEcran(trameJaune);
                    joueur.ajouterPoint();
                    rejouer = true;
                }
                else
                {
                    retirerBilleJaune();
                    String trameFaute = trameFaute(CommunicationBluetooth.FAUTE, id, "Empoche bille autre joueur");
                    connexionEcran(trameFaute);
                }
                break;

            case 2:
                String trameFauteBlanche = trameFaute(CommunicationBluetooth.FAUTE, id, "Blanche");
                connexionEcran(trameFauteBlanche);
                break;

            case 3:
                String trameFauteNoire = trameFaute(CommunicationBluetooth.FAUTE, id, "Noire");
                connexionEcran(trameFauteNoire);
                joueurs.get(indexJoueurActuel).retirerPointsEmpochageBilleNoire();
                joueurs.get((indexJoueurActuel + 1) % 2);
                match.setEtat(Match.EtatMatch.FINI);
                new Handler().postDelayed(()->{
                    runOnUiThread(this::gestionDuMatch);
                },1000);
                return;

            default:
                String trameFaute = trameFaute(CommunicationBluetooth.FAUTE, id, "");
                connexionEcran(trameFaute);
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

        if (!rejouer)
        {
            indexJoueurActuel = (indexJoueurActuel + 1) % 2;
        }

        Joueur suivant = joueurs.get(indexJoueurActuel);
        runOnUiThread(() -> joueurActuel.setText(suivant.getNom() + " " + suivant.getPrenom()));
        trameCouleurRecue = -1;

        if (indexBilleRouge == 7 || indexBilleJaune == 7)
        {
            match.setEtat(Match.EtatMatch.FINI);
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
                Log.d(TAG, "Attente empochage");
                partie();
                break;
            case FINI:
                finDePartie();
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
    }

    private void initialiserBluetooth()
    {
        BluetoothAdapter adaptateurBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (adaptateurBluetooth == null || !adaptateurBluetooth.isEnabled())
        {
            Log.e(TAG, "Bluetooth non disponible ou désactivé");
            return;
        }

        deviceReception = adaptateurBluetooth.getRemoteDevice(adresseMacTable);
        communicationBluetoothTable = new CommunicationBluetooth(deviceReception);
        communicationBluetoothTable.setReceptionListener(message -> {
            runOnUiThread(() -> {
                if (estTrameValide(message))
                {
                    decomposerTrameEmpochage(message);
                }
                else
                {
                    Toast.makeText(this, "Trame invalide reçue de la table", Toast.LENGTH_SHORT).show();
                }
            });
        });
        communicationBluetoothTable.start();

        new Handler().postDelayed(() -> {
            if (communicationBluetoothTable != null && communicationBluetoothTable.estConnecte())
            {
                tableConnecter();
                Log.d(TAG, "Connexion Bluetooth à la table réussie");

                deviceEmission = adaptateurBluetooth.getRemoteDevice(adresseMacEcran);
                communicationBluetoothEcran = new CommunicationBluetooth(deviceEmission);
                communicationBluetoothEcran.setReceptionListener(message -> {
                    Log.d(TAG, "Message reçu de l’écran : " + message);
                });
                communicationBluetoothEcran.start();

                new Handler().postDelayed(() -> {
                    if (communicationBluetoothEcran != null && communicationBluetoothEcran.estConnecte())
                    {
                        ecranConnecter();
                        Log.d(TAG, "Connexion Bluetooth à l'écran réussie");
                    }
                    else
                    {
                        ecranNonConnecter();
                        Log.e(TAG, "Connexion à l’écran ÉCHOUÉE !");
                    }
                }, 3000);

            }
            else
            {
                tableNonConnecter();
                Log.e(TAG, "Connexion à la table ÉCHOUÉE !");
            }
        }, 3000);
    }
    private void connexionTable(String message)
    {
        if (communicationBluetoothTable != null && communicationBluetoothTable.estConnecte())
        {
            envoyerTrameVersTable(message);
            Log.d(TAG, "Message envoyé à la table : " + message);
        }
        else
        {
            Log.e(TAG, "Impossible d'envoyer le message : table non connectée");
        }
    }

    private void connexionEcran(String message)
    {
        if (communicationBluetoothEcran != null && communicationBluetoothEcran.estConnecte())
        {
            envoyerTrame(message);
            Log.d(TAG, "Message envoyé à l'écran : " + message);
        }
        else
        {
            Log.e(TAG, "Impossible d'envoyer le message : écran non connecté");
        }
    }

    public void ecranNonConnecter()
    {
        ecranStatut.setBackgroundColor(ContextCompat.getColor(this, R.color.rouge));
        ecranStatut.setText("Erreur de connexion à l'écran");
    }
    public void ecranConnecter()
    {
        ecranStatut.setBackgroundColor(ContextCompat.getColor(this, R.color.vertFonce));
        ecranStatut.setText("Connexion à l'écran reussie");
    }
    public void tableNonConnecter()
    {
        tableStatut.setBackgroundColor(ContextCompat.getColor(this, R.color.rouge));
        tableStatut.setText("Erreur de connexion à la table");
    }
    public void tableConnecter()
    {
        tableStatut.setBackgroundColor(ContextCompat.getColor(this, R.color.vertFonce));
        tableStatut.setText("Connexion à la table réussie");
    }
    public void envoyerTrame(String trame)
    {
        if (communicationBluetoothEcran != null && communicationBluetoothEcran.estConnecte())
        {
            Log.d(TAG, "Envoi vers écran : " + trame);
            communicationBluetoothEcran.envoyerTrameAsync(trame);
            ecranConnecter();
        }
        else
        {
            Log.e(TAG, "Échec envoi écran : Bluetooth (écran) non connecté");
            ecranNonConnecter();
        }
    }
    public void envoyerTrameVersTable(String trame)
    {
        if (communicationBluetoothTable != null && communicationBluetoothTable.estConnecte())
        {
            Log.d(TAG, "Envoi vers table : " + trame);
            communicationBluetoothTable.envoyerTrameAsync(trame);
            tableConnecter();
        }
        else
        {
            Log.e(TAG, "Échec envoi table : Connexion Bluetooth table non disponible");
            tableNonConnecter();
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

            messageEmpochage.setText("Bille " + CouleurBille.utiliserId(trameCouleurRecue) + " empochée dans la poche " + tramePocheRecue);
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

    public String trameEtatPourEcran(String etat)
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

    public void finDePartie()
    {
        boutonTirLoupe.setVisibility(Button.GONE);
        joueurGagnant.setVisibility(TextView.VISIBLE);

        Joueur joueur1 = joueurs.get(0);
        Joueur joueur2 = joueurs.get(1);

        int pointsJoueur1 = joueur1.afficherPoint();
        int pointsJoueur2 = joueur2.afficherPoint();

        if (pointsJoueur1 < pointsJoueur2)
        {
            joueurGagnant.setText("Le joueur gagnant est : " + joueur2.getNom() + " " + joueur2.getPrenom() + " avec " + pointsJoueur2 + " points");
            String trame = trameFinDePartie(CommunicationBluetooth.PARTIE_TERMINER, nbPartiesCompteur, joueur2.getId());
            connexionEcran(trame);
            nbPartiesGagnerJoueur2++;
            Log.d(TAG, "nbPartiesGagnees Joueur 2 : " + nbPartiesGagnerJoueur2);
        }
        else if (pointsJoueur1 > pointsJoueur2)
        {
            joueurGagnant.setText("Le joueur gagnant est : " + joueur1.getNom() + " " + joueur1.getPrenom() + " avec " + pointsJoueur1 + " points");
            String trame = trameFinDePartie(CommunicationBluetooth.PARTIE_TERMINER, nbPartiesCompteur, joueur1.getId());
            connexionEcran(trame);
            nbPartiesGagnerJoueur1++;
            Log.d(TAG, "nbPartiesGagnees Joueur 1 : " + nbPartiesGagnerJoueur1);
        }
        else
        {
            joueurGagnant.setText("Égalité entre les deux joueurs avec " + pointsJoueur1 + " points");
            Log.d(TAG, "finDeMatch : Égalité");
        }

        afficherJoueurGagnant();
        Log.d(TAG, "Fin du match");
    }
    private void afficherJoueurGagnant()
    {
        if (nbPartiesGagnerJoueur1 > nbPartiesGagnerJoueur2)
        {
            idPartieEnCours.setText("");
            messageEmpochage.setText("");
            boutonTerminer.setVisibility(Button.VISIBLE);
            joueurGagnant.setText(joueur1.getNom() + " " + joueur1.getPrenom() + " à gagner " + nbPartiesGagnerJoueur1 + " partie");
            baseDonnees.ajouterManche(idMatch,joueur1.getId(),joueur2.getId(),numeroTable);
            matchTermnier();
        }
        else if(nbPartiesGagnerJoueur1 < nbPartiesGagnerJoueur2)
        {
            idPartieEnCours.setText("");
            messageEmpochage.setText("");
            boutonTerminer.setVisibility(Button.VISIBLE);
            joueurGagnant.setText(joueur2.getNom() + " " + joueur2.getPrenom() + " à gagner " + nbPartiesGagnerJoueur2 + " partie");
            baseDonnees.ajouterManche(idMatch,joueur2.getId(),joueur1.getId(),numeroTable);
            matchTermnier();
        }
        else
        {
            idPartieEnCours.setText("");
            messageEmpochage.setText("");
            boutonTerminer.setVisibility(Button.VISIBLE);
            joueurGagnant.setText("Egalité entre le joueur 1 et le joueur 2");
            matchTermnier();
        }
    }
    public void matchTermnier()
    {
        baseDonnees.terminerMatch(idMatch, 1);
        boutonTerminer.setOnClickListener(view -> {
            envoyerFinDeMatch(nbPartiesGagnerJoueur1, nbPartiesGagnerJoueur2);
            arreterDetectionTable();
            Intent changerDeVue = new Intent(ActivitePartie.this, ActiviteHistorique.class);
            startActivity(changerDeVue);
        });
    }
    public void envoyerFinDeMatch(int nbJoueur1Parties, int nbJoueur2Parties)
    {
        String envoyerTrameVersEcrans=
                trameFinDeMatch(CommunicationBluetooth.MATCH_TERMINER, nbJoueur1Parties, nbJoueur2Parties);
        connexionEcran(envoyerTrameVersEcrans);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        String trameReprise;
        trameReprise = trameEtatPourEcran(CommunicationBluetooth.REPRISE);
        connexionEcran(trameReprise);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        String tramePause;
        tramePause = trameEtatPourEcran(CommunicationBluetooth.PAUSE);
        connexionEcran(tramePause);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(communicationBluetoothEcran != null)
            communicationBluetoothEcran.close();
        if(communicationBluetoothTable != null)
            communicationBluetoothTable.close();
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