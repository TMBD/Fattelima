package com.thiernombd.fattelima;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Thierno_M_B_DIALLO on 15/12/2017.
 */

public class AlarmRingtoneActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_ringtone_layout);


        MediaPlayer media = new MediaPlayer();
        try {
            //
            //  /mnt/sdcard
            // /Music/slow/
            //Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/a.mp3"); Uri.parse("file://sdcard/a.mp3");
            String uri = Environment.getExternalStorageDirectory().getAbsolutePath();
            //Toast.makeText(AlarmRingtoneActivity.this,""+uri,Toast.LENGTH_LONG).show();
            media.setDataSource("/mnt/sdcard/a.mp3");

            media.prepare();
            media.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
