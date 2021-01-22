package com.example.myschedule.data;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView Fio;
    private TextView Doljn;
    private ImageView Img;
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        Fio = itemView.findViewById(R.id.fioTxt);
        Doljn = itemView.findViewById(R.id.doljnTxt);
        Img = itemView.findViewById(R.id.admin_img);

    }

    public TextView getFioView(){
        return Fio;
    }
    public TextView getDoljnView(){
        return Doljn;
    }
    public ImageView getImgView(){
        return Img;
    }
}