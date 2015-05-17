package se.lth.mamn01.jogr.jogr;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;


public class DistSpeed extends ActionBarActivity {
    NumberPicker hPicker;
    NumberPicker minPicker;
    EditText dist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distspeed);
        dist =(EditText)findViewById(R.id.editText5);
        hPicker = (NumberPicker)findViewById(R.id.hPicker);
        hPicker.setMaxValue(10);
        hPicker.setMinValue(0);

        minPicker = (NumberPicker)findViewById(R.id.minPicker);
        minPicker.setMaxValue(59);
        minPicker.setMinValue(0);
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
    public void nextPage(View view){

        Intent intent = new Intent(this, Simulering.class);
        String distString = dist.getText().toString();
        int time = hPicker.getValue()* 3600+ minPicker.getValue()*60;

        intent.putExtra("variables", distString +" "+time);

        startActivity(intent);
    }
}
