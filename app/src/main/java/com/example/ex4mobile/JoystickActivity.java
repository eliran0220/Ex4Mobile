package com.example.ex4mobile;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class JoystickActivity extends AppCompatActivity implements JoystickView.JoystickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JoystickView joystick = new JoystickView(this);
        setContentView(joystick);
    }

    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        TcpClient tcpClient = TcpClient.getInstance();
        String[] commands = new String[2];
        commands[0] = "set /controls/flight/aileron ";
        commands[1] = "set /controls/flight/elevator ";
        commands[0] += String.valueOf(xPercent) + " \r\n";
        commands[1] += String.valueOf(yPercent) + " \r\n";
        tcpClient.sendValues(commands[0]);
        tcpClient.sendValues(commands[1]);
    }

}
