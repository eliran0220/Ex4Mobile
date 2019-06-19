package com.example.ex4mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class JoystickActivity extends AppCompatActivity implements JoystickView.JoystickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JoystickView joystick = new JoystickView(this);
        setContentView(joystick);
    }

    /*
     The function uses the tcpClient instance method to get an instance of the client,
     and uses the sendValues function of it to send the values to server.

     */
    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        TcpClient tcpClient = TcpClient.getInstance();
        String[] commands = initializeCommands(xPercent, yPercent);
        tcpClient.sendValues(commands[0]);
        tcpClient.sendValues(commands[1]);
    }

    /*
    Sets the right values to the aileron and elevator in the server / simulator.
     */
    public String[] initializeCommands(float xPercent, float yPercent) {
        String[] commands = new String[2];
        yPercent *= -1;
        commands[0] = "set /controls/flight/aileron " + Float.toString(xPercent) + " \r\n";
        commands[1] = "set /controls/flight/elevator " + Float.toString(yPercent) + " \r\n";
        return commands;
    }

}
