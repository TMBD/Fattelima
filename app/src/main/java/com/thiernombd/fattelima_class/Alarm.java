package com.thiernombd.fattelima_class;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.TimePicker;
import android.widget.Toast;

import com.thiernombd.AlarmsDB.AlarmDBHandler;
import com.thiernombd.AlarmsDB.AlarmDBManager;
import com.thiernombd.fattelima.AlarmRingtoneActivity;
import com.thiernombd.fattelima.HomeActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.AlarmManager.INTERVAL_DAY;
import static android.app.AlarmManager.RTC_WAKEUP;

/**
 * Created by Thierno_M_B_DIALLO on 23/11/2017.
 */

public class Alarm {
    public long id = 0;  //null pour l'insertion
    public int alarmViewServiceId = 0;
    public String millis;
    public boolean repetition;
    public boolean status;
    public long alarmViewId = 0;
    public static long _24HMILIS = 24*60*60*1000;

    //public String ringFile=null;
    //private enum daysChecked{Lun,Mar,Mer,Jeu,Ven,Sam,Dim};
    //public String repeteDays[]=null;
    //public String repeteDays=null;

    public Alarm(long id, int alarmViewServiceId, String millis, boolean repetition, boolean status, long alarmViewId) {
        this.id = id;
        this.alarmViewServiceId = alarmViewServiceId;
        this.millis = millis;
        this.repetition = repetition;
        this.status = status;
        this.alarmViewId = alarmViewId;
    }

    public Alarm(int alarmViewServiceId, String millis, boolean repetition, boolean status, long alarmViewId) {
        this.alarmViewServiceId = alarmViewServiceId;
        this.millis = millis;
        this.repetition = repetition;
        this.status = status;
        this.alarmViewId = alarmViewId;
    }

    public static void loadAlarmsOnServices(Context context){
        AlarmDBManager dbManager = new AlarmDBManager(AlarmDBHandler.DBNAME, context, AlarmDBHandler.VERSION);
        dbManager.openReadableDB();
        List<Alarm> alView = dbManager.selectAlarm();
        dbManager.close();

        for(int i = 0; i<alView.size(); i++){
            activeAlarm(alView.get(i), context);
        }


    }

    //Ajouter un type Alarm dans la base de données et dans les services en prenant en compte les repetitions
    public static void addAlarm_DBView(Alarm al, SparseBooleanArray daysChecked, Context context){
        AlarmDBManager dbManager = new AlarmDBManager(AlarmDBHandler.DBNAME, context, AlarmDBHandler.VERSION);
        ArrayList<Alarm> arrayAlarms = getAlarmsFromAlarm(al, daysChecked);
        dbManager.openWritableDB();
        for(int i = 0; i<arrayAlarms.size(); i++){
            activeAlarm(arrayAlarms.get(i), context);
            dbManager.insertAlarm(arrayAlarms.get(i));
        }
        dbManager.close();
    }

    /*
    * Permet d'obtenir toutes les repetitions d'une Alarm pour pouvoir les utiliser comme des services
    * */
    public static ArrayList<Alarm> getAlarmsFromAlarm(Alarm al, SparseBooleanArray daysChecked){
        ArrayList<Alarm> arrayAlarms = new ArrayList<Alarm>();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.valueOf(al.millis));
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if(day == Calendar.MONDAY) day = 0;
        else if(day == Calendar.TUESDAY) day = 1;
        else if(day == Calendar.WEDNESDAY) day = 2;
        else if(day == Calendar.THURSDAY) day = 3;
        else if(day == Calendar.FRIDAY) day = 4;
        else if(day == Calendar.SATURDAY) day = 5;
        else if(day == Calendar.SUNDAY) day = 6;
        int j;
        if(al.repetition){
            for(int i = 0; i<daysChecked.size(); i++){
                if(daysChecked.get(i)){
                    j = day > i ? day - i : i - day;
                    arrayAlarms.add(new Alarm(al.alarmViewServiceId, String.valueOf(Long.valueOf(al.millis) + j * _24HMILIS), true, true, al.alarmViewId));
                }
            }
        }else arrayAlarms.add(new Alarm(al.alarmViewServiceId, al.millis, false, true, al.alarmViewId));

        return arrayAlarms;
    }

    public static long getTimeInMillis(TimePicker alarmeTimePicker){
        alarmeTimePicker.setIs24HourView(false);
        int alarmHour =  alarmeTimePicker.getCurrentHour();
        int alarmMinutes =  alarmeTimePicker.getCurrentMinute();
        Calendar calendar = Calendar.getInstance();
        Date dt = calendar.getTime();
        int tomorrow = alarmHour > dt.getHours() || (alarmHour == dt.getHours() && alarmMinutes > dt.getMinutes()) ? 0 : 1;
        calendar.set(Calendar.AM_PM,Calendar.AM);
        calendar.set(Calendar.HOUR,alarmeTimePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE,alarmeTimePicker.getCurrentMinute());
        calendar.set(Calendar.SECOND,0);
        return  calendar.getTimeInMillis() + tomorrow*24*60*60*1000;

        //calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),alarmeTimePicker.getCurrentHour(),alarmeTimePicker.getCurrentMinute());
        //calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
        //calendar.set(Calendar.DATE,calendar.get(Calendar.DAY_OF_MONTH));
        //calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
        //Calendar cal = Calendar.getInstance();
        //cal.setTimeInMillis(alarmTimeInMillis);
        //long timeInSecond = (alarmHour*60 + alarmMinutes)*60;
        //long currentTimeInMillis= System.currentTimeMillis();
        //long now = (Calendar.getInstance().getTime().getMinutes() + Calendar.getInstance().getTime().getHours()*60)*60;
        //long offset = timeInSecond - now;
        //long differenceOfTime = timeInSecond-Calendar.getInstance().getTimeInMillis()/1000;
        //AlarmView(long id, String hour, String days, String ringFile, String libelle)
    }


    public static void activeAlarm(Alarm alarm, Context context){
        //////////////////////////// ALARMS//////////////////////
        Intent alarmRingtoneActivityIntent = new Intent(context, AlarmRingtoneActivity.class);
        PendingIntent alarmLauncherPendingIntent = PendingIntent.getActivity(context, alarm.alarmViewServiceId, alarmRingtoneActivityIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(alarm.status) {
            if (alarm.repetition)
                manager.setRepeating(RTC_WAKEUP, Long.valueOf(alarm.millis), INTERVAL_DAY * 7, alarmLauncherPendingIntent);
            else manager.set(RTC_WAKEUP, Long.valueOf(alarm.millis), alarmLauncherPendingIntent);
        }
    }


}
