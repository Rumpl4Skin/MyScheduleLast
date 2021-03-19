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
    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(0);
    }
}
