package se.lth.mamn01.jogr.jogr;

/**
 * Created by Strom on 2015-04-20.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.GpsSatellite;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
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
    private int statCyclerX = 0;
    private int statCyclerZ = 0;
    private int targetSpeed;
    private int targetDist;
    private Vibrator vib;
    private LineGraphSeries<DataPoint> series;
    private LineGraphSeries<DataPoint> targetSeries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stats_0);
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            public void onShakeForward() {
                cycleStatsForward();
            }

            public void onShakeBack() {
                cycleStatsBack();
            }

            public void onShakeLeft() {
                statCyclerZ = 0;
                cycleStatsLeft();
            }

            public void onShakeRight() {
                statCyclerZ = 0;
                cycleStatsRight();
            }
        });


        Bundle extras = getIntent().getExtras();

        long[] xValues = extras.getLongArray("xValues");
        float[] yValues = extras.getFloatArray("yValues");
        targetSpeed = extras.getInt("targetSpeed");
        targetDist = extras.getInt("targetDist");
        int entries = 0;
        for (int i = 0; i < xValues.length; i++) {
            if (xValues[i] == 0) {
                entries = i;
                break;
            }
        }

        DataPoint[] points = new DataPoint[entries];

        for (int i = 0; i < entries; i++) {
            points[i] = new DataPoint(xValues[i] / 1000, yValues[i]);

        }
        graphMaxX = (int) (xValues[entries - 1] / 1000);
        DataPoint[] targetPoints = new DataPoint[2];
        targetPoints[0] = new DataPoint(0, targetSpeed);
        targetPoints[1] = new DataPoint(graphMaxX, targetSpeed);
        targetSeries = new LineGraphSeries<DataPoint>(targetPoints);
        series = new LineGraphSeries<DataPoint>(points);
        targetSeries.setThickness(2);
        targetSeries.setColor(Color.RED);
        setLayout();
    }

    public void cycleStatsForward() {
        if (statCyclerZ == 0) {
            vib.vibrate(100);
            Toast.makeText(getApplicationContext(), "Delat!",
                    Toast.LENGTH_SHORT).show();

        } else if (statCyclerZ == 1) {
            vib.vibrate(100);
            statCyclerZ = 0;
            setLayout();
        }


    }

    public void cycleStatsBack() {
        if (statCyclerZ == 0) {
            vib.vibrate(100);
            statCyclerZ = 1;
            setLayout();
        }

    }

    public void cycleStatsRight() {
        vib.vibrate(100);
        statCyclerX++;
        if (statCyclerX == 3) {
            statCyclerX = 0;
        }
        setLayout();
    }

    public void cycleStatsLeft() {
        vib.vibrate(100);
        statCyclerX--;
        if (statCyclerX == -1) {
            statCyclerX = 2;
        }
        setLayout();
    }

    public void reloadGraph() {
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

    public void setLayout() {

        if (statCyclerX == 0) {
            if (statCyclerZ == 0) {
                setContentView(R.layout.activity_stats_0);
                TextView text = (TextView) findViewById(R.id.text0);
                TextView text3 = (TextView) findViewById(R.id.text3);
                text.setText(targetDist + " Meter");
                text3.setText(graphMaxX + " Sekunder");
            } else if (statCyclerZ == 1) {
                setContentView(R.layout.activity_stats_0b);
            }
        }
        if (statCyclerX == 1) {
            if (statCyclerZ == 0) {
                setContentView(R.layout.activity_stats_1);
                TextView text = (TextView) findViewById(R.id.text1);
                int kcalBurned = 83 * targetDist / 1000;
                String comparison;
                if (kcalBurned < 100)
                    comparison = "liten";
                else if (kcalBurned < 200)
                    comparison = "ganska liten";
                else if (kcalBurned < 500)
                    comparison = "normalstor";

                else if (kcalBurned < 1000)
                    comparison = "jättestor";

                else
                    comparison = "alldeles onödigt stor";


                text.setText("Du brände " + 83 * targetDist / 1000 + " kcal! Det är lika mycket som en " + comparison + " skinkmacka!");
            } else if (statCyclerZ == 1) {
                setContentView(R.layout.activity_stats_1b);
            }
        }
        if (statCyclerX == 2) {
            if (statCyclerZ == 0) {
                setContentView(R.layout.activity_stats_2);
                reloadGraph();
            } else if (statCyclerZ == 1) {
                setContentView(R.layout.activity_stats_2b);
            }
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
