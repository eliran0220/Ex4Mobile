package com.example.ex4mobile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/*
The joystick class.
 */
public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private float centerX;
    private float centerY;
    private float baseRadius;
    private float joystickRadius;
    private JoystickListener joystickCallback;

    /*
    The function sets the dimensions of the joystick based on the screen width and
    height.
     */
    private void setupDimensions() {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight()) / 2;
        joystickRadius = Math.min(getWidth(), getHeight()) / 5;
    }

    /*
    The constructor of the joystick,sets the listener (to get positions of x,y)
     */
    public JoystickView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    /*
    Another constructor, in case more attributes are given and a style.
     */
    public JoystickView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    /*
    Another constructor, in case attributes are given
     */
    public JoystickView(Context context, AttributeSet attributes) {
        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    /*
    The drawJoystick function draws the joystick on the screen by the given newX, newY
    parameters.
    First draws the canvas color, then the outside circle and then the inner circle
    (which is the joystick itself)
     */
    private void drawJoystick(float newX, float newY) {
        if (getHolder().getSurface().isValid()) {
            Canvas myCanvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            myCanvas.drawColor(Color.argb(255, 2, 136, 209)); // BG color
            colors.setColor(Color.argb(255, 211, 211, 211));
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors);
            colors.setARGB(255, 36, 190, 1);
            myCanvas.drawCircle(newX, newY, joystickRadius, colors);
            getHolder().unlockCanvasAndPost(myCanvas);

        }
    }

    /*
    The first time the surface is created,
     the function sets the dimensions and then draws the new joystick.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupDimensions();
        drawJoystick(centerX, centerY);
    }

    /*
    In case the joystick changes it's surface (for example, the screen becomes
    horizontal) the function sets the dimensions and then draws the new joystick
    (by the given parameters)
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        setupDimensions();
        drawJoystick(centerX, centerY);
    }

    /*
    In case the app closes, this is called
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        TcpClient client = TcpClient.getInstance();
        client.closeClient();
    }

    /*
    The onTouch functions monitors the joystick values every time it changes it's position
    If the joystick is released, it is set back to the 0,0 position (center)
     */
    public boolean onTouch(View v, MotionEvent e) {
        if (v.equals(this)) {
            if (e.getAction() != e.ACTION_UP) {
                float displacement = (float) Math.sqrt((Math.pow(e.getX() - centerX, 2)) + Math.pow(e.getY() - centerY, 2));
                if (displacement < baseRadius) {
                    drawJoystick(e.getX(), e.getY());
                    joystickCallback.onJoystickMoved((e.getX() - centerX) / baseRadius, (e.getY() - centerY) / baseRadius, getId());
                } else {
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (e.getX() - centerX) * ratio;
                    float constrainedY = centerY + (e.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                    joystickCallback.onJoystickMoved((constrainedX - centerX) / baseRadius, (constrainedY - centerY) / baseRadius, getId());
                }
            } else {
                drawJoystick(centerX, centerY);
                joystickCallback.onJoystickMoved(0, 0, getId());
            }
        }
        return true;
    }

    /*
    Creates the joystick listener.
     */
    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent, int id);
    }
}

