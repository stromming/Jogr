package se.lth.mamn01.jogr.jogr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.os.Handler;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.media.MediaPlayer;

/**
 * Created by Filip on 2015-04-21.
 */
public class Simulering extends Activity{
    NumberPicker speedPicker;
    String[] values;
    Handler handler;
   // CheckBox checkbox;
    MediaPlayer playFinish;
    MediaPlayer playGoodSpeed;
    MediaPlayer playHighSpeed;
    MediaPlayer playLowSpeed;
    int dist;
    int time;
    int speed;
    int countGood,countLow,countHigh;
    int mediumSpeed;
    boolean loop;
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate i Simulering");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulering);
       // checkbox = (CheckBox)this.findViewById(R.id.checkBox);
        speedPicker = (NumberPicker)findViewById(R.id.numberPicker);
        speedPicker.setMaxValue(15);
        speedPicker.setMinValue(0);
        playFinish = MediaPlayer.create(this, R.raw.finish);
        playGoodSpeed = MediaPlayer.create(this, R.raw.goodspeed);
        playHighSpeed = MediaPlayer.create(this,R.raw.highspeed);
        playLowSpeed = MediaPlayer.create(this,R.raw.lowspeed);
        Bundle extras = getIntent().getExtras();

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
        loop=true;
        dist = 1000 * Integer.parseInt(values[0]);
        time = Integer.parseInt(values[1]);
        mediumSpeed= dist/time;
        System.out.println("Before loop "+dist+" : "+time);
        handler = new Handler();

        handler.postDelayed(runnable, 100);





    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countLow=countLow%4;
            countGood=countGood%4;
            countHigh=countHigh%4;
      /* do what you need to do */
            speed=speedPicker.getValue();
            System.out.println("loop "+dist+" : "+time +" : "+speed);
        if(loop) {
            if (dist <= 0) {
                playFinish.start();
                loop=false;
            }else if(speed>= mediumSpeed && speed<=(mediumSpeed+2)){
                if(countGood==0) {
                    playGoodSpeed.start();
                    countHigh=0;
                    countHigh=0;
                }
                countGood++;
            }else if(speed<mediumSpeed){
                if(countLow==0) {
                    playLowSpeed.start();
                    countHigh=0;
                    countGood=0;
                }
                countLow++;
            }else {
                if(countHigh==0){
                    playHighSpeed.start();
                    countGood=0;
                    countLow=0;
                }
                countHigh++;

            }
            dist= dist-(speed*5);
            handler.postDelayed(this, 500);
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
        System.out.println("pressed");
        Log.d("test", "Pressed");
        Intent intent = new Intent(this, StatScreen.class);
        startActivity(intent);
    }
}
