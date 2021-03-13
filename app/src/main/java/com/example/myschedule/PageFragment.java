package com.example.myschedule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.adapters.ScheduleRecycleListAdapter;
import com.example.myschedule.data.Subject;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PageFragment extends Fragment {
    private Subject[] subjects;
    Integer page;

   /* public static PageFragment newInstance(int page) {
        PageFragment fragment = new PageFragment();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }*/
    public static PageFragment newInstance(int pages, Subject[] subjects) {
          int[] id_subject=new int[subjects.length];
          String[] time=new String[subjects.length];
          String[] subjectName= new String[subjects.length];
          String[] comm=new String[subjects.length];
          String[] cab=new String[subjects.length];
          for(int i=0;i<subjects.length;i++){
               id_subject[i]=new Integer(subjects[i].getId_subject());
               time[i]=new String(subjects[i].getTime());
               subjectName[i]= new String(subjects[i].getSubjectName());
               comm[i]=new String(subjects[i].getComm());
               cab[i]=new String(subjects[i].getCab());
          }
        PageFragment fragment = new PageFragment();
        Bundle args=new Bundle();
        args.putIntArray("ids",id_subject);
        args.putStringArray("times",time);
        args.putStringArray("subjectNames",subjectName);
        args.putStringArray("comments",comm);
        args.putStringArray("cabs",cab);
        args.putInt("num", pages);
        fragment.setArguments(args);
        return fragment;
    }

    public PageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            int count = getArguments() != null ? getArguments().getIntArray("ids").length : 0;
            subjects=new Subject[count];
            int[] id_subject = getArguments().getIntArray("ids");
            String[] time = getArguments().getStringArray("times");
            String[] subjectName = getArguments().getStringArray("subjectNames");
            String[] comm = getArguments().getStringArray("comments");
            String[] cab = getArguments().getStringArray("cabs");

            for (int i = 0; i < count; i++) {
                subjects[i]=new Subject(time[i],subjectName[i],comm[i],cab[i]);
            }

            page=this.getArguments().getInt("num");
            SharedPreferences sPref = getActivity().getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putInt("page", page);
            ed.commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_page, container, false);

        return root;
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView=view.findViewById(R.id.shedule_RecList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences sPref = getActivity().getPreferences(MODE_PRIVATE);
        int savedText = sPref.getInt("page", 1);
        SharedPreferences sPrefComm = getActivity().getSharedPreferences("Comments",MODE_PRIVATE);
        for(int i=0;i<subjects.length;i++){
            subjects[i].setComm(sPrefComm.getString(page+""+i, ""));
        }
        recyclerView.setAdapter(new ScheduleRecycleListAdapter(subjects, savedText ,getContext()));
        //String header = "Фрагмент " + (pageNumber+1);
        // pageHeader.setText(header);
    }
}
