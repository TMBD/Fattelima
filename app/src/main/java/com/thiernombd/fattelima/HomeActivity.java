package com.thiernombd.fattelima;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.thiernombd.fattelima_class.Alarm;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.AlarmManager.ELAPSED_REALTIME;
import static android.app.AlarmManager.RTC;

public class HomeActivity extends AppCompatActivity {

    MyAdapter adapter;
    AlertDialog alarmeDialog;
    String timeToDisplay;
    CharSequence[] daysOfWeek = {"Lun.","Mar.","Mer.","Jeu.","Ven.","Sam.","Dim."};
    SparseBooleanArray daysCheked = new SparseBooleanArray(7);
    String repeteDaysChoised = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //******************************************************************************************
        ///////////////////////RECYCLERVIEW/////////////////////////////////////////////////////////
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);

        final RecyclerView homeRecyclerView = (RecyclerView) findViewById(R.id.listAlarm_rv);
        homeRecyclerView.setLayoutManager(layout);
        List<Alarm> al = Alarm.getAlarmDataList();
        adapter = new MyAdapter(al);
        homeRecyclerView.setAdapter(adapter);
        //homeRecyclerView.setAnimation(new DefaultItemAnimator());
        ////////////////////////////////////////////////////////////////////////////////////////////

        //******************************************************************************************
        ////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////ANDROID STUDIO///////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alarmeAlertDialog = new AlertDialog.Builder(HomeActivity.this);
                View alarmeCreator_v = getLayoutInflater().inflate(R.layout.alarme_dialog_contents,null);
                final TimePicker alarmeTimePicker = (TimePicker) alarmeCreator_v.findViewById(R.id.alarmeTimeChooser_tp);
                final EditText libelle_et = (EditText) alarmeCreator_v.findViewById(R.id.libelle_et);
                final CheckBox repeter_cb = (CheckBox) alarmeCreator_v.findViewById(R.id.repeter_cb);
                final TextView alarmeRepeteDays_tv = (TextView) alarmeCreator_v.findViewById(R.id.alarmeRepeteDays_tv);
                final ImageButton soundChooser_img = (ImageButton) alarmeCreator_v.findViewById(R.id.soundChooser_img);
                final TextView fileName_tv = (TextView) alarmeCreator_v.findViewById(R.id.alarmeSoudFile_tv);
                final Button alarmeTerminer_bt = (Button) alarmeCreator_v.findViewById(R.id.validerAlarm_bt);
                alarmeTimePicker.setIs24HourView(true);

                alarmeAlertDialog.setView(alarmeCreator_v);
                alarmeDialog = alarmeAlertDialog.create();
                alarmeDialog.show();
                alarmeTerminer_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeToDisplay = alarmeTimePicker.getCurrentHour()+"h:"+alarmeTimePicker.getCurrentMinute()+"min";
                        int hour =  alarmeTimePicker.getCurrentHour();
                        int minutes =  alarmeTimePicker.getCurrentMinute();
                        long timeInSecond = (hour*60 + minutes)*60;
                        long currentTimeInMillis= System.currentTimeMillis();
                        long now = (Calendar.getInstance().getTime().getMinutes() + Calendar.getInstance().getTime().getHours()*60)*60;
                        long offset = timeInSecond - now;
                        long differenceOfTime = timeInSecond-Calendar.getInstance().getTimeInMillis()/1000;

                        Alarm newAlarm = new Alarm(timeToDisplay,libelle_et.getText().toString(),fileName_tv.getText().toString(),repeteDaysChoised);
                        adapter.addAlarmIn_rv(0,newAlarm);
                        Toast.makeText(v.getContext(),"Alarm dans "+offset/60+" min",Toast.LENGTH_SHORT).show();
                        alarmeDialog.hide();


                        //////////////////////////// ALARMS//////////////////////
                        Intent alarmLauncherIntent = new Intent(HomeActivity.this,AlarmLauncher.class);
                        PendingIntent alarmLauncherPendingIntent = PendingIntent.getActivity(HomeActivity.this,0,alarmLauncherIntent,0);
                        AlarmManager manager = (AlarmManager) HomeActivity.this.getSystemService(Context.ALARM_SERVICE);
                        manager.set(RTC, System.currentTimeMillis()+1000, alarmLauncherPendingIntent);


                    }
                });

                repeter_cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (repeter_cb.isChecked()){

                            repeter_cb.setChecked(false);
                            daysCheked.append(0, false);
                            daysCheked.append(1, false);
                            daysCheked.append(2, false);
                            daysCheked.append(3, false);
                            daysCheked.append(4, false);
                            daysCheked.append(5, false);
                            daysCheked.append(6, false);
                            final AlertDialog.Builder daysAlertDialog = new AlertDialog.Builder(HomeActivity.this);
                            daysAlertDialog.setTitle("Choix des jours")
                                .setMultiChoiceItems(daysOfWeek, null, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        daysCheked.put(which, isChecked);
                                    }
                                })
                                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        repeter_cb.setChecked(false);
                                    }
                                })
                                .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        repeteDaysChoised = "";
                                        for (int i = 0; i < 7; i++) {
                                            repeteDaysChoised += daysCheked.get(i) ? daysOfWeek[i] + " " : "";
                                        }
                                        if(!repeteDaysChoised.equals("")){
                                            repeter_cb.setChecked(true);
                                            alarmeRepeteDays_tv.setVisibility(View.VISIBLE);
                                            alarmeRepeteDays_tv.setText(repeteDaysChoised);
                                            Toast.makeText(HomeActivity.this, "Repetition : "+repeteDaysChoised, Toast.LENGTH_SHORT).show();
                                        }else Toast.makeText(HomeActivity.this, "Pas de repetition", Toast.LENGTH_SHORT).show();
                                    }
                                }) //fin du .setPositiveButton
                                .show();
                        } // fin du if repeter_cb.isChecked()
                        else{
                            alarmeRepeteDays_tv.setText("");
                            alarmeRepeteDays_tv.setVisibility(View.INVISIBLE);
                        }
                    } //Fin du On click de repeter_cb
                }); // fin du repeter_cb.setOnClickListener

            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
    }


    //**********************************************************************************************
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////ANDROID STUDIO///////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
