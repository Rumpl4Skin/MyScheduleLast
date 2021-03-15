package com.example.myschedule.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.R;
import com.example.myschedule.data.Docs;
import com.example.myschedule.data.RecyclerViewDiscHolder;
import com.example.myschedule.data.RecyclerViewDocsHolder;
import com.example.myschedule.data.Subject;


public class DiscRecycleListAdapter extends RecyclerView.Adapter<RecyclerViewDiscHolder> {
    private Subject[] disc;
    private Context context;

    public DiscRecycleListAdapter(Subject[] docs1, Context c) {
        disc=docs1;
        context=c;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.frame_disc;
    }

    @NonNull
    @Override
    public RecyclerViewDiscHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewDiscHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewDiscHolder holder, int position) {
        holder.getNameSubj().setText(String.valueOf(disc[position].getSubjectName()));
        holder.getLabCount().setText(String.valueOf(disc[position].getLab_count()));
        holder.getPractCount().setText(String.valueOf(disc[position].getPract_count()));
    }

    @Override
    public int getItemCount() {
        return disc.length;
    }
}
