package se.lth.mamn01.jogr.jogr;

/**
 * Created by Strom on 2015-04-20.
 */

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class StatScreen extends Activity implements ShakeEventListener.OnShakeListener {

    private SensorManager mSensorManager;

    private ShakeEventListener mSensorListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate i StatScreen");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener(this.getApplicationContext());

        mSensorListener.setOnShakeListener(this);


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
    public void onShake() {
        TextView txt = (TextView) this.findViewById(R.id.text);
        txt.setText("Skakad!");
        Toast.makeText(getApplicationContext(), "Shake!", Toast.LENGTH_SHORT).show();
    }
}
