package se.lth.mamn01.jogr.jogr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by Filip on 2015-07-26.
 */
public class MediaButtonReceiver extends BroadcastReceiver {

    public MediaButtonReceiver() {
        super();

    }


    @Override
    public void onReceive(Context context, Intent intent) {

        KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);


        Log.d("Buttonreceiver", "Keyevent: " +keyEvent.toString());
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            if (keyEvent != null) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    SharedPreferences prefs = context.getSharedPreferences("myPrefs",
                            Context.MODE_MULTI_PROCESS);
                    SharedPreferences.Editor editor = prefs.edit();
                    if(prefs.getBoolean("flag", false)){
                        editor.putBoolean("flag", false);
                        editor.apply();
                    }

                    editor.putBoolean("flag", true);
                    editor.apply();

                }
            }
        }

    }
}