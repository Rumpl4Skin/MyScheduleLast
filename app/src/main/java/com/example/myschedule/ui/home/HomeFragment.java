package com.example.myschedule.ui.home;


import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myschedule.DbHelper;
import com.example.myschedule.MainActivity;
import com.example.myschedule.R;
import com.example.myschedule.adapters.ScheduleRecycleListAdapter;
import com.example.myschedule.data.Subject;
import com.example.myschedule.data.model.LoggedInUser;
import com.example.myschedule.utils.PageAdapter;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements EasyPermissions.PermissionCallbacks{
    public interface OnFragmentSendDataListener {
        void onSendData( Subject[] subjects);
    }

    GoogleAccountCredential mCredential;
    ProgressDialog mProgress;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String SPREAD_SHEET_42I = "1XcATglqKX3IomyzjEaFv4h65B6z0wSNyIkl3Ld4omz0";
    private static final String SPREAD_SHEET_32I = "1EA2JyUBb2lhhcXZM0ERjXQAH1l_OYp44zDYM4MwqTMA";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String TABLE_NAME_CH = "Числитель";
    private static final String TABLE_NAME_ZN = "Знаменатель";
    private static final String MON_R = "C2:D11";
    private static final String TUE_R = "E2:F11";
    private static final String WED_R = "G2:H11";
    private static final String THU_R = "I2:J11";
    private static final String FRI_R = "K2:L11";
    boolean isBeforeFri=false;
    int week=0;
    String change="";

    Map<Integer, String> tabs = new HashMap<Integer, String>();

    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS_READONLY };

    private OnFragmentSendDataListener fragmentSendDataListener;
    private RecyclerView recyclerView;
    private DbHelper mDBHelper;
    private SQLiteDatabase mDb;
    AutoCompleteTextView GroupName;
    private LoggedInUser user;
    AssetManager assetManager;
    Map<String,Subject[]> scheldule;
    Subject[] subjects;
    TextView curentGroup;
    ViewPager2 pager;
    TabLayout tabLayout;


    String[] days={"Понедельник","Вторник","Среда","Четверг","Пятница","Понедельник","Вторник","Среда","Четверг","Пятница"};
   @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fragmentSendDataListener = (OnFragmentSendDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mDBHelper = new DbHelper(root.getContext());
        Bundle arguments = getActivity().getIntent().getExtras();
        GroupName=root.findViewById(R.id.group_namesHome);
        curentGroup=root.findViewById(R.id.curent_group);
        tabLayout = root.findViewById(R.id.tabs);
        //recyclerView = root.findViewById(R.id.shedule_RecList);
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

        List<String> namesList = Arrays.asList(mDBHelper.getAllGroupName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                root.getContext(), android.R.layout.simple_dropdown_item_1line, namesList);
        GroupName.setAdapter(adapter);
        user=new LoggedInUser(mDBHelper.getUser( arguments.get("user").toString()));
        GroupName.setText(user.getGroupName());
        GroupName.setThreshold(1);

        //setAdapter(user.getGroupName());
        //update(Subject[] subjects);

        assetManager = getActivity().getAssets();
           // recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        /*if(this.subjects != null)
            recyclerView.setAdapter(new ScheduleRecycleListAdapter(subjects,getContext()));*/
        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage("Загрузка расписания ...");

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        getResultsFromApi();


        pager=(ViewPager2)root.findViewById(R.id.pager);
        GroupName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!GroupName.getText().toString().equals(user.getGroupName()))
                    curentGroup.setVisibility(View.GONE);
                else curentGroup.setVisibility(View.VISIBLE);
                if(GroupName.getText().toString().equals("Admin Group"))
                    Toast.makeText(getActivity().getApplicationContext(), "Расписание не доступно для администраторов", Toast.LENGTH_SHORT).show();
                else if(GroupName.getText().toString().equals("42и")||GroupName.getText().toString().equals("32и"))
                    getResultsFromApi();
                else Toast.makeText(getActivity().getApplicationContext(), "Расписание данной группы пока недоступно", Toast.LENGTH_LONG).show();
            }
        });
        return root;
    }
    public void onSendData(Subject[] subjects){

                if(!GroupName.getText().toString().equals(user.getGroupName()))
                    curentGroup.setVisibility(View.GONE);
                else curentGroup.setVisibility(View.VISIBLE);
                if(GroupName.getText().toString().equals("Admin Group"))
                    Toast.makeText(getActivity().getApplicationContext(), "Расписание не доступно для администраторов", Toast.LENGTH_SHORT).show();
                else {
                    getResultsFromApi();
                }
                if(subjects.length==0) Toast.makeText(getActivity().getApplicationContext(), "Расписание для этой группы пока не доступно", Toast.LENGTH_SHORT).show();
    }
    public void setAdapter(Subject[] subjects){
        GroupName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!GroupName.getText().toString().equals(user.getGroupName()))
                    curentGroup.setVisibility(View.GONE);
                else curentGroup.setVisibility(View.VISIBLE);
                if(GroupName.getText().toString().equals("Admin Group"))
                    Toast.makeText(getActivity().getApplicationContext(), "Расписание не доступно для администраторов!", Toast.LENGTH_SHORT).show();

                else if(subjects.length<0)Toast.makeText(getActivity().getApplicationContext(), "Расписание для этой группы пока не доступно", Toast.LENGTH_SHORT).show();
                else getResultsFromApi();
            }
        });
    }
    public String getCurrentDay(){
        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E");
        String Day="";
        switch (formatForDateNow.format(date)){
            case "Пн": Day= MON_R; break;
            case "Mon": Day= MON_R; break;
            case "Вт": Day= TUE_R;break;
            case "Tue": Day= TUE_R; break;
            case "Ср": Day= WED_R;break;
            case "Wed": Day= WED_R; break;
            case "Чт": Day= THU_R;break;
            case "Thu": Day= THU_R; break;
            case "Пт": Day= FRI_R;break;
            case "Fri": Day= FRI_R; break;
            case "Сб": Day= MON_R; break;
            case "Sat": Day= MON_R; break;
            case "Вс": Day= MON_R;break;
            case "Sun": Day= MON_R; break;
        }
        return Day;
    }
    public String getNameDay(String name){

        String Day="";
        switch (name){

            case MON_R: Day="Пн" ; break;

            case TUE_R: Day="Вт" ;break;

            case WED_R: Day= "Ср";break;

            case THU_R: Day= "Чт";break;
            case FRI_R: Day= "Пт";break;

        }
        return Day;
    }
    public int getCountDay(String name){

        int Day=0;
        switch (name){

            case MON_R: Day=0 ; break;

            case TUE_R: Day=1 ;break;

            case WED_R: Day= 2;break;

            case THU_R: Day= 3;break;
            case FRI_R: Day= 4;break;

        }
        return Day;
    }
    public String getDayRange(String day){

        String Day="";
        switch (day){

            case "Понедельник": Day= MON_R; break;
            case "Mon": Day= MON_R; break;
            case "Вторник": Day= TUE_R;break;
            case "Tue": Day= TUE_R; break;
            case "Среда": Day= WED_R;break;
            case "Wed": Day= WED_R; break;
            case "Четверг": Day= THU_R;break;
            case "Thu": Day= THU_R; break;
            case "Пятница": Day= FRI_R;break;
            case "Fri": Day= FRI_R; break;

        }
        return Day;
    }
    public void setSubjects(Subject[] subjects) {
        this.subjects = subjects;
    }

    public void setSchelduleAdapter(Subject[] subjects){
        this.subjects=subjects;
        if(this.subjects != null) {
            //Subject[] subjects=new Subject[arguments.getInt("Schl_size")];
            // subjects= (Subject[]) arguments.getParcelableArray("SchelduleList");
            this.subjects = (Subject[]) getArguments().getParcelableArray("SchelduleList");

            //Subject[] subjects={new Subject("9.00-10.40","Ботаника","10 лаб","26"),
            //       new Subject("8.55-9.40","Контроль качества продукции в сфере д/о и призводства мебели","fdkjgflkgjdjdlgjfdlkgjldjglfdfgjd","26")};

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            // recyclerView.setAdapter(new ScheduleRecycleListAdapter(subjects,getContext()));}
            /*else*/
            Toast.makeText(getActivity().getApplicationContext(), "Расписание не доступно", Toast.LENGTH_SHORT).show();
        }}

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    public void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            Toast.makeText(getActivity(), "No network connection available.", Toast.LENGTH_LONG).show();
        } else {

            new MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                getActivity(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getActivity().getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(getActivity(),
                            "This app requires Google Play Services. Please install Google Play Services on your device and relaunch this app.", Toast.LENGTH_LONG).show();
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(getContext());
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(getContext());
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                getActivity(),
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }




    /**
     * An asynchronous task that handles the Google Sheets API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, Map<String,Subject[]>> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Sheets API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Google Sheets API.
         * @param params no parameters needed for this task.
         * @return
         */
        @Override
        protected Map<String,Subject[]> doInBackground(Void... params) {
            try {
                if(GroupName.getText().toString().equals("32и"))
                return getDataFromApi(SPREAD_SHEET_32I,TABLE_NAME_CH,getCurrentDay());

              else
                  if(GroupName.getText().toString().equals("42и"))
                    return getDataFromApi(SPREAD_SHEET_42I,TABLE_NAME_CH,getCurrentDay());
                  else {
                      //Toast.makeText(getContext(),"Расписание для выбранной группы не доступно",Toast.LENGTH_LONG).show();
                  cancel(true);
                  return null;
                  }
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of names and majors of students in a sample spreadsheet:
         * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
         * @return List of names and majors
         * @throws IOException
         */
        private Map<String,Subject[]> getDataFromApi(String spreadsheetId, String table, String day) throws IOException {
            //spreadsheetId = "1XcATglqKX3IomyzjEaFv4h65B6z0wSNyIkl3Ld4omz0";
            Map<String, Subject[]> schedule = new HashMap<String, Subject[]>();
            String range="B14";


            ArrayList<Subject> results = new ArrayList<Subject>();

            ValueRange response = this.mService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();

            if (values != null) {
                for (int i = 0; i < values.size(); i++) {

                    if (values.get(i).size() == 1){
                       change=values.get(i).get(0).toString();
                    }
                    else if(values.get(i).size() == 0)
                        change="";
                }
            }

int count=getCountDay(getNameDay(getCurrentDay()));
            for(int k=/*count*/0;k</*count+*/10;k++) {
                if(k<4){
                    range = TABLE_NAME_CH + "!" + getDayRange(days[k]);
                }
                else {
                    range = TABLE_NAME_ZN + "!" + getDayRange(days[k]);
                }
                tabs.put(k,getNameDay(getDayRange(days[k])));
                 results = new ArrayList<Subject>();

                 response = this.mService.spreadsheets().values()
                        .get(spreadsheetId, range)
                        .execute();
                 values = response.getValues();

                if (values != null) {
                    for (int i = 0; i < values.size(); i++) {
                        if (values.get(i).size() == 1)
                            results.add(new Subject(Time(i), values.get(i).get(0).toString(), "", ""));
                        else if (values.get(i).size() == 2)
                            results.add(new Subject(Time(i), values.get(i).get(0).toString(), "", values.get(i).get(1).toString()));
                    }
                }

                //Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
                Subject[] fin_res = new Subject[results.size()];
                for (int i = 0; i < fin_res.length; i++){
                    fin_res[i] = results.get(i);

                }
                if(!getActivity().getSharedPreferences("Comments", MODE_PRIVATE).equals(null)){
                    for(int i = 0; i < fin_res.length; i++) {
                        SharedPreferences sPref = getActivity().getSharedPreferences("Comments", MODE_PRIVATE);
                        String savedText = sPref.getString(k+"" + i, "");
                        fin_res[i].setComm(savedText);
                    }
                }
                schedule.put(days[k],fin_res);
            }
            return schedule;
        }
        public String Time(int i){
            String ret="";
            switch (i){
                case 0:
                    ret= "8.00-8.45";
                    break;
                case 1:
                    ret= "8.55-9.40";
                    break;
                case 2:
                    ret= "9.50-10.35";
                    break;
                case 3:
                    ret= "10.45-11.30";
                    break;
                case 4:
                    ret= "11.40-12.25";
                    break;
                case 5:
                    ret= "12.35-13.20";
                    break;
                case 6:
                    ret= "13.30-14.15";
                    break;
                case 7:
                    ret= "14.25-15.10";
                    break;
                case 8:
                    ret= "15.20-16.05";
                    break;
                case 9:
                    ret= "16.15-17.00";
                    break;
                case 10:
                    ret= "17.10-17.55";
                    break;
                case 11:
                    ret= "18.05 - 18.50";
                    break;
                case 12:
                    ret= "19.00 - 19.45";
                    break;
                case 13:
                    ret= "19.55 - 20.40";
                    break;

            }
            return ret;
        }



        @Override
        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected void onPostExecute(Map<String,Subject[]> output) {
            super.onPostExecute(output);
            mProgress.hide();
            if (output == null || output.size() == 0) {
                Toast.makeText(getContext(), "Расписание для данной группы пока не доступно", Toast.LENGTH_LONG).show();
            } else {
                scheldule=output;
                //recyclerView.setAdapter(new ScheduleRecycleListAdapter(subjects,getContext()));

                //Toast.makeText(getContext(), subjects[0].getSubjectName(), Toast.LENGTH_SHORT).show();

                FragmentStateAdapter pageAdapter = new PageAdapter(getActivity(), scheldule);
                pager.setAdapter(pageAdapter);

                new TabLayoutMediator(tabLayout, pager,
                        new TabLayoutMediator.TabConfigurationStrategy() {

                            @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                Date date = new Date();
                                SimpleDateFormat formatForDate = new SimpleDateFormat("dd.MM E");
                                SimpleDateFormat formatForDay = new SimpleDateFormat("E");
                                String Day=formatForDate.format(date);
                                String NameDay=formatForDay.format(date);

                                Calendar c = Calendar.getInstance();
                                try {
                                    c.setTime(formatForDate.parse(Day));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if(!isBeforeFri)
                                c.add(Calendar.DATE, position+1);
                                else c.add(Calendar.DATE, position+2+week);
                                NameDay = formatForDay.format(c.getTime());
                                if(NameDay.equals("Sat")||NameDay.equals("СБ")){
                                    c.add(Calendar.DATE, 2);
                                    isBeforeFri=true;
                                    week++;
                                }
                                else if(NameDay.equals("Sun")||NameDay.equals("ВС")) {
                                    c.add(Calendar.DATE, 2);
                                    isBeforeFri=true;
                                    week++;
                                }
                                Day = formatForDate.format(c.getTime());
                                tab.setText(Day/*+" "+tabs.get(position)*/);
                            }
                        }).attach();
                if(change!="")
                Snackbar.make(getView(),"Расписание изменено для: "+change, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    Toast.makeText(getContext(), "The following error occurred:"+mLastError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(getContext(), "Request cancelled.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
