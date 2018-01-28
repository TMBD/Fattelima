package com.thiernombd.fattelima_class;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.thiernombd.AlarmsDB.AlarmDBHandler;
import com.thiernombd.AlarmsDB.AlarmDBManager;
import com.thiernombd.fattelima.MyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thierno_M_B_DIALLO on 22/12/2017.
 */

public class AlarmView {
    public long id = 0;
    public int serviceId = 0;  //null pour l'insertion
    public String hour = null;
    public String days = null;
    public boolean status = true;
    public String ringFile = null;
    public String libelle = null;

    public static final String LUN = "Lun.";
    public static final String MAR = "Mar.";
    public static final String MER = "Mer.";
    public static final String JEU = "Jeu.";
    public static final String VEN = "Ven.";
    public static final String SAM = "Sam.";
    public static final String DIM = "Dim.";

    public static final String HOURSEPARATOR = "h:";
    public static final String MIN = "min";


    public static MyAdapter adapter;

    private String[] daysTb = {LUN, MAR, MER, JEU, VEN, SAM, DIM};

    public AlarmView() {}

    public AlarmView(long id, int serviceId, String hour, String days, boolean status, String ringFile, String libelle) {
        this.id = id;
        this.serviceId = serviceId;
        this.hour = hour;
        this.days = days;
        this.status = status;
        this.ringFile = ringFile;
        this.libelle = libelle;
    }

    public AlarmView(int serviceId, String hour, String days, boolean status, String ringFile, String libelle) {
        this.serviceId = serviceId;
        this.hour = hour;
        this.days = days;
        this.status = status;
        this.ringFile = ringFile;
        this.libelle = libelle;
    }


    public static List<AlarmView> getAlarmDataList(){
        List<AlarmView> data= new ArrayList<AlarmView>();
        AlarmView[] al = getAlarms();
        for (int i=0;i<al.length;i++){
            al[i].libelle = i+" - "+al[i].libelle;
            data.add(al[i]);
        }
        return data;
    }


    private static AlarmView[] getAlarms(){
        //String[] repet = new String[]{"Lun. Mar. Mer. Jeu. Ven. Sam. Dim", "erzere"};
        return  new AlarmView[]{
                new AlarmView(00000000, "12h:30min", "Mar. Mer. Sam. Dim", true,"sdcard/download/myringfile", "Je dois me reveiller"),
                new AlarmView(123456789, "05h:30min", "Lun. Mar. Mer. Jeu. Ven. Sam. Dim", true, "sdcard/download/myringfile", "Je dois me reveiller"),
                new AlarmView(98765410, "12h:30min", "Mar. Mer. Jeu. Ven.", true, "sdcard/download/myringfile", "Je dois me reveiller"),
                /*new Alarm("10:00min","Aller à l'ecole","sdcard/Music/myringfile","Lun. Mar. Ven. Dim"),
                */
        };
    }


    public static void loadAlarmViewsOnRV(RecyclerView rv, Context context){
        AlarmDBManager dbManager = new AlarmDBManager(AlarmDBHandler.DBNAME, context, AlarmDBHandler.VERSION);
        dbManager.openReadableDB();
        List<AlarmView> alView = dbManager.selectAlarmView();
        dbManager.close();
        alView = orderAlarmViews(alView);
        adapter = new MyAdapter(alView);
        rv.setAdapter(adapter);
    }


    /*
    * Activer/desactiver les alarms pour lesquels l'AlarmView vient d'etre active/desactiver avec le bouton switch
    */
    public static void ToggleAlarmViewAlarm(long alarmViewID, boolean active, Context context){
        List<Alarm> al = Alarm.getAlarmsByAlarmViewID(alarmViewID, context);
        if(active){
            for(int i = 0; i<al.size(); i++){
                al.get(i).status = true;
                Alarm.activeAlarm(al.get(i), context);

            }
        }else Alarm.cancelAlarm(al.get(0).alarmViewServiceId, context); // Cette ligne suffi pour desactiver tous les Alarm associés a cette AlarmView
        //Mise à jour des status des alarms et des alarmView dans la bases de donnes
        updateAlarmViewStatus(alarmViewID, active, context);
    }

    public static void updateAlarmViewStatus(long ID, boolean status, Context context){
        AlarmDBManager dbManager = new AlarmDBManager(AlarmDBHandler.DBNAME, context, AlarmDBHandler.VERSION);
        dbManager.openWritableDB();
        dbManager.updateAlarmViewStatus(ID, status, context);
        dbManager.close();
    }

    public static long addAlarmView_DBView(AlarmView alView, Context context){
        AlarmDBManager dbManager = new AlarmDBManager(AlarmDBHandler.DBNAME, context, AlarmDBHandler.VERSION);
        dbManager.openWritableDB();
        long alarmViewID = dbManager.insertAlarmView(alView, context);
        dbManager.close();

        int position = getAlarmViewPosition(adapter.getAlarmObjectList(), alView);
        adapter.addAlarmIn_rv(position, alView);

        return alarmViewID;
    }


    public static long addAlarmView_DBView_2(AlarmView alView, Context context){
        AlarmDBManager dbManager = new AlarmDBManager(AlarmDBHandler.DBNAME, context, AlarmDBHandler.VERSION);
        dbManager.openWritableDB();
        long alarmViewID = dbManager.insertAlarmView_2(alView, context);

        //Important : On affecte aussi l'id de l'AlarmView inserer recement a l'id de l'AlarmView qui doit etre affiché dans le recyclerView
        // car sinon l'AlarmView qu'on doit ajouter n'aura pas d'id et donc lors de sa suppression/desactivation/modification dans la vue, on ne pourra pas supprimer/desactiver/modifier
        // les Alarm associes a cette AlarmView du fait qu'on n'a pas son id.
        alView.id = alarmViewID;

        int position = getAlarmViewPosition(adapter.getAlarmObjectList(), alView);
        adapter.addAlarmIn_rv(position, alView);

        return alarmViewID;
    }


    public static int getNewServiceId(Context context){
        AlarmDBManager dbManager = new AlarmDBManager(AlarmDBHandler.DBNAME, context, AlarmDBHandler.VERSION);
        dbManager.openReadableDB();
        int alarmServiceId = dbManager.selectMaxAlarmViewServiceId() + 1;
        if(alarmServiceId >= Integer.MAX_VALUE - 10) alarmServiceId = 1;
        dbManager.close();
        return alarmServiceId;
    }

    //La fonction retourne 0 si rien n'est trouvé
    public static int getServiceId(int id, Context context){
        AlarmDBManager dbManager = new AlarmDBManager(AlarmDBHandler.DBNAME, context, AlarmDBHandler.VERSION);
        dbManager.openWritableDB();
        int alarmServiceId = dbManager.selectAlarmViewServiceId(id);
        dbManager.close();
        return alarmServiceId;
    }

    private static List<AlarmView> orderAlarmViews(List<AlarmView> alarmViews){
        //List<AlarmView> alarms = new ArrayList<>();
        int minimum, index, current;
        for(int i = 0; i<alarmViews.size()-1; i++){
            minimum = getMinutes(alarmViews.get(i).hour);
            index = i;
            for(int j = i+1; j<alarmViews.size(); j++){
                current = getMinutes(alarmViews.get(j).hour);
                if(minimum > current) {
                    minimum = current;
                    index = j;
                }
            }

            //Permutation
            AlarmView alView_i = alarmViews.get(i);
            AlarmView alView_index = alarmViews.get(index);
            alarmViews.remove(i);
            alarmViews.add(i, alView_index);
            alarmViews.remove(index);
            alarmViews.add(index, alView_i);
        }

        return alarmViews;
    }

    public static int getAlarmViewPosition(List<AlarmView> alarmViews, AlarmView alView){
        int i = 0;
        while(i < alarmViews.size() && (getMinutes(alarmViews.get(i).hour) < getMinutes(alView.hour))) i++;
        return i;

    }

    private static int getMinutes(String hour){
        String time = hour.split(AlarmView.MIN)[0];
        String times[] = time.split(AlarmView.HOURSEPARATOR);
        return Integer.valueOf(times[0])*60 + Integer.valueOf(times[1]);
    }


}
