package com.example.plug_in_pool;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

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

    private static final String TAG = "_CommunicationBluetooth";
    private static final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothDevice peripherique;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private ReceptionListener receptionListener;

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
            inputStream = socket.getInputStream();

            Log.d(TAG, "Connexion Bluetooth réussie");

            new Thread(this::ecouterReception).start();
        }
        catch (IOException e)
        {
            Log.e(TAG, "Erreur lors de la connexion Bluetooth", e);
        }
    }

    public void envoyerTrameAsync(String trame)
    {
        executor.execute(() -> {
            try
            {
                if (outputStream != null)
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
            catch (IOException e)
            {
                Log.e(TAG, "Erreur lors de l'envoi de la trame", e);
            }
        });
    }

    private void ecouterReception()
    {
        byte[] buffer = new byte[1024];
        int bytes;

        while (socket != null && socket.isConnected())
        {
            try
            {
                bytes = inputStream.read(buffer);
                String messageRecu = new String(buffer, 0, bytes);

                Log.d(TAG, "Message reçu : " + messageRecu);

                if (receptionListener != null)
                {
                    receptionListener.onMessageRecu(messageRecu);
                }
            }
            catch (IOException e)
            {
                Log.e(TAG, "Erreur de réception", e);
                break;
            }
        }
    }

    public boolean estConnecte()
    {
        return socket != null && socket.isConnected();
    }

    public interface ReceptionListener
    {
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
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null && socket.isConnected()) socket.close();
        }
        catch (IOException e)
        {
            Log.e(TAG, "Erreur lors de la fermeture de la connexion", e);
        }
    }
}