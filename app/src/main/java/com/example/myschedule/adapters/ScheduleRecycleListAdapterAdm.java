package com.example.myschedule.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.R;
import com.example.myschedule.data.RecyclerViewScheduleHolderAdm;
import com.example.myschedule.data.Subject;
import com.example.myschedule.helper.ItemTouchHelperAdapter;
import com.example.myschedule.ui.OnStartDragListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ScheduleRecycleListAdapterAdm extends RecyclerView.Adapter<RecyclerViewScheduleHolderAdm>
        implements ItemTouchHelperAdapter {

    private Subject[] subjects;
    private Context context;
    private final List<Subject> mItems = new ArrayList<Subject>();
    private int count;
    private final OnStartDragListener mDragStartListener;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Subject prev = mItems.remove(fromPosition);
        mItems.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        for(int i=0;i<mItems.size();i++){
            mItems.get(i).setTime(Time(i));
        }
        notifyItemMoved(fromPosition, toPosition);
        subjects=new Subject[mItems.size()];
        for(int i=0;i<mItems.size();i++)
            subjects[i]=new Subject(mItems.get(i));
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
        
    }

    public interface OnDragStartListener {
        void onDragStarted(RecyclerView.ViewHolder viewHolder);
    }

   /* public ScheduleRecycleListAdapterAdm(Map<String,Subject[]> subjects, int count, Context context, OnStartDragListener dragStartListener) {
        this.subjects = subjects.get(""+count);
        this.context = context;
        this.count=count;
        this.mDragStartListener = dragStartListener;
        for(int i=0;i<subjects.get(""+count).length;i++){
            mItems.add(subjects.get(""+count)[i]);
        }
    }*/
    public ScheduleRecycleListAdapterAdm(Subject[] subjects, int count, Context context, OnStartDragListener dragStartListener) {
        this.subjects = subjects;
        this.context = context;
        this.count=count;
        this.mDragStartListener = dragStartListener;
        for(int i=0;i<subjects.length;i++){
            mItems.add(subjects[i]);
        }
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.frame_schedule_adm;
    }

    @NonNull
    @Override
    public RecyclerViewScheduleHolderAdm onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_schedule_adm, parent, false);
        return new RecyclerViewScheduleHolderAdm(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewScheduleHolderAdm holder, int position) {
        if(subjects!=null) {
            holder.getTxtTime().setText(String.valueOf(subjects[position].getTime()));
            if(subjects[position].getCab()=="")
                holder.getTxtCab().setText(String.valueOf(subjects[position].getCab() + "--"));
            else
            holder.getTxtCab().setText(String.valueOf(subjects[position].getCab() + " кб"));
            holder.getTxtSubject().setText(String.valueOf(subjects[position].getSubjectName()));
            //holder.getTxtComm().setText(String.valueOf(subjects[position].getComm()));
        }

        holder.getHandleView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    public String Time(int i){
        String ret="";
        switch (i){
            case 0:
                ret= "8.00-8.45";
                break;
            case 1:
                ret= "8.55-9.40";
                break;
            case 2:
                ret= "9.50-10.35";
                break;
            case 3:
                ret= "10.45-11.30";
                break;
            case 4:
                ret= "11.40-12.25";
                break;
            case 5:
                ret= "12.35-13.20";
                break;
            case 6:
                ret= "13.30-14.15";
                break;
            case 7:
                ret= "14.25-15.10";
                break;
            case 8:
                ret= "15.20-16.05";
                break;
            case 9:
                ret= "16.15-17.00";
                break;
            case 10:
                ret= "17.10-17.55";
                break;
            case 11:
                ret= "18.05 - 18.50";
                break;
            case 12:
                ret= "19.00 - 19.45";
                break;
            case 13:
                ret= "19.55 - 20.40";
                break;

        }
        return ret;
    }
}

