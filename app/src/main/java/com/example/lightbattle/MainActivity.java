package com.example.lightbattle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity
{

    private DatagramSocket UDPSocket;
    private InetAddress address;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btSendMessage).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendData(Integer.parseInt(((TextView) findViewById(R.id.EdNbRepete)).getText().toString()));
            }
        });
    }

    /// Initialise une socket avec les parametres recupere dans l'interface graphique pour l'envoi des données
    public void Initreseau()
    {
        try
        {
            UDPSocket = new DatagramSocket();
            address = InetAddress.getByName(((TextView)findViewById(R.id.EdIpServeur)).getText().toString());
        }
        catch (SocketException e)
        {
                e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
    }
    /// Envoi les données dans la socket defini par la methode InitReseau
    public void SendInstruction(final byte[] data )
    {
        Initreseau();
        (new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    DatagramPacket packet = new DatagramPacket(data, data.length, address, Integer.valueOf(((TextView) findViewById(R.id.EdPort)).getText().toString()));
                    UDPSocket.send(packet);
                    DatagramPacket packetreponse = null;
                    UDPSocket.receive(packetreponse);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /// Envoi X fois la data
    public void SendData(int x)
    {
        try
        {
         for (int i = 0; i < x; i++)
            {
              byte[] data = ((TextView) findViewById(R.id.EdText)).getText().toString().getBytes();
              SendInstruction(data);
            }
         }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /// scan le port mis en parametre
    public void ReceiveData(final int portNum)
    {
        (new Thread()
        {
            @Override
            public void run()
            {
                try
                {

                    final int taille = 1024;
                    final byte buffer[] = new byte[taille];
                    DatagramSocket socketReceive = new DatagramSocket(portNum);
                    while(true)
                    {
                        DatagramPacket data = new DatagramPacket(buffer,buffer.length);
                        socketReceive.receive(data);
                        System.out.println(data);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
