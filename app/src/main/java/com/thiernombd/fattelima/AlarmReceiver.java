package com.thiernombd.fattelima;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.thiernombd.fattelima_class.Alarm;

import java.io.IOException;
import java.util.Calendar;

import static android.app.AlarmManager.ELAPSED_REALTIME;
import static android.app.AlarmManager.RTC;

/**
 * Created by Thierno_M_B_DIALLO on 15/12/2017.
 */

public class AlarmReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        //intent.getAction().equals(Intent.ACTION_SCREEN_ON)||intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)||intent.getAction().equals(Intent.ACTION_USER_PRESENT)
        //if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)||intent.getAction().equals(Intent.ACTION_LOCKED_BOOT_COMPLETED)) {

        Toast.makeText(context,"BIEN DEMARRE 111111111111111 !",Toast.LENGTH_LONG).show();
        Alarm.loadAlarmsOnServices(context);
        Toast.makeText(context,"BIEN DEMARRE 2222222222222222 !",Toast.LENGTH_LONG).show();

            ////////////////////////////////////////////////////////////////////////////

            //MediaPlayer media = new MediaPlayer();

                //
                //  /mnt/sdcard
                // /Music/slow/
                //Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/a.mp3"); Uri.parse("file://sdcard/a.mp3");
                //String uri = Environment.getExternalStorageDirectory().getAbsolutePath();
                //Toast.makeText(AlarmRingtoneActivity.this,""+uri,Toast.LENGTH_LONG).show();
                //media.setDataSource("/mnt/sdcard/a.mp3");


                /*AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setSpeakerphoneOn(true);

                MediaPlayer media = MediaPlayer.create(context, R.raw.akbar);
                media.setAudioStreamType(AudioManager.STREAM_MUSIC);

                //media.setAudioStreamType(AudioManager.STREAM_ALARM);
                //media.setAudioStreamType(AudioManager.STREAM_RING);

                //media.prepare();
                media.start();*/



            ///////////////////////////////////////////////////////////////////////////////::::
        /*final AlertDialog.Builder daysAlertDialog = new AlertDialog.Builder(context);
        daysAlertDialog.setTitle("TEST")
                .setMessage("JUST FOR TESTING")
                .show();*/

        /*Intent alarmRingtoneActivityIntent = new Intent(context,AlarmRingtoneActivity.class);
        PendingIntent alarmLauncherPendingIntent = PendingIntent.getActivity(context,0,alarmRingtoneActivityIntent,0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();

        manager.set(RTC, calendar.getTimeInMillis()+60000, alarmLauncherPendingIntent);*/



            //startActivity(alarmRingtoneActivityIntent);


            //Toast.makeText(context,alarmTimeInMillis+"" ,Toast.LENGTH_LONG).show();
            //Toast.makeText(HomeActivity.this,String.valueOf(tomorrow) ,Toast.LENGTH_LONG).show();
            //manager.set(RTC_WAKEUP, System.currentTimeMillis()+1000, alarmLauncherPendingIntent);





            /*Intent activityIntent = new Intent(context, MyActivity.class);
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);*/



    }
}
