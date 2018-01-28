package com.thiernombd.AlarmsDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Thierno_M_B_DIALLO on 21/12/2017.
 */

public class AlarmDBHandler extends SQLiteOpenHelper {

    public static final String DBNAME = "fattelima.db";
    public static final int VERSION = 7;
    //////////TABLES NAMES AND ATTRIBUTES/////////////////////////
    public static final String ALARM_T = "alarm_t";
    public static final String ALARM_VIEW_T = "alarm_view_t";
    public static final String ID = "id";
    public static final String SERVICEID = "service_id";
    public static final String ALARM_VIEW_T_SERVICEID = "alarm_view_t_service_id";
    public static final String MILLIS = "millis";
    public static final String REPETITION = "repetition";
    public static final String STATUS = "status";
    public static final String ALARM_VIEW_T_ID = "alarm_view_t_id";
    public static final String HOUR = "hour";
    public static final String DAYS = "days";
    public static final String RING_FILE = "ring_file";
    public static final String LIBELLE = "libelle";
    /////////////////////////////////////////////////

    ///////////REQUESTS FOR CREATING TABLES///////////
    public static final String ALARM_VIEW_T_CREATE =
            " CREATE TABLE " + ALARM_VIEW_T + " ( " +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SERVICEID + " INTEGER, " +
                    HOUR + " TEXT, " +
                    DAYS + " TEXT, " +
                    STATUS + " INTEGER NOT NULL, " +
                    RING_FILE + " TEXT, " +
                    LIBELLE+ " TEXT ," +
                    " CHECK ( " + STATUS + " = " + 0 + " or ( " + STATUS + " = " + 1 + " )) " +
                    " );";

    public static final String ALARM_T_CREATE =
            " CREATE TABLE " + ALARM_T + " ( " +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ALARM_VIEW_T_SERVICEID + " INTEGER, " +
                    MILLIS + " INTEGER, " +
                    REPETITION + " INTEGER NOT NULL, " +       //EVENT IF THE ALARM WILL REPETE (1) OR NOT (0)
                    STATUS + " INTEGER NOT NULL, " +
                    //STATUS + " INTEGER NOT NULL, "+
                    ALARM_VIEW_T_ID + " INTEGER NOT NULL, " +
                    " CHECK ( " + REPETITION + " = " + 0 + " or ( " + REPETITION + " = " + 1 + " ))," +
                    " CHECK ( " + STATUS + " = " + 0 + " or ( " + STATUS + " = " + 1 + " )), " +
                    " FOREIGN KEY ( " + ALARM_VIEW_T_ID + " ) REFERENCES " + ALARM_VIEW_T + " ( " + ID + " ) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    " FOREIGN KEY ( " + ALARM_VIEW_T_SERVICEID + " ) REFERENCES " + ALARM_VIEW_T + " ( " + SERVICEID + " ) ON DELETE CASCADE ON UPDATE CASCADE " +
                    " );";

    //////////////////////////////////////////////////

    /////////////REQUESTS FOR DROP TABLES/////////////////////////////////////
    public static final String ALARM_VIEW_T_DROP = " DROP TABLE IF EXISTS " + ALARM_VIEW_T + " ; ";
    public static final String ALARM_T_DROP = " DROP TABLE IF EXISTS " + ALARM_T + " ; ";
    /////////////////////////////////////////////////////////////////

    public AlarmDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("info","onCreate....................");
        db.execSQL(ALARM_VIEW_T_CREATE);
        db.execSQL(ALARM_T_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ALARM_T_DROP);
        db.execSQL(ALARM_VIEW_T_DROP);
        onCreate(db);
    }

}
