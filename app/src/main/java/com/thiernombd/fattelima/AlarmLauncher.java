package com.thiernombd.fattelima;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import static java.security.AccessController.getContext;

/**
 * Created by Thierno_M_B_DIALLO on 13/12/2017.
 */

public class AlarmLauncher extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_launcher_layout);

        MediaPlayer media = new MediaPlayer();
        try {

            //  /mnt/sdcard
            // /Music/slow/
            //Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/a.mp3"); Uri.parse("file://sdcard/a.mp3");
            String uri = Environment.getExternalStorageDirectory().getAbsolutePath();
            Toast.makeText(AlarmLauncher.this,""+uri,Toast.LENGTH_LONG).show();
            media.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/a.mp3");

            media.prepareAsync();
            media.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


//    public AlarmLauncher() {
//        super("alarmlauncher_super");
//    }
//
//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//
//    }

}
