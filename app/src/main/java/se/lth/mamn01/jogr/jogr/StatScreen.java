package se.lth.mamn01.jogr.jogr;

/**
 * Created by Strom on 2015-04-20.
 */

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class StatScreen extends Activity {

    private SensorManager mSensorManager;

    private ShakeEventListener mSensorListener;
    private int statCycler = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate i StatScreen");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_0);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShakeLeft() {
             Toast.makeText(getApplicationContext(), "Left!", Toast.LENGTH_SHORT).show();
             cycleStatsLeft();
            }
            public void onShakeRight(){
             Toast.makeText(getApplicationContext(), "Right!", Toast.LENGTH_SHORT).show();
             cycleStatsRight();
            }
        });


    }
public void cycleStatsRight() {
    statCycler++;
    if (statCycler == 3) {
        statCycler = 0;
    }
    setLayout();
}

    public void cycleStatsLeft(){
        statCycler--;
        if(statCycler == -1){
            statCycler = 2;
        }
        setLayout();
    }
public void setLayout(){
    if(statCycler == 0){
        setContentView(R.layout.activity_stats_0);
    }
    if(statCycler == 1){
        setContentView(R.layout.activity_stats_1);
    }
    if(statCycler == 2){
        setContentView(R.layout.activity_stats_2);
    }

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
    public void shareToFacebook(View view){
        Toast.makeText(getApplicationContext(), "Shared!", Toast.LENGTH_SHORT).show();
    }
}
