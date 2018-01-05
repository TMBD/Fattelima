package com.thiernombd.fattelima;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.thiernombd.AlarmsDB.AlarmDBHandler;
import com.thiernombd.AlarmsDB.AlarmDBManager;
import com.thiernombd.fattelima_class.Alarm;
import com.thiernombd.fattelima_class.AlarmView;

import java.sql.SQLDataException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.AlarmManager.RTC;
import static android.app.AlarmManager.RTC_WAKEUP;

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

        //Chargement des alamrs dans le rv
        AlarmView.loadAlarmViewsOnRV(homeRecyclerView, this);

        //homeRecyclerView.setAnimation(new DefaultItemAnimator());
        ////////////////////////////////////////////////////////////////////////////////////////////



        /*AlarmDBManager dbManager = new AlarmDBManager(AlarmDBHandler.DBNAME, this, AlarmDBHandler.VERSION);
        dbManager.openWritableDB();
        //dbManager.insertAlarmView(AlarmView.getAlarmDataList().get(1));
        List<AlarmView> alV = dbManager.selectAlarmView();
        //if( ! alV.isEmpty())
        adapter.addAlarmIn_rv(0, alV.get(0));
        Log.i("TTTTTT",String.valueOf(alV.get(2).id));
        Log.i("AlarmV",AlarmDBHandler.ALARM_VIEW_T_CREATE);
        Log.i("Alarm",AlarmDBHandler.ALARM_T_CREATE);
        dbManager.close();*/
       /* try {
            dbManager.openWritableDB();
        }catch (SQLiteException e){
            e.printStackTrace();
            //Log.e("erre",e.printStackTrace());
        }*/




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

                        String hour = String.valueOf(alarmeTimePicker.getCurrentHour());
                        hour = hour.length() > 1 ? hour : "0"+hour;
                        String min = String.valueOf(alarmeTimePicker.getCurrentMinute());
                        min = min.length() > 1 ? min : "0"+min;

                        timeToDisplay = hour + AlarmView.HOURSEPARATOR + min + AlarmView.MIN;
                        long alarmTimeInMillis = Alarm.getTimeInMillis(alarmeTimePicker);
                        int serviceId = AlarmView.getNewServiceId(HomeActivity.this);
                        AlarmView newAlarmView = new AlarmView(serviceId, timeToDisplay, repeteDaysChoised, fileName_tv.getText().toString(), libelle_et.getText().toString());

                        //ajout a la fois dans la vue et dans la base de donnees
                        long alarmViewID = AlarmView.addAlarmView_DBView(newAlarmView, v.getContext());
                        //Alarm(String millis, boolean repetition, boolean status, long alarmViewId)
                        Alarm alarm = new Alarm(serviceId, String.valueOf(alarmTimeInMillis), repeter_cb.isChecked(), true, alarmViewID);
                        Alarm.addAlarm_DBView(alarm, daysCheked, v.getContext());


                        alarmeDialog.hide();

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
