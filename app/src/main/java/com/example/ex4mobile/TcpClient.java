package com.example.ex4mobile;

import android.util.Log;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.io.BufferedWriter;
import java.net.Socket;

import static android.content.ContentValues.TAG;

/*
The TcpClient class.
Configured to be singelton.
 */
public class TcpClient {
    public static TcpClient tcpClient = null;
    private String ip;
    private int port;
    private boolean isRunning;
    private Socket socket;

    /*
    Constructor for TcpClient.
     */
    private TcpClient() {
        this.ip = "";
        this.socket = null;
        isRunning = false;
    }

    /*
    The getInstance returns an instance of the tcp client, if already
    constructed returns it, if not constructs a new one.
     */
    public static TcpClient getInstance() {
        if (tcpClient == null)
            tcpClient = new TcpClient();

        return tcpClient;
    }

    /*
    The connect function creates a new thread in which it's going to connect
    to the given ip and port.
     */
    public void connect(String givenIp, int givenPort) {
        this.isRunning = true;
        this.ip = givenIp;
        this.port = givenPort;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress inetAddress = InetAddress.getByName(ip);
                    socket = new Socket(inetAddress, port);
                } catch (Exception e) {
                    Log.e("TCP", "S: Error", e);
                    System.out.println((e.toString()));
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /*
    The sendValues function creates a new PrintWriter and sends the
    values from the joystick to the server / simulator.
     */
    public void sendValues(final String command) {
        try {
            System.out.println(command);
            Runnable runnable = new Runnable() {
                PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                @Override
                public void run() {
                    if (printWriter != null) {
                        Log.d(TAG, "Sending: " + command);
                        printWriter.println(command);
                        printWriter.flush();
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
            closeClient();
        }

    }

    /*
    The isConnected function returns the status of the client.
     */
    public boolean isConnected() {
        return this.isRunning;
    }

    /*
    Closes the client and releases the socked.
     */
    public void closeClient() {
        try {
            this.socket.close();
        } catch (Exception e) {
            Log.e("TCP", "S: Error", e);
        }
    }

}


