package se.lth.mamn01.jogr.jogr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.os.Handler;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.media.MediaPlayer;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Filip on 2015-04-21.
 */
public class Simulering extends Activity implements LocationListener{
    long timeStarted;
    NumberPicker speedPicker;
    String[] values;
    Handler handler;
   // CheckBox checkbox;
    MediaPlayer playFinish;
    MediaPlayer playGoodSpeed;
    MediaPlayer playHighSpeed;
    MediaPlayer playLowSpeed;
    MediaPlayer play100;
    MediaPlayer play200;
    MediaPlayer play300;
    MediaPlayer play400;
    MediaPlayer play500;
    int dist;
    int time;
    int speed;
    int countGood,countLow,countHigh;
    int mediumSpeed;
    boolean loop;
    int prevSpeed;
    int targetDist;
    private Button finishedButton;
    private ProgressBar bar;
    private int counter;
    long[] xValues;
    long[] yValues;
    boolean goodSpeed;
    int kcalCounter = 0;
    int kcalHelper = 0;
    Location location;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulering);
       // checkbox = (CheckBox)this.findViewById(R.id.checkBox);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        speedPicker = (NumberPicker)findViewById(R.id.numberPicker);
        speedPicker.setMaxValue(15);
        speedPicker.setMinValue(0);
        playFinish = MediaPlayer.create(this, R.raw.finish);
        playGoodSpeed = MediaPlayer.create(this, R.raw.goodspeed);
        playHighSpeed = MediaPlayer.create(this,R.raw.highspeed);
        playLowSpeed = MediaPlayer.create(this,R.raw.lowspeed);
        play100 = MediaPlayer.create(this,R.raw.one);
        play200 = MediaPlayer.create(this,R.raw.two);
        play300 = MediaPlayer.create(this,R.raw.three);

        play400 = MediaPlayer.create(this,R.raw.four);

        play500 = MediaPlayer.create(this,R.raw.five);

        Bundle extras = getIntent().getExtras();
        finishedButton = (Button)findViewById(R.id.button);
        finishedButton.setEnabled(false);
        countGood=0;
        countLow=0;
        countHigh =0;
        if (extras != null) {
            String value = extras.getString("variables");
            values =value.split("[ ]");
        }else{
            values[0]="0";
            values[1]="0";
        }
        xValues = new long[100];
        yValues = new long[100];
        counter = 0;
        loop=true;
        timeStarted = System.currentTimeMillis();
        dist = Integer.parseInt(values[0]);
        targetDist = dist;
        time = Integer.parseInt(values[1]);
        mediumSpeed= dist/time;
        TextView targetSpeed = (TextView)findViewById(R.id.target_speed);
        targetSpeed.setText("Målhastighet: " +mediumSpeed);
        System.out.println("Before loop "+dist+" : "+time);

        bar = (ProgressBar) findViewById(R.id.progressBar1);
        bar.setVisibility(View.VISIBLE);
        bar.setScaleY(3.0f);
        bar.setProgress(0);
        bar.setLeft(1);

        handler = new Handler();

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        this.onLocationChanged(null);

        handler.postDelayed(runnable, 100);





    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countLow=countLow%4;
countGood = countGood%4;
            countHigh=countHigh%4;
      /* do what you need to do */
            prevSpeed = speed;
            speed = (int) location.getSpeed();

            if(prevSpeed != speed){
                long diagX = System.currentTimeMillis() - timeStarted;
                long diagY = speed;
                xValues[counter] = diagX;
                yValues[counter] = diagY;
                System.out.println(diagX);
                System.out.println(diagY);

                counter++;

            }

        if(loop) {
            if (dist <= 0) {
                playGoodSpeed.stop();
                playHighSpeed.stop();
                playLowSpeed.stop();

                playFinish.start();
                finishedButton.setEnabled(true);
                xValues[counter] = System.currentTimeMillis()-timeStarted;
                yValues[counter] = speed;
                loop=false;
            }else if(speed>= mediumSpeed-1 && speed<=(mediumSpeed+1)){
                if(!goodSpeed && speed != 0) {
                    playGoodSpeed.start();
                    countHigh=0;
                    countLow=0;
                    goodSpeed = true;
                }

            }else if(speed<mediumSpeed){
                if(countLow==0) {
                    playLowSpeed.start();
                    countHigh=0;
                    goodSpeed = false;
                }
                countLow++;
            }else {
                if(countHigh==0){
                    playHighSpeed.start();
                   goodSpeed = false;
                    countLow=0;
                }
                countHigh++;

            }
            dist= dist-(speed*2);
            bar.setProgress((int)(100-100*dist/(double)(targetDist)));
kcalCounter += speed*2;
            if(kcalCounter>1200){
                kcalHelper++;
                kcalCounter=0;
                if(kcalHelper==1){
                    play100.start();
                }
                else if(kcalHelper==2){
                    play200.start();
                }
                else if(kcalHelper==3){
                    play300.start();
                }
                else if(kcalHelper==4){
                    play400.start();
                }
                else if(kcalHelper==5){
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
        playFinish.release();
        playGoodSpeed.release();
        playHighSpeed.release();
        playLowSpeed.release();
    }

    public void nextPage(View view){
        Intent intent = new Intent(this, StatScreen.class);
        intent.putExtra("targetSpeed", mediumSpeed);
        intent.putExtra("xValues", xValues);
        intent.putExtra("yValues", yValues);
        intent.putExtra("targetDist", targetDist);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location=location;
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
}
