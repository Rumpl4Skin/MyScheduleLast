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
        //if (true/*teamsList.size()==0*/){
         /*   new DownloadWebpageTask(new AsyncResult() {
                @Override
                public void onResult(JSONObject object) {
                    processJson(object);
                }
            }).execute("https://spreadsheets.google.com/tq?key=1m3clD7GVfmAYpIrchlA6JiGVzPBctLX6VgXqkiqltaE");


        }
        //loadAllTeam();
        TeamData.postValue(teamsList);*/
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

           /* for (int r = 0; r < rows.length(); ++r) {
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
            }*/

            Team team1 = new Team(1, "8.00-8.45","37","Физика",0,"","");
            Team team2 = new Team(2, "8.55-9.40","37","Физика",0,"","");
            Team team3 = new Team(3, "9.50-10.35","37","Специальная технология",0,"","");
            Team team4 = new Team(4, "10.45-11.30","31","Обед (1)/ Прикладная информатика (2)",0,"","");
            Team team5 = new Team(5, "11.40-12.25","31","Прикладная информатика (1)/ Обед (2)",0,"","");
            Team team6 = new Team(6, "12.35-13.20","25","Биология",0,"","");
            Team team7 = new Team(7, "13.30-14.15","--","Физическая культура и здоровье (ф)",0,"","");
            Team team8 = new Team(8, "14.25-15.10","32","Физика",0,"","");
            Team team9 = new Team(9, "15.20-16.05","32","География",0,"","");
            //if(!teamsList.contains(team1))
            teamsList.add(team1);
            if(!teamsList.contains(team2))
                teamsList.add(team2);
            if(!teamsList.contains(team3))
                teamsList.add(team3);
            if(!teamsList.contains(team4))
                teamsList.add(team4);
            if(!teamsList.contains(team5))
                teamsList.add(team5);
            if(!teamsList.contains(team6))
                teamsList.add(team6);
            if(!teamsList.contains(team7))
                teamsList.add(team7);
            if(!teamsList.contains(team8))
                teamsList.add(team8);
            if(!teamsList.contains(team9))
                teamsList.add(team9);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}