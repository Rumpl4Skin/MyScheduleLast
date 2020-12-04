package com.example.myschedule.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.R;
import com.example.myschedule.data.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamsAdapter extends ArrayAdapter<Team> {

    Context context;
    private List<Team> teams;
    private TeamsAdapter mListener;

    public TeamsAdapter(Context context, int textViewResourceId, List<Team> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.teams = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.team, null);
        }
        Team o = teams.get(position);
        if (o != null) {
            TextView pos = (TextView) v.findViewById(R.id.n_subject);
            TextView name = (TextView) v.findViewById(R.id.name_subject);
            TextView wins = (TextView) v.findViewById(R.id.duration);
            TextView draws = (TextView) v.findViewById(R.id.audit_numb);
            //TextView losses = (TextView) v.findViewById(R.id.losses);
            //TextView points = (TextView) v.findViewById(R.id.points);

            pos.setText(String.valueOf(o.getN_subject()));
            name.setText(String.valueOf(o.getSubject_name()));
            wins.setText(String.valueOf(o.getDuration()));
            draws.setText(String.valueOf(o.getAudit()));
            //losses.setText(String.valueOf(o.getSubject_name()));
            //points.setText(String.valueOf(o.getN_group()));
        }
        return v;
    }
}


