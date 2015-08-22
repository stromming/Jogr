package se.lth.mamn01.jogr.jogr;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.os.Handler;
import android.media.MediaPlayer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Filip on 2015-04-21.
 */
public class RunActivity extends Activity implements LocationListener, SharedPreferences.OnSharedPreferenceChangeListener {
    long timeStarted;
    String[] values;
    Handler handler;
AudioManager am;
    MediaPlayer playFinish;
    MediaPlayer playGoodSpeed;
    MediaPlayer playHighSpeed;
    MediaPlayer playLowSpeed;
    MediaPlayer play100;
    MediaPlayer play200;
    MediaPlayer play300;
    MediaPlayer play400;
    MediaPlayer play500;
    int distRemaining;
    int time;
    float speed = 0;
    int countGood, countLow, countHigh;
    float goalSpeed;
    boolean loop;
    float prevSpeed;
    int targetDist;
    private Button finishedButton;
    private ProgressBar bar;
    private int counter;
    long[] xValues;
    float[] yValues;
    boolean goodSpeed;
    int kcalCounter = 0;
    int kcalHelper = 0;
    Location location;
    TextView currentSpeedView;
    private TextView currentTimeView;
    private TextView currentMeterView;
    private long lastTick;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        getSharedPreferences("myPrefs",
                Context.MODE_MULTI_PROCESS).registerOnSharedPreferenceChangeListener(this);

        currentTimeView = (TextView) findViewById(R.id.time1);
        currentMeterView = (TextView) findViewById(R.id.meter1);
        currentSpeedView = (TextView) findViewById(R.id.speed1);

        currentSpeedView.setText("0");
        currentTimeView.setText("00:00");
        currentMeterView.setText("0 m");
         am = (AudioManager)getSystemService(AUDIO_SERVICE);

        am.registerMediaButtonEventReceiver(new ComponentName(getPackageName(), MediaButtonReceiver.class.getName()));


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        playFinish = MediaPlayer.create(this, R.raw.finish);
        playGoodSpeed = MediaPlayer.create(this, R.raw.goodspeed);
        playHighSpeed = MediaPlayer.create(this, R.raw.highspeed);
        playLowSpeed = MediaPlayer.create(this, R.raw.lowspeed);
        play100 = MediaPlayer.create(this, R.raw.one);
        play200 = MediaPlayer.create(this, R.raw.two);
        play300 = MediaPlayer.create(this, R.raw.three);

        play400 = MediaPlayer.create(this, R.raw.four);

        play500 = MediaPlayer.create(this, R.raw.five);

        Bundle extras = getIntent().getExtras();

        finishedButton = (Button) findViewById(R.id.button);
        finishedButton.setEnabled(false);
        countGood = 0;
        countLow = 0;
        countHigh = 0;
        if (extras != null) {
            String value = extras.getString("variables");
            values = value.split("[ ]");
        } else {
            values[0] = "0";
            values[1] = "0";
        }
        xValues = new long[5000];
        yValues = new float[5000];
        counter = 0;
        loop = true;
        timeStarted = System.currentTimeMillis();
        distRemaining = Integer.parseInt(values[0]);
        targetDist = distRemaining;
        time = Integer.parseInt(values[1]);
        goalSpeed = distRemaining / (float)time;
        SharedPreferences.Editor editor = getSharedPreferences("myPrefs",
                Context.MODE_MULTI_PROCESS).edit();
        editor.putBoolean("flag", false);
        editor.apply();
        bar = (ProgressBar) findViewById(R.id.progressBar1);
        bar.setVisibility(View.VISIBLE);
        bar.setScaleY(3.0f);
        bar.setProgress(0);
        bar.setLeft(1);

        handler = new Handler();

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        lastTick = System.currentTimeMillis();
        Toast.makeText(this,"Målhastighet: " + goalSpeed + " m/s", Toast.LENGTH_LONG).show();
        handler.postDelayed(runnable, 100);


    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countLow = countLow % 10;
            countGood = countGood % 30;
            countHigh = countHigh % 10;

            prevSpeed = speed;

            if (location != null && location.getSpeed() != 0) {

                speed = location.getSpeed();
                currentSpeedView.setText(String.valueOf(speed));


            }
            int cTime = (int) (System.currentTimeMillis() - timeStarted) / 1000;
            int min = (cTime / 60);
            int sec = (cTime % 60);
            String minString;
            String secString;
            if(min<10) {
                minString = "0" + String.valueOf(min);
            }
            else{
                minString = String.valueOf(min);

            }
            if(sec<10){
                secString = "0" +String.valueOf(sec);
            }
            else{
                secString = String.valueOf(sec);
            }
            currentTimeView.setText(minString + ":" + secString);

            currentMeterView.setText(String.valueOf(targetDist - distRemaining) + " m");

            //Aadd new information for the Stat Screen diagram.

            long diagX = System.currentTimeMillis() - timeStarted;
            float diagY = speed;
            xValues[counter] = diagX;
            yValues[counter] = diagY;

            counter++;


            if (loop) {
                if (distRemaining <= 0) {
                    playGoodSpeed.stop();
                    playHighSpeed.stop();
                    playLowSpeed.stop();

                    playFinish.start();
                    finishedButton.setEnabled(true);
                    xValues[counter] = System.currentTimeMillis() - timeStarted;
                    yValues[counter] = speed;
                    loop = false;
                } else if (speed >= goalSpeed - 1 && speed <= (goalSpeed + 1)) {
                    if (!goodSpeed && speed != 0) {
                        playGoodSpeed.start();
                        countHigh = 0;
                        countLow = 0;
                        goodSpeed = true;
                    }

                } else if (speed < goalSpeed) {
                    if (countLow == 5) {
                        playLowSpeed.start();
                        countHigh = 0;
                        goodSpeed = false;
                    }
                    countLow++;
                } else {
                    if (countHigh == 5) {
                        playHighSpeed.start();
                        goodSpeed = false;
                        countLow = 0;
                    }
                    countHigh++;

                }
                double tickTime = (System.currentTimeMillis() - lastTick) / 1000.0;
                lastTick = System.currentTimeMillis();
                distRemaining = distRemaining - (int) (speed * tickTime);
                bar.setProgress((int) (100 - 100 * distRemaining / (double) (targetDist)));

                kcalCounter += speed * tickTime;
                if (kcalCounter > 1200) {
                    kcalHelper++;
                    kcalCounter = 0;
                    if (kcalHelper == 1) {
                        play100.start();
                    } else if (kcalHelper == 2) {
                        play200.start();
                    } else if (kcalHelper == 3) {
                        play300.start();
                    } else if (kcalHelper == 4) {
                        play400.start();
                    } else if (kcalHelper == 5) {
                        play500.start();
                    }
                }


                handler.postDelayed(this, 1850);
            }

        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        /*
        playFinish.release();
        playGoodSpeed.release();
        playHighSpeed.release();
        playLowSpeed.release();
        */
    }

    public void nextPage(View view) {
        Intent intent = new Intent(this, StatScreen.class);
        intent.putExtra("targetSpeed", goalSpeed);
        intent.putExtra("xValues", xValues);
        intent.putExtra("yValues", yValues);
        intent.putExtra("targetDist", targetDist);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void resumeButton(View v){
    Toast.makeText(this,"Inte implementerad i demoversionen", Toast.LENGTH_SHORT).show();
    }
    public void pauseButton(View v){
        Toast.makeText(this,"Inte implementerad i demoversionen", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        playGoodSpeed.stop();
        playHighSpeed.stop();
        playLowSpeed.stop();
        am.playSoundEffect(AudioManager.FX_KEY_CLICK, 0.5f);
        if (sharedPreferences.getBoolean("flag", true)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();


            editor.putBoolean("flag", false);
            editor.apply();
            goalSpeed = prevSpeed;
            Log.d("Simulering", "goal speed changed");
        }
    }



}
