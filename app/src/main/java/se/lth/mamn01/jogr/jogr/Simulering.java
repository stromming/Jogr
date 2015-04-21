package se.lth.mamn01.jogr.jogr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.os.Handler;
import android.widget.EditText;
/**
 * Created by Filip on 2015-04-21.
 */
public class Simulering extends Activity{
    EditText speed;
    EditText goal;
    Handler handler;
    CheckBox checkbox;
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate i Simulering");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulering);
        checkbox = (CheckBox)this.findViewById(R.id.checkBox);
        speed = (EditText)findViewById(R.id.editText);
        goal = (EditText)findViewById(R.id.editText2);
        handler = new Handler();
        handler.postDelayed(runnable, 100);





    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
        //System.out.println("speed: "+speedstring+" och goal: "+goalstring);


            String speedstring = speed.getText().toString();
            String goalstring = goal.getText().toString();

            if(!speedstring.isEmpty()&&!goalstring.isEmpty()) {
                int speedint = Integer.parseInt(speedstring);
                int goalint = Integer.parseInt(goalstring);
                if (speedint >= goalint) {
                    checkbox.setChecked(true);
                } else {
                    checkbox.setChecked(false);
                }
            }
      /* delay */
            handler.postDelayed(this, 100);
        }
    };


  /*  private void checkBox(){



         speed = (String)findViewById(R.id.editText).toString();
         goal = (String)findViewById(R.id.editText2).toString();
          if(speed!=null && goal!=null&&speed.compareTo(goal)<=0){
           checkbox.setChecked(true);
          }else{
              checkbox.setChecked(false);
          }


      }*/



    public void nextPage(View view){
        System.out.println("pressed");
        Log.d("test", "Pressed");
        Intent intent = new Intent(this, StatScreen.class);
        startActivity(intent);
    }
}
