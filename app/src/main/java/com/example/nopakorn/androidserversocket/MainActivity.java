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

    private LinearLayout mColorViews;
    private LinearLayout mColorViews2;
    private LinearLayout mBottomPanel;
    private LayoutInflater mInflater;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = (TextView) findViewById(R.id.info);
        infoip = (TextView) findViewById(R.id.infoip);
        msg = (TextView) findViewById(R.id.msg);
        textBtn = (TextView) findViewById(R.id.textFromBtn);
        infoip.setText(getIpAddress());

        mInflater = LayoutInflater.from(getApplicationContext());

        mBottomPanel = (LinearLayout) findViewById(R.id.bottom_panel);
        mColorViews = (LinearLayout) mInflater.inflate(R.layout.colorviews, null, false);
        mColorViews2 = (LinearLayout) mInflater.inflate(R.layout.colorviews2, null, false);
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
        textBtn.setText(btn);
        mBottomPanel.removeAllViews();
        if(btn.equals("b")){
            Intent intent = new Intent(MainActivity.this, ViewBActivity.class);
            startActivity(intent);

        }else if(btn.equals("a")){
            Intent intent = new Intent(MainActivity.this, ViewAActivity.class);
            startActivity(intent);

        }else if(btn.equals("c")){
            Intent intent = new Intent(MainActivity.this, ViewCActivity.class);
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

                    //TODO:Get input stream from client
                    InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader reader = new BufferedReader(streamReader);
                    btnName = reader.readLine();
                    //reader.close();
                    socket.shutdownInput();
                    //--

                    count++;
                    message = "#" + count + " from " + socket.getInetAddress()
                            + ":" + socket.getPort() + "\n";
                    MainActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            msg.setText(message);
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
               // hostThreadSocket = new Socket("192.168.1.54", 8080);
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
}

