package com.example.ex4mobile;

import android.util.Log;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.io.BufferedWriter;
import java.net.Socket;

import static android.content.ContentValues.TAG;

public class TcpClient {
    public static TcpClient tcpClient = null;
    private String ip;
    private int port;
    private boolean isRunning;
    private Socket socket;
    private String[] commands;

    private TcpClient() {
        this.ip = "";
        this.socket = null;
        isRunning = false;
        commands = new String[2];
    }

    public static TcpClient getInstance() {
        if (tcpClient == null)
            tcpClient = new TcpClient();

        return tcpClient;
    }

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

    public boolean sock() {
        if (this.socket == null)
            return false;
        return true;
    }


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


    public boolean isConnected() {
        return this.isRunning;
    }

    public void closeClient() {
        try {
            this.socket.close();
        } catch (Exception e) {
            Log.e("TCP", "S: Error", e);
        }
    }

    public void initializeCommands() {
        this.commands[0] = "set /controls/flight/aileron ";
        this.commands[1] = "set /controls/flight/elevator ";
    }
}


