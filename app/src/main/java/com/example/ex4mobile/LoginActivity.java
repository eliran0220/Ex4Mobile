package com.example.ex4mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button connect;
    private EditText ip;
    private EditText port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connect = findViewById(R.id.connectButton);
        connect.setOnClickListener(this);
        ip = findViewById(R.id.editIp);
        port = findViewById(R.id.editPort);
    }



    @Override
    public void onClick(View view) {
        String connectIP = ip.getText().toString();
        String tempPort = port.getText().toString();
        int connectPort = Integer.parseInt(tempPort);
        TcpClient tcpClient = TcpClient.getInstance();
        tcpClient.connect(connectIP,connectPort);
        Intent intent = new Intent(this, JoystickActivity.class);
        startActivity(intent);
    }
}
