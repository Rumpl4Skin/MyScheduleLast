package com.example.myschedule.ui.disc;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.myschedule.DbHelper;
import com.example.myschedule.R;
import com.example.myschedule.adapters.DiscRecycleListAdapter;
import com.example.myschedule.adapters.DocsRecycleListAdapter;
import com.example.myschedule.data.Docs;
import com.example.myschedule.data.Subject;
import com.example.myschedule.data.model.LoggedInUser;
import com.example.myschedule.ui.slideshow.SlideshowViewModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class DiscFragment extends Fragment {
    private SlideshowViewModel slideshowViewModel;
    private DbHelper mDBHelper;
    private SQLiteDatabase mDb;
    private RecyclerView recyclerView;
    Subject[] disc;
    AutoCompleteTextView SubjName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_disc, container, false);
        mDBHelper = new DbHelper(root.getContext());

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        SubjName=root.findViewById(R.id.subj_names);
         disc=mDBHelper.getAllDisc();

        List<String> namesList = Arrays.asList(mDBHelper.getAllSubjName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                root.getContext(), android.R.layout.simple_dropdown_item_1line, namesList);
        SubjName.setAdapter(adapter);
        SubjName.setText("Введите название предмета");
        SubjName.setThreshold(1);
        SubjName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                disc=mDBHelper.getSubjName(SubjName.getText().toString());
                recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                recyclerView.setAdapter(new DiscRecycleListAdapter(disc,root.getContext()));
            }
        });

        recyclerView = root.findViewById(R.id.disc_RecList);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setAdapter(new DiscRecycleListAdapter(disc,root.getContext()));
        return root;
    }
}