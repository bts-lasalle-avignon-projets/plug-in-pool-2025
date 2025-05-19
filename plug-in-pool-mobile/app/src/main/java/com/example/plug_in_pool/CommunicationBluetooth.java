package com.example.plug_in_pool;

import android.Manifest;
import android.adservices.measurement.WebSourceRegistrationRequest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import androidx.annotation.RequiresPermission;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommunicationBluetooth extends Thread
{
    /*
     * Format Trame
     */
    protected static final String ENTETE           = "$";
    protected static final String SEPARATEUR       = "/";
    protected static final String DELIMITATEUR_FIN = "!";
    protected static final String DEMARER_MATCH    = "D";
    protected static final String CASSE            = "C";

    private static final String TAG       = "_CommunicationBluetooth";
    private static final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothDevice peripherique;
    private BluetoothSocket       socket;
    private OutputStream          outputStream;
    private InputStream           inputStream;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private ReceptionListener     receptionListener;

    public CommunicationBluetooth(BluetoothDevice peripherique)
    {
        this.peripherique = peripherique;
    }

    @Override
    public void run()
    {
        try
        {
            socket = peripherique.createRfcommSocketToServiceRecord(SERIAL_UUID);
            socket.connect();
            outputStream = socket.getOutputStream();
            inputStream  = socket.getInputStream();

            Log.d(TAG, "Connexion Bluetooth réussie");

            new Thread(this::ecouterReception).start();
        }
        catch(IOException e)
        {
            Log.e(TAG, "Erreur lors de la connexion Bluetooth", e);
        }
    }

    public void envoyerTrameAsync(String trame)
    {
        executor.execute(() -> {
            try
            {
                if(outputStream != null)
                {
                    outputStream.write(trame.getBytes());
                    outputStream.flush();
                    Log.d(TAG, "Trame envoyée : " + trame);
                }
                else
                {
                    Log.e(TAG, "OutputStream nul, trame non envoyée");
                }
            }
            catch(IOException e)
            {
                Log.e(TAG, "Erreur lors de l'envoi de la trame", e);
            }
        });
    }

    private void ecouterReception()
    {
        byte[] buffer = new byte[1024];
        int bytes;

        while(socket != null && socket.isConnected())
        {
            try
            {
                bytes = inputStream.read(buffer);

                if(bytes == -1)
                {
                    Log.e(TAG, "Connexion Bluetooth fermée (read = -1)");
                    break;
                }

                String messageRecu = new String(buffer, 0, bytes);

                Log.d(TAG, "Message reçu : " + messageRecu);

                if(estTrameValide(messageRecu))
                {
                    if(receptionListener != null)
                    {
                        receptionListener.onMessageRecu(messageRecu);
                    }
                }
                else
                {
                    Log.e(TAG, "Trame invalide reçue");
                }
            }
            catch(IOException e)
            {
                Log.e(TAG, "Erreur de réception", e);
                break;
            }
        }
        try
        {
            if(inputStream != null)
                inputStream.close();
            if(socket != null)
                socket.close();
        }
        catch(IOException e)
        {
            Log.e(TAG, "Erreur lors de la fermeture du socket", e);
        }
    }

    public boolean estConnecte()
    {
        return socket != null && socket.isConnected();
    }

    public boolean estTrameValide(String trame)
    {
        if(trame == null)
        {
            return false;
        }
        trame = trame.trim();

        if(!trame.startsWith(ENTETE) || !trame.endsWith(DELIMITATEUR_FIN))
        {
            return false;
        }

        String contenu    = trame.substring(1, trame.length() - 1);
        String[] morceaux = contenu.split(SEPARATEUR);

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

    public interface ReceptionListener {
        void onMessageRecu(String message);
    }

    public void setReceptionListener(ReceptionListener listener)
    {
        this.receptionListener = listener;
    }

    public void close()
    {
        executor.shutdownNow();
        try
        {
            if(inputStream != null)
                inputStream.close();
            if(outputStream != null)
                outputStream.close();
            if(socket != null && socket.isConnected())
                socket.close();
        }
        catch(IOException e)
        {
            Log.e(TAG, "Erreur lors de la fermeture de la connexion", e);
        }
    }
}