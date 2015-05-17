package se.lth.mamn01.jogr.jogr;

/**
 * Created by Strom on 2015-04-20.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class StatScreen extends Activity {

    private int graphMaxX;
    private SensorManager mSensorManager;
    private GraphView graph;
    private ShakeEventListener mSensorListener;
    private int statCycler = 0;
    private int targetSpeed;
    private int targetDist;
private LineGraphSeries<DataPoint> series;
    private LineGraphSeries<DataPoint> targetSeries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_stats_0);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            public void onShakeForward(){
                Toast.makeText(getApplicationContext(), "Forward!", Toast.LENGTH_SHORT).show();
            }
            public void onShakeBack(){
                Toast.makeText(getApplicationContext(), "Back!", Toast.LENGTH_SHORT).show();
            }
            public void onShakeLeft() {
             Toast.makeText(getApplicationContext(), "Left!", Toast.LENGTH_SHORT).show();
             cycleStatsLeft();
            }
            public void onShakeRight(){
             Toast.makeText(getApplicationContext(), "Right!", Toast.LENGTH_SHORT).show();
             cycleStatsRight();
            }
        });


        Bundle extras = getIntent().getExtras();
        long[] xValues = extras.getLongArray("xValues");
        long[] yValues = extras.getLongArray("yValues");
        targetSpeed = extras.getInt("targetSpeed");
        targetDist = extras.getInt("targetDist");
        int entries = 0;
        for(int i = 0; i< xValues.length; i++){
            if (xValues[i] == 0){
                entries = i;
                break;
            }
        }

        DataPoint[] points = new DataPoint[entries];

        for(int i = 0; i<entries; i++){
            points[i] = new DataPoint(xValues[i]/1000, yValues[i]);

        }
        graphMaxX = (int) (xValues[entries-1]/1000);
        DataPoint[] targetPoints = new DataPoint[2];
        targetPoints[0]= new DataPoint(0,targetSpeed);
        targetPoints[1] = new DataPoint(graphMaxX, targetSpeed);
        targetSeries = new LineGraphSeries<DataPoint>(targetPoints);
        series = new LineGraphSeries<DataPoint>(points);
        targetSeries.setThickness(2);
        targetSeries.setColor(Color.RED);
        setLayout();
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

    public void reloadGraph(){
        graph = (GraphView) findViewById(R.id.graph);

        graph.addSeries(series);
        graph.addSeries(targetSeries);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxX(graphMaxX);
        graph.getViewport().setMinY(0);
        graph.getViewport().setScalable(true);

        graph.getViewport().setScrollable(true);
    }
public void setLayout(){
    if(statCycler == 0){
        setContentView(R.layout.activity_stats_0);
        TextView text = (TextView) findViewById(R.id.text0);
        text.setText("Du sprang " + targetDist + " meter på " + graphMaxX + " sekunder. Bra jobbat!");
    }
    if(statCycler == 1){
        setContentView(R.layout.activity_stats_1);
        TextView text = (TextView) findViewById(R.id.text1);
        int kcalBurned = 83*targetDist/1000;
        String comparison;
        if(kcalBurned < 100)
            comparison = "liten";
        else if(kcalBurned < 200)
            comparison = "ganska liten";
        else if(kcalBurned < 500)
            comparison = "normalstor";

     else if(kcalBurned < 1000)
        comparison = "jättestor";

        else
        comparison = "alldeles onödigt stor";


        text.setText("Du brände " + 83*targetDist/1000 + " kcal! Det är lika mycket som en " +comparison+ " skinkmacka!");
    }
    if(statCycler == 2){
        setContentView(R.layout.activity_stats_2);
        reloadGraph();
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

}
