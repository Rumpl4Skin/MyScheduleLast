package com.example.myschedule.ui.home;

import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myschedule.MainActivity;
import com.example.myschedule.R;
import com.example.myschedule.adapters.TeamsAdapter;
import com.example.myschedule.data.Team;
import com.example.myschedule.ui.AsyncResult;
import com.example.myschedule.ui.DownloadWebpageTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private TeamsAdapter mListView;
    private MutableLiveData<List<Team>> TeamData;
    ArrayList<Team> teamsList = new ArrayList<>();

    // Get vehicle list of data return type MutableLiveData
    MutableLiveData<List<Team>> getTeamData() {
        TeamData = new MutableLiveData<List<Team>>();
        if (teamsList.size()==0){
            new DownloadWebpageTask(new AsyncResult() {
                @Override
                public void onResult(JSONObject object) {
                    processJson(object);
                }
            }).execute("https://spreadsheets.google.com/tq?key=1m3clD7GVfmAYpIrchlA6JiGVzPBctLX6VgXqkiqltaE");


        }
        //loadAllTeam();
        TeamData.postValue(teamsList);
        return TeamData;
    }


    /*
     * Hardcoded vehicle data which shows in our list.
     *
     * We can use AsyncTask to load this data in background but since this is not heavy process
     * loading from database or internet.
     */
    private void processJson(JSONObject object) {

        try {
            JSONArray rows = object.getJSONArray("rows");

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                int n_subject = columns.getJSONObject(0).getInt("v");
                int n_group = columns.getJSONObject(5).getInt("v");
                String week_day = columns.getJSONObject(1).getString("v");
                String duration = columns.getJSONObject(3).getString("v");
                String audit = columns.getJSONObject(4).getString("v");
                String subject_name = columns.getJSONObject(5).getString("v");
                String group_name = columns.getJSONObject(19).getString("v");

                Team team = new Team(n_subject, n_group, week_day, duration, audit, subject_name, group_name);
                if(!teamsList.contains(team))
                teamsList.add(team);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}