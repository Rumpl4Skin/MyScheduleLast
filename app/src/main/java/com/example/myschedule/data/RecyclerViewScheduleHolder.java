package com.example.myschedule.data;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.R;

public class RecyclerViewScheduleHolder extends RecyclerView.ViewHolder {

private TextView txtTime,txtSubject,txtCab,txtComm;

public RecyclerViewScheduleHolder(@NonNull View itemView) {
        super(itemView);
        txtTime = itemView.findViewById(R.id.txtTime);
        txtSubject = itemView.findViewById(R.id.txtSubject);
        txtCab = itemView.findViewById(R.id.txtCab);
        txtComm = itemView.findViewById(R.id.txtComm);

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
}
