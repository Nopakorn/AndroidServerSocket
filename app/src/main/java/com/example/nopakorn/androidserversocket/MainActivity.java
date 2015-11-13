package com.example.nopakorn.androidserversocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView info, infoip, msg, textBtn;
    String message = "";
    ServerSocket serverSocket;

    private LayoutInflater mInflater;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = (TextView) findViewById(R.id.info);
        infoip = (TextView) findViewById(R.id.infoip);
        infoip.setText(getIpAddress());
        mInflater = LayoutInflater.from(getApplicationContext());
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void screenChange(int count, String btn){
        if(btn.equals("batt1")){
            Intent intent = new Intent(MainActivity.this, Batt1Activity.class);
            startActivity(intent);

        }else if(btn.equals("batt2")){
            Intent intent = new Intent(MainActivity.this, Batt2Activity.class);
            startActivity(intent);

        }else if(btn.equals("batt3")){
            Intent intent = new Intent(MainActivity.this, Batt3Activity.class);
            startActivity(intent);

        }else if(btn.equals("batt4")){
            Intent intent = new Intent(MainActivity.this, Batt4Activity.class);
            startActivity(intent);

        }else if(btn.equals("batteryStart")){
            Intent intent = new Intent(MainActivity.this, BatteryActivity.class);
            startActivity(intent);

        }

    }



    private class SocketServerThread extends Thread {

        static final int SocketServerPORT = 8080;
        int count = 0;
        String btnName ="nothing...";
        SocketServerReplyThread socketServerReplyThread;
        @Override
        public void run() {

            try {
                serverSocket = new ServerSocket(SocketServerPORT);
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        info.setText("I'm waiting here: "
                                + serverSocket.getLocalPort());
                    }
                });

                while (true) {
                    Socket socket = serverSocket.accept();

                    //TODO Get input stream from client
                    InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader reader = new BufferedReader(streamReader);
                    btnName = reader.readLine();
                    socket.shutdownInput();
                    //--
                    count++;
                    message = "#" + count + " from " + socket.getInetAddress()
                            + ":" + socket.getPort() + "\n";
                    MainActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            String send = btnName;
                            screenChange(count, send);

                        }
                    });
                    socketServerReplyThread = new SocketServerReplyThread(socket,btnName);
                    socketServerReplyThread.run();

                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        int cnt;
        String response;
        SocketServerReplyThread(Socket socket, String res) {
            hostThreadSocket = socket;
            response  = res;
        }

        @Override
        public void run() {
            OutputStream outputStream;
            String msgReply = "from Android-server, # " + response;

            try {
                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(msgReply);
                Log.d("Server","Reply success: "+msgReply);
                printStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }finally{
                if(hostThreadSocket != null){
                    try {
                        hostThreadSocket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}

