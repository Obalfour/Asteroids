package com.example.asteroides;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Vector;

public class GameView extends View implements SensorEventListener{

    Context mContext;
    private float mX=0, mY=0;
    private boolean shoot=false;
    private boolean containsInitialValue = false;
    private float initialValue;

    // //// ASTEROIDS //////
    private Vector<Graphics> Asteroids; // Vector with Asteroids
    private int numAsteroids= 5; // Initial number of asteroids
    private int numFragments= 3; // How many pieces it is divided

    // //// SPACESHIP //////
    private Graphics spaceship;// Spaceship Graphic
    private int spaceshipTwirl; // direction increment
    private float spaceshipAcceleration; // aumento de velocidad
    // Standard increment of twirl and acceleration
    private static final int SPACESHIP_TWIRL_STEP = 5;
    private static final float SPACESHIP_ACCELERATION_STEP = 0.5f;

    // //// THREAD Y TIEMPO //////
    // Game Process Thread
    private ThreadGame thread = new ThreadGame();
    // process time (ms)
    private static int PROCESS_PERIOD = 50;
    // Last process duration
    private long lastProcess = 0;

    // //// MISIL //////
    private Graphics missile;
    private static int MISSILE_SPEED_STEP = 12;
    private boolean activeMissile = false;
    private int missileTime;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        //if (!listSensors.isEmpty()) {
            //Sensor orientationSensor = listSensors.get(0);
            //mSensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME);
        //}
        Drawable drawableSpaceship, drawableAsteroid, drawableMissile;
   /*     SharedPreferences pref = PreferenceManager.
                getDefaultSharedPreferences(getContext());
        if (pref.getString("graficos", "1").equals("0")) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            Path pathAsteroid = new Path();
            pathAsteroid.moveTo((float) 0.3, (float) 0.0);
            pathAsteroid.lineTo((float) 0.6, (float) 0.0);
            pathAsteroid.lineTo((float) 0.6, (float) 0.3);
            pathAsteroid.lineTo((float) 0.8, (float) 0.2);
            pathAsteroid.lineTo((float) 1.0, (float) 0.4);
            pathAsteroid.lineTo((float) 0.8, (float) 0.6);
            pathAsteroid.lineTo((float) 0.9, (float) 0.9);
            pathAsteroid.lineTo((float) 0.8, (float) 1.0);
            pathAsteroid.lineTo((float) 0.4, (float) 1.0);
            pathAsteroid.lineTo((float) 0.0, (float) 0.6);
            pathAsteroid.lineTo((float) 0.0, (float) 0.2);
            pathAsteroid.lineTo((float) 0.3, (float) 0.0);
            ShapeDrawable dAsteroid = new ShapeDrawable(
                    new PathShape(pathAsteroid, 1, 1));
            dAsteroid.getPaint().setColor(Color.WHITE);
            dAsteroid.getPaint().setStyle(Paint.Style.STROKE);
            dAsteroid.setIntrinsicWidth(50);
            dAsteroid.setIntrinsicHeight(50);
            drawableAsteroid = dAsteroid;
            setBackgroundColor(Color.BLACK);
            ShapeDrawable dMissile = new ShapeDrawable(new RectShape());
            dMissile.getPaint().setColor(Color.WHITE);
            dMissile.getPaint().setStyle(Paint.Style.STROKE);
            dMissile.setIntrinsicWidth(15);
            dMissile.setIntrinsicHeight(3);
            drawableMissile = dMissile;
        } else {
            setLayerType(View.LAYER_TYPE_HARDWARE, null); */
            drawableAsteroid = ContextCompat.getDrawable(context, R.drawable.asteroide1);
            drawableMissile = ContextCompat.getDrawable(context, R.drawable.misil1);
      //  }
        drawableSpaceship = context.getResources().getDrawable(R.drawable.nave);
        spaceship = new Graphics(this, drawableSpaceship);
        missile = new Graphics(this, drawableMissile);
        Asteroids = new Vector();
        for (int i = 0; i < numAsteroids; i++) {
            Graphics asteroid = new Graphics(this, drawableAsteroid);
            asteroid.setIncY(Math.random() * 4 - 2);
            asteroid.setIncX(Math.random() * 4 - 2);
            asteroid.setAngle((int) (Math.random() * 360));
            asteroid.setRotation((int) (Math.random() * 8 - 4));
            Asteroids.add(asteroid);
        }
    }


    @Override protected void onSizeChanged(int width, int heigth,
                                           int width_prev, int heigth_prev) {
        super.onSizeChanged(width, heigth, width_prev, heigth_prev);

        // Spaceship at the center of the screen
        spaceship.setPosX(width / 2 - spaceship.getWidth() / 2);
        spaceship.setPosY(heigth / 2 - spaceship.getHeigth() / 2);

        // Once we know the width and height.
        for (Graphics asteroid: Asteroids) {
            do{
                asteroid.setPosX((int)Math.random() * (width-asteroid.getWidth()));
                asteroid.setPosY((int)Math.random() * (heigth-asteroid.getHeigth()));
            } while(asteroid.distance(spaceship) < (width+heigth)/5);
        }

        lastProcess = System.currentTimeMillis();
        thread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        spaceship.drawGraphics(canvas);

        if (activeMissile) {
            missile.drawGraphics(canvas);
        }

        for (Graphics asteroid: Asteroids) {
            asteroid.drawGraphics(canvas);
        }
    }

    synchronized
    public void updatePhysics() {
        long now = System.currentTimeMillis();
        // Dont act until time process end
        if (lastProcess + PROCESS_PERIOD > now) {
            return;
        }
        //Calculate delay for real time execution
        double delay = (now - lastProcess) / PROCESS_PERIOD;
        lastProcess = now;	// For next time
        // Update velocity and direction of the spaceship with
        // spaceshipTwirl and spaceshipcceleration (according to player entry)
        spaceship.setAngle((int) (spaceship.getAngle() + spaceshipTwirl * delay));
        double nIncX = spaceship.getIncX() + spaceshipAcceleration *
                Math.cos(Math.toRadians(spaceship.getAngle())) * delay;
        double nIncY = spaceship.getIncY() + spaceshipAcceleration *
                Math.sin(Math.toRadians(spaceship.getAngle())) * delay;

        // Update if speed dont exceed maximum
        if (Math.hypot(nIncX, nIncY) <= Graphics.MAX_SPEED) {
            spaceship.setIncX(nIncX);
            spaceship.setIncY(nIncY);
        }
        // Update spaceship position
        spaceship.advancePos(delay);

        // Update missile position
        if (activeMissile) {
            missile.advancePos(delay);
            missileTime-=delay;
            if (missileTime < 0) {
                activeMissile = false;
            } else {
                for (int i = 0; i < Asteroids.size(); i++)
                    if (missile.checkCollision(Asteroids.elementAt(i))) {
                        destroyAsteroid(i);
                        break;
                    }
            }
        }

        for (Graphics asteroid : Asteroids) {
            asteroid.advancePos(delay);
        }
    }

    private void destroyAsteroid(int pos) {
        Asteroids.remove(pos);
        activeMissile = false;
    }

    private void ActivateMissile() {
        missile.setPosX(spaceship.getPosX() + spaceship.getWidth() / 2 - missile.getWidth() / 2);
        missile.setPosY(spaceship.getPosY() + spaceship.getHeigth() / 2 - missile.getHeigth() / 2);
        missile.setAngle(spaceship.getAngle());
        missile.setIncX(Math.cos(Math.toRadians(missile.getAngle())) * MISSILE_SPEED_STEP);
        missile.setIncY(Math.sin(Math.toRadians(missile.getAngle())) * MISSILE_SPEED_STEP);

        missileTime = (int) Math.min(this.getWidth() / Math.abs(missile.getIncX()),
                this.getHeight() / Math.abs(missile.getIncY())) - 2;
        activeMissile = true;
    }

    class ThreadGame extends Thread {
        private boolean pause, run;

        public synchronized void pause() {
            pause = true;
        }

        public synchronized void resumeGame() {
            pause = false;
            notify();
        }

        public void stopGame() {
            run = false;
            if (pause) resumeGame();
        }

        @Override
        public void run() {
            run = true;
            while (run) {
                updatePhysics();
                synchronized (this) {
                    while (pause)
                        try {
                            wait();
                        } catch (Exception e) {
                        }
                }
            }
        }
    }

    public ThreadGame getThread() {
        return thread;
    }

    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        super.onKeyDown(keyCode, event);

        boolean procesed = true;

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                spaceshipAcceleration = +SPACESHIP_ACCELERATION_STEP;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                spaceshipTwirl = -SPACESHIP_TWIRL_STEP;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                spaceshipTwirl = +SPACESHIP_TWIRL_STEP;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                ActivateMissile();
                break;
            default:
                procesed = false;
                break;
        }
        return procesed;
    }

    @Override

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        super.onKeyUp(keyCode, event);
        boolean procesed = true;

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                spaceshipAcceleration = 0;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                spaceshipTwirl = 0;
                break;
            default:
                procesed = false;
                break;
        }
        return procesed;
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {

        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                shoot = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dy<25 && dx>25){
                    spaceshipTwirl = Math.round((x - mX) / 5);
                    shoot = false;
                } else if (dx<25 && dy>25){
                    spaceshipAcceleration = Math.round((mY - y) / 35);
                    shoot = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                spaceshipTwirl = 0;
                spaceshipAcceleration = 0;
                if (shoot){
                    ActivateMissile();
                }
                break;
        }
        mX=x; mY=y;
        return true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float value = event.values[1];
        if (!containsInitialValue){
            initialValue = value;
            containsInitialValue = true;
        }
        spaceshipTwirl=(int) (value-initialValue)/3 ;
    }

}
