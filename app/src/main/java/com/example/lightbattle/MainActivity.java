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

public class MainActivity extends AppCompatActivity {

    private DatagramSocket UDPSocket;
    private InetAddress address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btSendMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData();
            }
        });
    }

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

    public void SendInstructionToLed(final byte[] data )
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
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    public void SendData() {
        try
        {
         for (int i = 0; i < Integer.parseInt(((TextView) findViewById(R.id.EdNbRepete)).getText().toString()); i++)
            {
              byte[] data = ((TextView) findViewById(R.id.EdText)).getText().toString().getBytes();
              SendInstructionToLed(data);
            }
         }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void SendDatardm() {
        try
        {
            for (int i = 0; i < Integer.parseInt(((TextView) findViewById(R.id.EdNbRepete)).getText().toString()); i++)
            {
                int rnd = myRandomInteger(1,2);
                String datastr = String.valueOf(rnd);
                datastr="("+datastr+")";
                byte[] data =   datastr.getBytes();
                SendInstructionToLed(data);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

   public int myRandomInteger(int min, int max)
    {
        return (int) (min + Math.random() * (max - min + 1));
    }

}
