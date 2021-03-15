package com.example.myschedule.data;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.PageFragment;
import com.example.myschedule.R;

public class RecyclerViewDiscHolder extends RecyclerView.ViewHolder {

    private TextView NameSubj;
    private TextView LabCount;
    private TextView PractCount;

    public RecyclerViewDiscHolder(@NonNull View itemView) {
        super(itemView);
        NameSubj = itemView.findViewById(R.id.txtDiscSubject);
        LabCount = itemView.findViewById(R.id.txtLabCount);
        PractCount = itemView.findViewById(R.id.txtPractCount);
    }

    public TextView getNameSubj() {
        return NameSubj;
    }

    public TextView getLabCount() {
        return LabCount;
    }

    public TextView getPractCount() {
        return PractCount;
    }
}


