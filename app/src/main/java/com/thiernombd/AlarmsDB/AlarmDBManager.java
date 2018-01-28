package com.thiernombd.AlarmsDB;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.thiernombd.fattelima_class.Alarm;
import com.thiernombd.fattelima_class.AlarmView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thierno_M_B_DIALLO on 30/12/2017.
 */

public class AlarmDBManager {
    //private String dbName = null;
    //private Context context = null;
    //private int version;
    private AlarmDBHandler alarmDBHandler = null;
    private SQLiteDatabase SQLiteDB = null;

    public AlarmDBManager(String dbName, Context context, int version){
        //this.dbName = dbName;
        //this.context = context;
        //this.version = version;
        this.alarmDBHandler = new AlarmDBHandler(context, dbName, null, version);
    }

    public void openWritableDB(){
        SQLiteDB =  this.alarmDBHandler.getWritableDatabase();
    }

    public void openReadableDB(){
        SQLiteDB =  this.alarmDBHandler.getReadableDatabase();
    }

    public long insertAlarmView(AlarmView alView, Context context){
        ContentValues val = new ContentValues();
        val.put(AlarmDBHandler.HOUR, alView.hour);
        val.put(AlarmDBHandler.DAYS, alView.days);
        val.put(AlarmDBHandler.STATUS, alView.status ? 1 : 0);
        val.put(AlarmDBHandler.RING_FILE, alView.ringFile);
        val.put(AlarmDBHandler.LIBELLE, alView.libelle);
        val.put(AlarmDBHandler.SERVICEID, alView.serviceId);
        SQLiteDB.insert(AlarmDBHandler.ALARM_VIEW_T, null, val); //Insertion de l'alarmview dans la base de donnees
        //Recuperation de l'id de l'alarmview insere recement pour pouvoir inserer la nouvelle alarm afin dans le but de gerer les contraintes
        AlarmDBManager dbManager = new AlarmDBManager(AlarmDBHandler.DBNAME, context, AlarmDBHandler.VERSION);
        dbManager.openReadableDB();
        long alarmViewId = dbManager.lastInsertRowAlarmView();
        dbManager.close();
        return alarmViewId;

    }

    public long insertAlarmView_2(AlarmView alView, Context context){
        ContentValues val = new ContentValues();
        val.put(AlarmDBHandler.HOUR, alView.hour);
        val.put(AlarmDBHandler.DAYS, alView.days);
        val.put(AlarmDBHandler.STATUS, alView.status ? 1 : 0);
        val.put(AlarmDBHandler.RING_FILE, alView.ringFile);
        val.put(AlarmDBHandler.LIBELLE, alView.libelle);
        val.put(AlarmDBHandler.SERVICEID, alView.serviceId);
        SQLiteDB.insert(AlarmDBHandler.ALARM_VIEW_T, null, val); //Insertion de l'alarmview dans la base de donnees
        this.close();

        //Recuperation de l'id de l'alarmview insere recement pour pouvoir inserer la nouvelle alarm afin dans le but de gerer les contraintes
        AlarmDBManager dbManager = new AlarmDBManager(AlarmDBHandler.DBNAME, context, AlarmDBHandler.VERSION);
        dbManager.openReadableDB();
        long alarmViewId = dbManager.lastInsertRowAlarmView();
        dbManager.close();

        return alarmViewId;

    }

    public long insertAlarm(Alarm al){
        ContentValues val = new ContentValues();
        val.put(AlarmDBHandler.MILLIS, al.millis);
        val.put(AlarmDBHandler.ALARM_VIEW_T_SERVICEID, al.alarmViewServiceId);
        val.put(AlarmDBHandler.REPETITION, al.repetition);
        val.put(AlarmDBHandler.STATUS, al.status ? 1 : 0);
        val.put(AlarmDBHandler.ALARM_VIEW_T_ID, al.alarmViewId);
        return SQLiteDB.insert(AlarmDBHandler.ALARM_T, null, val);
    }

    public long deleteAlarmView(int id){  //Pas besoins de supprimer les Alarmes car la contrainte cascade s'en occupe
        return SQLiteDB.delete(AlarmDBHandler.ALARM_VIEW_T, AlarmDBHandler.ID + " = ?", new String[]{String.valueOf(id)});
    }

    public long updateAlarmView(AlarmView alView){
        ContentValues val = new ContentValues();
        val.put(AlarmDBHandler.HOUR, alView.hour);
        val.put(AlarmDBHandler.DAYS, alView.days);
        val.put(AlarmDBHandler.STATUS, alView.status ? 1 : 0);
        val.put(AlarmDBHandler.RING_FILE, alView.ringFile);
        val.put(AlarmDBHandler.LIBELLE, alView.libelle);
        val.put(AlarmDBHandler.SERVICEID, alView.serviceId);
        return SQLiteDB.update(AlarmDBHandler.ALARM_VIEW_T, val, AlarmDBHandler.ID + " = ?", new String[]{String.valueOf(alView.id)});
    }


    /*
     * L'ID reprensente l'ID de l'alarmView
     * Cette fonction permet de changer le status de l'alarmView et des alarms associes en meme temps.
     * */
    public long updateAlarmViewStatus(long ID, boolean status, Context context){
        List<Alarm> alarms = Alarm.getAlarmsByAlarmViewID(ID, context);
        ContentValues val = new ContentValues();
        val.put(AlarmDBHandler.STATUS, status ? 1 : 0);
        int result = SQLiteDB.update(AlarmDBHandler.ALARM_VIEW_T, val, AlarmDBHandler.ID + " = ?", new String[]{String.valueOf(ID)});
        for(int i = 0; i<alarms.size(); i++){
            SQLiteDB.update(AlarmDBHandler.ALARM_T, val, AlarmDBHandler.ID + " = ?", new String[]{String.valueOf(ID)});
        }
        return result;
    }

    public long updateAlarm(Alarm al){
        ContentValues val = new ContentValues();
        val.put(AlarmDBHandler.MILLIS, al.millis);
        val.put(AlarmDBHandler.REPETITION, al.repetition);
        val.put(AlarmDBHandler.STATUS, al.status ? 1 : 0);
        return SQLiteDB.update(AlarmDBHandler.ALARM_T, val, AlarmDBHandler.ID + " = ?", new String[]{String.valueOf(al.id)});
    }


    public List<AlarmView> selectAlarmView(){
        List<AlarmView> alarmArrayList = new ArrayList<AlarmView>();
        Cursor c = SQLiteDB.rawQuery("select * from " + AlarmDBHandler.ALARM_VIEW_T, null);

        while (c.moveToNext()){
            //public AlarmView(long id, int serviceId, String hour, String days, boolean status, String ringFile, String libelle)
            AlarmView al = new AlarmView(c.getLong(0), c.getInt(1), c.getString(2), c.getString(3), c.getInt(4) != 0, c.getString(5), c.getString(6));
            alarmArrayList.add(al);
        }
        c.close();
        return alarmArrayList;
    }


    public List<Alarm> selectAlarm(){
        List<Alarm> alarmArrayList = new ArrayList<Alarm>();
        Cursor c = SQLiteDB.rawQuery("select * from " + AlarmDBHandler.ALARM_T + " where "+AlarmDBHandler.STATUS+" = ?", new String[]{"1"});

        while (c.moveToNext()){
            //public Alarm(long id, String millis, boolean repetition, boolean status, long alarmViewId)
            Alarm al = new Alarm(c.getLong(0), c.getInt(1), c.getString(2), c.getInt(3) != 0, c.getInt(4) != 0, c.getLong(5));
            alarmArrayList.add(al);
        }
        c.close();
        return alarmArrayList;
    }

    public List<Alarm> selectAlarmsByAlarmViewID(long alarmviewID){
        List<Alarm> alarmArrayList = new ArrayList<Alarm>();
        Cursor c = SQLiteDB.rawQuery("select * from " + AlarmDBHandler.ALARM_T + " where "+AlarmDBHandler.ALARM_VIEW_T_ID+" = ?", new String[]{String.valueOf(alarmviewID)});

        while (c.moveToNext()){
            //public Alarm(long id, int alarmViewServiceId, String millis, boolean repetition, boolean status, long alarmViewId)
            Alarm al = new Alarm(c.getLong(0), c.getInt(1), c.getString(2), c.getInt(3) != 0, c.getInt(4) != 0, c.getLong(5));
            alarmArrayList.add(al);
        }
        c.close();
        return alarmArrayList;
    }

    public long lastInsertRowAlarmView(){
        Cursor c = SQLiteDB.rawQuery("SELECT MAX("+AlarmDBHandler.ID+") FROM " + AlarmDBHandler.ALARM_VIEW_T, null);
        c.moveToFirst();
        long id = c.getLong(0);
        c.close();
        Log.i("IIIIlast_insert_rowid()",String.valueOf(id));
        return id;
    }

    public int selectMaxAlarmViewServiceId(){
        Cursor c = SQLiteDB.rawQuery("select MAX("+AlarmDBHandler.SERVICEID+") from " + AlarmDBHandler.ALARM_VIEW_T, null);
        int serviceId = 1;
        if(c.moveToNext()){
            serviceId = c.getInt(0);
        }
        c.close();
        return serviceId;
    }


    public int selectAlarmViewServiceId(int id){
        Cursor c = SQLiteDB.rawQuery("select "+AlarmDBHandler.SERVICEID+" from " + AlarmDBHandler.ALARM_VIEW_T + " where "+AlarmDBHandler.ID+" = ?", new String[]{String.valueOf(id)});
        int serviceId = 0;
        if(c.moveToNext()){
            serviceId = c.getInt(0);
        }
        c.close();
        return serviceId;
    }

    public void close(){
        SQLiteDB.close();
    }

}
