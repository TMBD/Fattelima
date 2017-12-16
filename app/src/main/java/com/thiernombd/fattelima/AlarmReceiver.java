package com.thiernombd.fattelima;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Thierno_M_B_DIALLO on 15/12/2017.
 */

public class AlarmReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"BIEN DEMARRE !",Toast.LENGTH_LONG).show();
    }
}
