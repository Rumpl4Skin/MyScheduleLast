package com.example.myschedule.data;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.R;
import com.example.myschedule.helper.ItemTouchHelperViewHolder;

public class RecyclerViewScheduleHolderAdm extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

private TextView txtTime,txtSubject,txtCab,txtComm;
public final ImageView handleView;

public RecyclerViewScheduleHolderAdm(@NonNull View itemView) {
        super(itemView);
        txtTime = itemView.findViewById(R.id.txtTimeAdm);
        txtSubject = itemView.findViewById(R.id.txtSubjectAdm);
        txtCab = itemView.findViewById(R.id.txtCabAdm);
        txtComm = itemView.findViewById(R.id.txtCommAdm);
        handleView=itemView.findViewById(R.id.handle);

        }
    public TextView getTxtTime() {
        return txtTime;
    }

    public TextView getTxtSubject() {
        return txtSubject;
    }

    public TextView getTxtCab() {
        return txtCab;
    }

    public TextView getTxtComm() {
        return txtComm;
    }

    public ImageView getHandleView() {
        return handleView;
    }

    @Override
    public void onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY);
        txtTime.setText(Time(getAdapterPosition()));
    }

    @Override
    public void onItemClear() {
        txtTime.setText(Time(getAdapterPosition()));
        itemView.setBackgroundColor(0);
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
