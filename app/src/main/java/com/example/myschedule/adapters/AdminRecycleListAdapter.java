package com.example.myschedule.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.R;
import com.example.myschedule.data.Admins;
import com.example.myschedule.data.RecyclerViewHolder;

import java.io.IOException;
import java.io.InputStream;


public class AdminRecycleListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private Admins[] admins;
    private Context context;

    public AdminRecycleListAdapter(Admins[] adminss,Context c) {
        admins=adminss;
        context=c;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.frame_textview;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.getDoljnView().setText(String.valueOf(admins[position].getDiljn()));
        holder.getFioView().setText(String.valueOf(admins[position].getFio()));
        holder.getPhoneView().setText(String.valueOf(admins[position].getPhone()));
        InputStream inputStream = null;
        try{
            inputStream = context.getApplicationContext().getAssets().open(admins[position].getImg());
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            holder.getImgView().setImageDrawable(drawable);
            holder.getImgView().setScaleType(ImageView.ScaleType.FIT_XY);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                if(inputStream!=null)
                    inputStream.close();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }

        }
    }

    @Override
    public int getItemCount() {
        return admins.length;
    }
}
