package com.thiernombd.fattelima;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.thiernombd.fattelima_class.Alarm;

import java.util.List;

/**
 * Created by Thierno_M_B_DIALLO on 27/11/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Alarm> alarmObjectList;

    public MyAdapter(List<Alarm> alarmeObj){
        this.alarmObjectList = alarmeObj;
    }
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.alarme_cardview_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
        Alarm currentAlarm = alarmObjectList.get(position);
        holder.display(currentAlarm,position);
    }

    @Override
    public int getItemCount() {
        return alarmObjectList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView alarmeDays_tv;
        private final TextView alarmeHours_tv;
        private final TextView alarmeSound_tv;
        private final TextView alarmeLibelle_tv;
        private final ImageButton editeAlarme_img;
        private final ImageButton deleteAlarm_img;
        private Alarm currentAlarm;
        private int position;

        public MyViewHolder(View itemView){
            super(itemView);
            alarmeDays_tv = (TextView) itemView.findViewById(R.id.alarmeDays_tv);
            alarmeHours_tv = (TextView) itemView.findViewById(R.id.alarmeHour_tv);
            alarmeSound_tv = (TextView) itemView.findViewById(R.id.alarmeSound_tv);
            alarmeLibelle_tv = (TextView) itemView.findViewById(R.id.alarmeLibelle_tv);
            editeAlarme_img = (ImageButton) itemView.findViewById(R.id.editAlarm_img);
            deleteAlarm_img = (ImageButton) itemView.findViewById(R.id.deleteAlarm_img);

            alarmeLibelle_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Libellé")
                            .setMessage(currentAlarm.libelle)
                            .show();
                }
            });

            deleteAlarm_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAlarmIn_rv(position);
                }
            });

        }



        public void display(Alarm current,int position) {
            this.currentAlarm = current;
            this.position = position;
            alarmeDays_tv.setText(current.repeteDays);
            alarmeHours_tv.setText(current.rtcSecond);
            alarmeSound_tv.setText(current.ringFile);
            alarmeLibelle_tv.setText(current.libelle);
        }
    }

    public void removeAlarmIn_rv(int position){
        alarmObjectList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position,alarmObjectList.size());
    }

    public void addAlarmIn_rv(int position, Alarm alarm){
        alarmObjectList.add(position,alarm);
        notifyItemInserted(position);
        notifyItemRangeRemoved(position,alarmObjectList.size());
    }
}
