package com.example.myschedule.ui.admin;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myschedule.DbHelper;
import com.example.myschedule.R;
import com.example.myschedule.adapters.ScheduleRecycleListAdapterAdm;
import com.example.myschedule.data.Admins;
import com.example.myschedule.data.Docs;
import com.example.myschedule.data.Schedule;
import com.example.myschedule.data.Subject;
import com.example.myschedule.data.model.LoggedInUser;
import com.example.myschedule.helper.ItemTouchHelperAdapter;
import com.example.myschedule.helper.SimpleItemTouchHelperCallback;
import com.example.myschedule.ui.OnStartDragListener;
import com.example.myschedule.ui.home.HomeFragment;
import com.example.myschedule.utils.PageAdapter;
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
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AdminFragment extends Fragment implements EasyPermissions.PermissionCallbacks,
        ScheduleRecycleListAdapterAdm.OnDragStartListener {

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
    private static final String MON_R = "C2:D12";
    private static final String TUE_R = "E2:F12";
    private static final String WED_R = "G2:H12";
    private static final String THU_R = "I2:J12";
    private static final String FRI_R = "K2:L12";
    boolean isBeforeFri=false,tabsChange=false;
    int week=0;
    String change="";
    Map<Integer, Subject[]> schedules = new HashMap<Integer, Subject[]>();
    Map<Integer, String> tabs = new HashMap<Integer, String>();
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
    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS };
    String[] days={"Понедельник","Вторник","Среда","Четверг","Пятница","Понедельник","Вторник","Среда","Четверг","Пятница"};

    private HomeFragment.OnFragmentSendDataListener fragmentSendDataListener;
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

    private static final String MOD_USERS = "Редактирование пользователей";
    private static final String MOD_ADMIN = "Редактирование администрации";
    private static final String MOD_ZAYV = "Редактирование образцов заявлений";
    private static final String MOD_DISC = "Редактирование списка дисциплин";
    private static final String MOD_SCHEDULE = "Редактирование расписания";

    private static final String TABLE_USERS = "users";
    private static final String ID_USER = "id_user";
    private static final String USER_FIO = "fio";
    private static final String MAIL = "mail";
    private static final String ID_GROUP = "id_group";
    private static final String PASSWORD = "password";

    private static final String TABLE_ADMINS = "admins";
    private static final String ID_ADMINS = "id_admins";
    private static final String ADMINS_FIO = "fio_admins";
    private static final String ADMINS_DOLJN = "doljn";
    private static final String ADMINS_PHONE = "phone";
    private static final String ADMINS_IMG = "admins_img";

    private static final String TABLE_DOCS = "Docs";
    private static final String ID_DOC = "id_doc";
    private static final String DOC_NAME = "name_doc";
    private static final String DOC_IMG = "doc_img";

    private static final String TABLE_DISC = "subject";
    private static final String ID_DISC = "id_subject";
    private static final String DISC_NAME = "name_subject";
    private static final String DISC_LAB = "lab_numb";
    private static final String DISC_PRACT = "practic_numb";

    private static final String TABLE_GROUPS = "groups";
    private static final String GROUP_NAME = "group_name";
     Uri imageUri= Uri.parse("");
    EditText edtIdUser,edtFio,edtMail,edtPsw,edtIdGroup;
    AutoCompleteTextView edtGroupName;
    Spinner spMode;
    ImageView btnPrev,btnUpdate,btnDel,btnAdd,btnApply,btnNext,imgAdmins;
    public int count=0;
    public boolean is_edit=false;
    LoggedInUser[] users;
    Admins[] admins;
    Docs[] docs;
    Subject[] subj;
    LoggedInUser us;
    Admins adm;
    String[] mods = { MOD_USERS, MOD_ADMIN ,MOD_ZAYV,  MOD_DISC,MOD_SCHEDULE};
    String selected_mode=MOD_USERS;
    private ItemTouchHelper mItemTouchHelper;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*galleryViewModel =
                new ViewModelProvider(this).get(AdminViewModel.class);*/
        View root = inflater.inflate(R.layout.fragment_admin, container, false);

        spMode=root.findViewById(R.id.sp_mode);
        edtIdUser = root.findViewById(R.id.id_user);
        edtFio = root.findViewById(R.id.fio);
        edtMail = root.findViewById(R.id.mail);
        edtPsw = root.findViewById(R.id.password);
        edtIdGroup = root.findViewById(R.id.id_group);
        edtGroupName = root.findViewById(R.id.group_namesHome);

        btnPrev = root.findViewById(R.id.prev);
        btnUpdate = root.findViewById(R.id.update);
        btnApply = root.findViewById(R.id.apply);
        btnNext = root.findViewById(R.id.next);
        btnDel=root.findViewById(R.id.delete);
        btnAdd = root.findViewById(R.id.add);

        imgAdmins = root.findViewById(R.id.gallery_admins);
        recyclerView=root.findViewById(R.id.shedule_RecListAdm);
        mDBHelper = new DbHelper(getContext());


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
        mCredential = GoogleAccountCredential.usingOAuth2(
                getContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage("Загрузка расписания ...");


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(root.getContext(), android.R.layout.simple_spinner_item, mods);
        // Определяем разметку для использования при выборе элемента
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spMode.setAdapter(adapter1);

        spMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                switch (selectedItemPosition){
                    case 0://пользователи
                        edtPsw.setVisibility(View.VISIBLE);
                        edtIdGroup.setVisibility(View.GONE);
                        edtGroupName.setVisibility(View.VISIBLE);
                        setModeUsers();
                        List<String> namesList = Arrays.asList(mDBHelper.getAllGroupName());
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                root.getContext(), android.R.layout.simple_dropdown_item_1line, namesList);
                        edtGroupName.setAdapter(adapter);
                        edtGroupName.setThreshold(1);

                        count=0;
                        /*if(users.length>0)
                        us.clear(users);*/
                        users=mDBHelper.getAllUser();
                        //edtIdUser.setText(""+ users[count].getIdUser());
                        edtFio.setText(users[count].getFIO());
                        edtMail.setText(users[count].getMail());
                        edtPsw .setText(users[count].getPassword());
                        edtIdGroup.setText(""+ users[count].getIdGroup());
                        edtGroupName.setText(users[count].getGroupName());



                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                count=0;
                                users=mDBHelper.getAllUser();
                                edtIdUser.setText(""+ users[count].getIdUser());
                                edtFio.setText(users[count].getFIO());
                                edtMail.setText(users[count].getMail());
                                edtPsw .setText(users[count].getPassword());
                                edtIdGroup.setText(""+ users[count].getIdGroup());
                                edtGroupName.setText(users[count].getGroupName());
                                Toast.makeText(getContext(), "Обновлено", Toast.LENGTH_SHORT).show();
                            }
                        });
                        btnPrev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(count==0)
                                    count=mDBHelper.getAllUser().length-1;
                                else count=count-1;
                                edtIdUser.setText(""+ users[count].getIdUser());
                                edtFio.setText(users[count].getFIO());
                                edtMail.setText(users[count].getMail());
                                edtPsw .setText(users[count].getPassword());
                                edtIdGroup.setText(""+ users[count].getIdGroup());
                                edtGroupName.setText(users[count].getGroupName());
                            }
                        });
                        btnNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(count== users.length-1)
                                    count=0;
                                else count=count+1;
                                edtIdUser.setText(""+ users[count].getIdUser());
                                edtFio.setText(users[count].getFIO());
                                edtMail.setText(users[count].getMail());
                                edtPsw .setText(users[count].getPassword());
                                edtIdGroup.setText(""+ users[count].getIdGroup());
                                edtGroupName.setText(users[count].getGroupName());
                            }
                        });
                        btnDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDb.delete("users",
                                        "id_user = ?",
                                        new String[] {edtIdUser.getText().toString()});
                                Toast.makeText(getContext(), "Запись удалена", Toast.LENGTH_SHORT).show();
                                updateUsersUI();
                                count--;
                            }
                        });
                        btnApply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ContentValues newValues = new ContentValues();
// Задайте значения для каждой строки.
                                if(mDBHelper.userIsExistAny(users[count])){
                                    newValues.put(USER_FIO,  edtFio.getText().toString());
                                    newValues.put(MAIL,  edtMail.getText().toString());
                                    newValues.put(PASSWORD,  edtPsw.getText().toString());
                                    newValues.put(ID_GROUP,  mDBHelper.getGroupId(edtGroupName.getText().toString()));
                                    mDb.update (TABLE_USERS, newValues, ID_USER+"="+mDBHelper.getGroupId(edtGroupName.getText().toString()), null);
                                }
                                Toast.makeText(getContext(), "Запись отредактирована!", Toast.LENGTH_SHORT).show();
                                updateUsersUI();
                            }
                        });
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ContentValues newValues = new ContentValues();
                                if(!is_edit){
                                    ClearAllEdt();
                                    is_edit=true;
                                    Toast.makeText(getContext(), "Введите данные для добавления надписи", Toast.LENGTH_SHORT).show();
                                }

                                if(!anyEdtEmpty()&&mDBHelper.userIsExistWithMail(edtMail.getText().toString()))
                                    Toast.makeText(getContext(), "Не все поля заполнены или пользователь с такой почтой зарегистрирован", Toast.LENGTH_LONG).show();
                                else {
                                    if(edtPsw.getText().toString().length()<8)
                                        Toast.makeText(getContext(), "Пароль должен быть длинне 8-ми символов", Toast.LENGTH_LONG).show();
                                    else {
                                        newValues.put(USER_FIO, edtFio.getText().toString());
                                        newValues.put(MAIL, edtMail.getText().toString());
                                        newValues.put(PASSWORD, edtPsw.getText().toString());
                                        newValues.put(ID_GROUP, mDBHelper.getGroupId(edtGroupName.getText().toString()));



// Добавление в бд
                                        mDb.insert(TABLE_USERS, null, newValues);
                                        count++;
                                        Toast.makeText(getContext(), "Запись добавлена!", Toast.LENGTH_SHORT).show();
                                        updateUsersUI();
                                        is_edit=false;
                                    }
                                }
                            }
                        });
                        break;
                    case 1: count=0;//администрация
                    imgAdmins.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            //Тип получаемых объектов - image:
                            photoPickerIntent.setType("image/*");
                            //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                            startActivityForResult(photoPickerIntent, 1);
                        }
                    });
                        setModeAdmin();
                        //adm.clear(admins);
                        admins=mDBHelper.getAllAdmins();
                        edtIdUser.setText(""+ admins[count].getId_admins());
                        edtFio.setText(admins[count].getFio());
                        edtMail.setText(admins[count].getDiljn());
                        edtIdGroup.setText(""+admins[count].getPhone());
                        if(admins[count].getImg()!=null)
                        setImgAdmins(admins[count].getImg());
                        else
                            imgAdmins.setImageResource(R.drawable.ic_menu_gallery);

                        imgAdmins.setVisibility(View.VISIBLE);
                        edtPsw.setVisibility(View.GONE);
                        edtGroupName.setVisibility(View.GONE);

                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                count=0;
                                //adm.clear(admins);
                                admins=mDBHelper.getAllAdmins();
                                edtIdUser.setText(""+ admins[count].getId_admins());
                                edtFio.setText(admins[count].getFio());
                                edtMail.setText(admins[count].getDiljn());
                                edtIdGroup.setText(""+admins[count].getPhone());
                                setImgAdmins(admins[count].getImg());
                                Toast.makeText(getContext(), "Обновлено", Toast.LENGTH_SHORT).show();
                            }
                        });
                        btnPrev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(count==0)
                                    count=mDBHelper.getAllAdmins().length-1;
                                else count=count-1;
                                edtIdUser.setText(""+ admins[count].getId_admins());
                                edtFio.setText(admins[count].getFio());
                                edtMail.setText(admins[count].getDiljn());
                                edtIdGroup.setText(""+admins[count].getPhone());
                                setImgAdmins(admins[count].getImg());
                            }
                        });
                        btnNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(count== mDBHelper.getAllAdmins().length-1)
                                    count=0;
                                else count=count+1;
                                edtIdUser.setText(""+ admins[count].getId_admins());
                                edtFio.setText(admins[count].getFio());
                                edtMail.setText(admins[count].getDiljn());
                                edtIdGroup.setText(""+admins[count].getPhone());
                                setImgAdmins(admins[count].getImg());
                            }
                        });
                        btnDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDb.delete("admins",
                                        "id_admins = ?",
                                        new String[] {edtIdUser.getText().toString()});
                                Toast.makeText(getContext(), "Запись удалена", Toast.LENGTH_SHORT).show();
                                updateAdminsUI();
                                count--;
                            }
                        });
                        btnApply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ContentValues newValues = new ContentValues();
// Задайте значения для каждой строки.
                                if(mDBHelper.adminIsExistAny(admins[count])){
                                    newValues.put(ADMINS_FIO,  edtFio.getText().toString());
                                    newValues.put(ADMINS_DOLJN,  edtMail.getText().toString());
                                    newValues.put(ADMINS_PHONE,  edtIdGroup.getText().toString());
                                    newValues.put(ADMINS_IMG,  admins[count].getImg());
                                    mDb.update (TABLE_ADMINS, newValues, ID_ADMINS+"="+edtIdUser.getText().toString(), null);
                                }
                                Toast.makeText(getContext(), "Запись отредактирована!", Toast.LENGTH_SHORT).show();
                                updateAdminsUI();
                            }
                        });
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ContentValues newValues = new ContentValues();
                                if(!is_edit){
                                    ClearAllEdtAdm();
                                    is_edit=true;
                                    Toast.makeText(getContext(), "Введите данные для добавления надписи", Toast.LENGTH_SHORT).show();
                                }

                                if(!anyEdtEmptyAdm()&&mDBHelper.adminIsExistWithFio(edtFio.getText().toString()))
                                    Toast.makeText(getContext(), "Не все поля заполнены или администрация с таким именем существует", Toast.LENGTH_LONG).show();
                                else {

                                        newValues.put(ADMINS_FIO, edtFio.getText().toString());
                                        newValues.put(ADMINS_DOLJN, edtMail.getText().toString());
                                        newValues.put(ADMINS_PHONE,  edtIdGroup.getText().toString());
                                        newValues.put(ADMINS_IMG, imageUri.toString());

// Добавление в бд
                                        mDb.insert(TABLE_ADMINS, null, newValues);
                                        count++;
                                        Toast.makeText(getContext(), "Запись добавлена!", Toast.LENGTH_SHORT).show();
                                        updateUsersUI();
                                        is_edit=false;
                                }
                            }
                        });
                        break;
                    case 2:
                     count=0;//заявл
                        imgAdmins.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                //Тип получаемых объектов - image:
                                photoPickerIntent.setType("image/*");
                                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                                startActivityForResult(photoPickerIntent, 1);
                            }
                        });
                        setModeZayvl();
                        //adm.clear(admins);
                        docs=mDBHelper.getAllDocs();
                        edtIdUser.setText(""+ docs[count].getId_doc());
                        edtFio.setText(docs[count].getName());
                        if(docs[count].getImg()!=null)
                            setImgAdmins(docs[count].getImg());
                        else
                            imgAdmins.setImageResource(R.drawable.ic_menu_gallery);

                        imgAdmins.setVisibility(View.VISIBLE);
                        edtPsw.setVisibility(View.GONE);
                        edtIdGroup.setVisibility(View.GONE);
                        edtGroupName.setVisibility(View.GONE);

                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                count=0;
                                //adm.clear(admins);
                                docs=mDBHelper.getAllDocs();
                                edtIdUser.setText(""+ docs[count].getId_doc());
                                edtFio.setText(docs[count].getName());
                                setImgAdmins(docs[count].getImg());
                                Toast.makeText(getContext(), "Обновлено", Toast.LENGTH_SHORT).show();
                            }
                        });
                        btnPrev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(count==0)
                                    count=mDBHelper.getAllDocs().length-1;
                                else count=count-1;
                                edtIdUser.setText(""+ docs[count].getId_doc());
                                edtFio.setText(docs[count].getName());
                                setImgAdmins(docs[count].getImg());
                            }
                        });
                        btnNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(count== mDBHelper.getAllDocs().length-1)
                                    count=0;
                                else count=count+1;
                                edtIdUser.setText(""+ docs[count].getId_doc());
                                edtFio.setText(docs[count].getName());
                                setImgAdmins(docs[count].getImg());
                            }
                        });
                        btnDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDb.delete("Docs",
                                        "id_doc = ?",
                                        new String[] {edtIdUser.getText().toString()});
                                Toast.makeText(getContext(), "Запись удалена", Toast.LENGTH_SHORT).show();
                                updateDocsUI();
                                count--;
                            }
                        });
                        btnApply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ContentValues newValues = new ContentValues();
// Задайте значения для каждой строки.
                                    newValues.put(DOC_NAME,  edtFio.getText().toString());
                                    newValues.put(DOC_IMG,  docs[count].getImg());
                                    mDb.update (TABLE_DOCS, newValues, ID_DOC+"="+edtIdUser.getText().toString(), null);

                                Toast.makeText(getContext(), "Запись отредактирована!", Toast.LENGTH_SHORT).show();
                                updateDocsUI();
                            }
                        });
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(!is_edit){
                                    ClearAllEdt();
                                    is_edit=true;
                                    edtFio.setText("");
                                    Toast.makeText(getContext(), "Введите данные для добавления заявления", Toast.LENGTH_SHORT).show();
                                }

                                if(edtFio.getText().toString().equals(""))
                                    Toast.makeText(getContext(), "Не все поля заполнены ", Toast.LENGTH_LONG).show();
                                else {
                                    ContentValues newValues = new ContentValues();
                                    newValues.put(DOC_NAME, edtFio.getText().toString());
                                    newValues.put(DOC_IMG, imageUri.toString());

// Добавление в бд
                                    mDb.insert("Docs", null, newValues);
                                    count++;
                                    Toast.makeText(getContext(), "Запись добавлена!", Toast.LENGTH_SHORT).show();
                                    updateDocsUI();
                                    is_edit=false;
                                }
                            }
                        });
                        break;
                    case 3://предметы
                        edtPsw.setVisibility(View.GONE);
                        edtIdGroup.setVisibility(View.GONE);
                        edtGroupName.setVisibility(View.VISIBLE);
                        setModeSubj();


                        count=0;
                        /*if(users.length>0)
                        us.clear(users);*/
                        subj=mDBHelper.getAllSubjName_All();
                        edtIdUser.setText(""+ subj[count].getId_subject());
                        edtFio.setText(subj[count].getSubjectName());
                        edtMail.setText(""+subj[count].getLab_count());
                        edtIdGroup.setText(""+ subj[count].getPract_count());




                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                count=0;
                                subj=mDBHelper.getAllSubjName_All();
                                edtIdUser.setText(""+ subj[count].getId_subject());
                                edtFio.setText(subj[count].getSubjectName());
                                edtMail.setText(""+subj[count].getLab_count());
                                edtIdGroup.setText(""+ subj[count].getPract_count());
                                Toast.makeText(getContext(), "Обновлено", Toast.LENGTH_SHORT).show();
                            }
                        });
                        btnPrev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(count==0)
                                    count=mDBHelper.getAllSubjName_All().length-1;
                                else count=count-1;
                                edtIdUser.setText(""+ subj[count].getId_subject());
                                edtFio.setText(subj[count].getSubjectName());
                                edtMail.setText(""+subj[count].getLab_count());
                                edtIdGroup.setText(""+ subj[count].getPract_count());
                            }
                        });
                        btnNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(count== subj.length-1)
                                    count=0;
                                else count=count+1;
                                edtIdUser.setText(""+ subj[count].getId_subject());
                                edtFio.setText(subj[count].getSubjectName());
                                edtMail.setText(""+subj[count].getLab_count());
                                edtIdGroup.setText(""+ subj[count].getPract_count());
                            }
                        });
                        btnDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDb.delete(TABLE_DISC,
                                        ID_DISC+" = ?",
                                        new String[] {edtIdUser.getText().toString()});
                                Toast.makeText(getContext(), "Запись удалена", Toast.LENGTH_SHORT).show();
                                updateSubjectUI();
                                count--;
                            }
                        });
                        btnApply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ContentValues newValues = new ContentValues();
// Задайте значения для каждой строки.

                                    newValues.put(ID_DISC,  edtIdUser.getText().toString());
                                    newValues.put(DISC_NAME,  edtFio.getText().toString());
                                    newValues.put(DISC_LAB,  edtMail.getText().toString());
                                    newValues.put(DISC_PRACT, edtIdGroup.getText().toString());
                                    mDb.update (TABLE_DISC, newValues, ID_DISC+"="+edtIdUser.getText().toString(), null);

                                Toast.makeText(getContext(), "Запись отредактирована!", Toast.LENGTH_SHORT).show();
                                updateSubjectUI();
                            }
                        });
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ContentValues newValues = new ContentValues();
                                if(!is_edit){
                                    edtIdUser.setText("");
                                    edtFio.setText("");
                                    edtMail.setText("");
                                    edtIdGroup.setText("");
                                    is_edit=true;
                                    Toast.makeText(getContext(), "Введите данные для добавления надписи", Toast.LENGTH_SHORT).show();
                                }

                                if((edtFio.getText().toString().equals(""))
                                        ||edtMail.getText().toString().equals(""))
                                    Toast.makeText(getContext(), "Не все поля заполнены ", Toast.LENGTH_LONG).show();
                                else {
                                        newValues.put(DISC_NAME, edtFio.getText().toString());
                                        newValues.put(DISC_LAB, edtMail.getText().toString());
                                        newValues.put(DISC_PRACT, edtIdGroup.getText().toString());
// Добавление в бд
                                        mDb.insert(TABLE_DISC, null, newValues);
                                        count++;
                                        Toast.makeText(getContext(), "Запись добавлена!", Toast.LENGTH_SHORT).show();
                                        updateSubjectUI();
                                        is_edit=false;
                                }
                            }
                        });
                        break;
                    case 4://расписание
                        setModeSchedule();
                        count=0;
                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                count=0;
                                getResultsFromApi();
                                Toast.makeText(getContext(), "Обновлено", Toast.LENGTH_SHORT).show();
                            }
                        });
                        btnPrev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(count==0)
                                    count=schedules.size()-1;
                                else count=count-1;
                                ScheduleRecycleListAdapterAdm adapterr = new ScheduleRecycleListAdapterAdm(schedules.get(count),count,getContext(),AdminFragment.this::onDragStarted);
                                recyclerView.setAdapter(adapterr);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                if(count<=5)
                                edtFio.setText(days[count]+" Числитель");
                                else
                                    edtFio.setText(days[count]+" Знаменатель");
                                ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapterr);
                                mItemTouchHelper = new ItemTouchHelper(callback);
                                mItemTouchHelper.attachToRecyclerView(recyclerView);
                            }
                        });
                        btnNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(count == schedules.size()-1)
                                    count=0;
                                else count=count+1;
                                ScheduleRecycleListAdapterAdm adapterr = new ScheduleRecycleListAdapterAdm(schedules.get(count),count,getContext(),AdminFragment.this::onDragStarted);
                                recyclerView.setAdapter(adapterr);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                if(count<=5)
                                    edtFio.setText(days[count]+" Числитель");
                                else
                                    edtFio.setText(days[count]+" Знаменатель");
                                ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapterr);
                                mItemTouchHelper = new ItemTouchHelper(callback);
                                mItemTouchHelper.attachToRecyclerView(recyclerView);
                            }
                        });
                        btnDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(getContext(), "Для удаления смахните предмет влево или вправо", Toast.LENGTH_SHORT).show();

                                count--;
                            }
                        });
                        btnApply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (! isGooglePlayServicesAvailable()) {
                                    acquireGooglePlayServices();
                                } else if (mCredential.getSelectedAccountName() == null) {
                                    chooseAccount();
                                } else if (! isDeviceOnline()) {
                                    Toast.makeText(getActivity(), "No network connection available.", Toast.LENGTH_LONG).show();
                                } else {

                                    new AdminFragment.MakeRequestTaskUpd(mCredential).execute();
                                }

                                Toast.makeText(getContext(), "Запись отредактирована!", Toast.LENGTH_SHORT).show();
                                //updateSubjectUI();
                            }
                        });
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LayoutInflater li = LayoutInflater.from(getContext());
                                View promptsView = li.inflate(R.layout.prompt_adm, null);

                                //Создаем AlertDialog
                                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext());

                                //Настраиваем prompt.xml для нашего AlertDialog:
                                mDialogBuilder.setView(promptsView);

                                //Настраиваем отображение поля для ввода текста в открытом диалоге:
                                final EditText nameInput = (EditText) promptsView.findViewById(R.id.name_subject);
                                final EditText cabInput = (EditText) promptsView.findViewById(R.id.cab);
                                //Настраиваем сообщение в диалоговом окне:
                                mDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("Добавить",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        //Вводим текст и отображаем в строке ввода на основном экране:
                                                        Subject[] now= new Subject[schedules.get(count).length+1];
                                                        for(int i=0;i<now.length;i++){
                                                            if(i==now.length-1){
                                                                now[i]=new Subject(Time(i),nameInput.getText().toString(),"",cabInput.getText().toString());
                                                            }
                                                            else
                                                            now[i]= schedules.get(count)[i];
                                                        }
                                                        schedules.put(count,now);
                                                        ScheduleRecycleListAdapterAdm adapterr = new ScheduleRecycleListAdapterAdm(schedules.get(count),count,getContext(),AdminFragment.this::onDragStarted);
                                                        recyclerView.setAdapter(adapterr);
                                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapterr);
                                                        mItemTouchHelper = new ItemTouchHelper(callback);
                                                        mItemTouchHelper.attachToRecyclerView(recyclerView);

                                                    }
                                                })
                                        .setNegativeButton("Отмена",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                //Создаем AlertDialog:
                                AlertDialog alertDialog = mDialogBuilder.create();

                                //и отображаем его:
                                alertDialog.show();
                            }
                        });
                       // getResultsFromApi();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        List<String> namesList = Arrays.asList(mDBHelper.getAllGroupName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                root.getContext(), android.R.layout.simple_dropdown_item_1line, namesList);
        edtGroupName.setAdapter(adapter);
        edtGroupName.setThreshold(1);

        count=0;
       // us.clear(users);
        users=mDBHelper.getAllUser();
        edtIdUser.setText(""+ users[count].getIdUser());
        edtFio.setText(users[count].getFIO());
        edtMail.setText(users[count].getMail());
        edtPsw .setText(users[count].getPassword());
        edtIdGroup.setText(""+ users[count].getIdGroup());
        edtGroupName.setText(users[count].getGroupName());



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=0;
                //us.clear(users);
                users=mDBHelper.getAllUser();
               edtIdUser.setText(""+ users[count].getIdUser());
                edtFio.setText(users[count].getFIO());
                edtMail.setText(users[count].getMail());
                edtPsw .setText(users[count].getPassword());
                //edtIdGroup.setText(""+ users[count].getIdGroup());
                edtGroupName.setText(users[count].getGroupName());
                Toast.makeText(getContext(), "Обновлено", Toast.LENGTH_SHORT).show();
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0)
                    count=mDBHelper.getAllUser().length-1;
                else count=count-1;
                edtIdUser.setText(""+ users[count].getIdUser());
                edtFio.setText(users[count].getFIO());
                edtMail.setText(users[count].getMail());
                edtPsw .setText(users[count].getPassword());
                //edtIdGroup.setText(""+ users[count].getIdGroup());
                edtGroupName.setText(users[count].getGroupName());
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count== users.length-1)
                    count=0;
                else count=count+1;
                edtIdUser.setText(""+ users[count].getIdUser());
                edtFio.setText(users[count].getFIO());
                edtMail.setText(users[count].getMail());
                edtPsw .setText(users[count].getPassword());
                //edtIdGroup.setText(""+ users[count].getIdGroup());
                edtGroupName.setText(users[count].getGroupName());
            }
        });
btnDel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mDb.delete("users",
                "id_user = ?",
                new String[] {edtIdUser.getText().toString()});
        Toast.makeText(getContext(), "Запись удалена", Toast.LENGTH_SHORT).show();
        updateUsersUI();
        count--;
    }
});
btnApply.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        ContentValues newValues = new ContentValues();
// Задайте значения для каждой строки.
 if(mDBHelper.userIsExistAny(users[count])){
            newValues.put(USER_FIO,  edtFio.getText().toString());
            newValues.put(MAIL,  edtMail.getText().toString());
            newValues.put(PASSWORD,  edtPsw.getText().toString());
            newValues.put(ID_GROUP,  edtIdGroup.getText().toString());
            mDb.update (TABLE_USERS, newValues, ID_USER+"="+edtIdUser.getText().toString(), null);
       }
        Toast.makeText(getContext(), "Запись отредактирована!", Toast.LENGTH_SHORT).show();
        updateUsersUI();
    }
});
btnAdd.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ContentValues newValues = new ContentValues();
        if(!is_edit){
        ClearAllEdt();
        is_edit=true;
        Toast.makeText(getContext(), "Введите данные для добавления надписи", Toast.LENGTH_SHORT).show();
        }

        if(!anyEdtEmpty()&&mDBHelper.userIsExistWithMail(edtMail.getText().toString()))
            Toast.makeText(getContext(), "Не все поля заполнены или пользователь с такой почтой зарегистрирован", Toast.LENGTH_LONG).show();
        else {
            if(edtPsw.getText().toString().length()<8)
                Toast.makeText(getContext(), "Пароль должен быть длинне 8-ми символов", Toast.LENGTH_LONG).show();
            else {
                newValues.put(USER_FIO, edtFio.getText().toString());
                newValues.put(MAIL, edtMail.getText().toString());
                newValues.put(PASSWORD, edtPsw.getText().toString());
                newValues.put(ID_GROUP, mDBHelper.getGroupId(edtGroupName.getText().toString()));



// Добавление в бд
        mDb.insert(TABLE_USERS, null, newValues);
        count++;
        Toast.makeText(getContext(), "Запись добавлена!", Toast.LENGTH_SHORT).show();
        updateUsersUI();
        is_edit=false;
            }
    }
    }
});
        return root;
    }
    public void ClearAllEdtAdm(){
        edtIdUser.setText("");
        edtFio.setText("");
        edtMail.setText("");
        imgAdmins.setImageResource(R.drawable.ic_menu_gallery);
    }
    public void ClearAllEdt(){
        edtIdUser.setText("");
        edtFio.setText("");
        edtMail.setText("");
        edtPsw .setText("");
        edtIdGroup.setText("");
        imgAdmins.setImageResource(R.drawable.ic_menu_gallery);
    }
    public boolean anyEdtEmpty(){
        if((edtFio.getVisibility()!=View.GONE &&edtFio.getText().toString().equals(""))
                ||(edtMail.getVisibility()!=View.GONE &&edtMail.getText().toString().equals(""))
                ||(edtPsw.getVisibility()!=View.GONE &&edtPsw.getText().toString().equals(""))
                ||(edtGroupName.getVisibility()!=View.GONE &&edtGroupName.getText().equals("")))
        return true;
        else return false;
    }
    public boolean anyEdtEmptyAdm(){
        if(edtFio.getText().toString().equals("")
                ||edtMail.getText().toString().equals(""))
            return true;
        else return false;
    }
    public boolean allEdtEmpty(){
        if(edtFio.getText().toString()==""
                &&edtMail.getText().toString()==""
                &&edtPsw.getText().toString()==""
                &&edtGroupName.getText().toString()=="")
            return true;
        else return false;
    }
    public void updateSubjectUI(){
        count=0;
//        us.clear(users);
        subj=mDBHelper.getAllSubjName_All();
        edtIdUser.setText(""+ subj[count].getId_subject());
        edtFio.setText(subj[count].getSubjectName());
        edtMail.setText(""+subj[count].getLab_count());
        edtIdGroup.setText(""+subj[count].getPract_count());
    }
    public void updateUsersUI(){
        count=0;
//        us.clear(users);
        users=mDBHelper.getAllUser();
        edtIdUser.setText(""+ users[count].getIdUser());
        edtFio.setText(users[count].getFIO());
        edtMail.setText(users[count].getMail());
        edtPsw .setText(users[count].getPassword());
        //edtIdGroup.setText(""+ users[count].getIdGroup());
        edtGroupName.setText(users[count].getGroupName());
    }
    public void updateAdminsUI(){
        count=0;
        //adm.clear(admins);
        admins=mDBHelper.getAllAdmins();
        edtIdUser.setText(""+ admins[count].getId_admins());
        edtFio.setText(admins[count].getFio());
        edtMail.setText(admins[count].getDiljn());
        setImgAdmins(admins[count].getImg());
    }
    public void updateDocsUI(){
        count=0;
        //adm.clear(admins);
        docs=mDBHelper.getAllDocs();
        edtIdUser.setText(""+ docs[count].getId_doc());
        edtFio.setText(docs[count].getName());
        setImgAdmins(docs[count].getImg());
    }
    public void setModeAdmin(){

        count=0;
        //adm.clear(admins);
        admins=mDBHelper.getAllAdmins();
        edtIdUser.setText(""+ admins[count].getId_admins());
        edtFio.setText(admins[count].getFio());
        edtIdUser.setHint(R.string.un_id);
        edtMail.setText(admins[count].getDiljn());
        edtMail.setHint(R.string.admin_doljn);
        edtFio.setHint(R.string.un_fio);
        edtIdGroup.setVisibility(View.VISIBLE);
        edtMail.setText(""+admins[count].getPhone());
        edtIdGroup.setHint("Телефон");

        imgAdmins.setVisibility(View.VISIBLE);
        edtPsw.setVisibility(View.GONE);

        edtGroupName.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }
    public void setModeZayvl(){

        count=0;
        //adm.clear(admins);
        docs=mDBHelper.getAllDocs();
        edtIdUser.setText(""+ docs[count].getId_doc());
        edtFio.setText(docs[count].getName());
        edtIdUser.setHint(R.string.un_id);
        edtFio.setHint("Наименование документа");
        edtMail.setVisibility(View.GONE);
        imgAdmins.setVisibility(View.VISIBLE);
        edtPsw.setVisibility(View.GONE);
        edtIdGroup.setVisibility(View.GONE);
        edtGroupName.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }
    public void setModeUsers(){

        imgAdmins.setVisibility(View.GONE);
        edtPsw.setVisibility(View.VISIBLE);
        edtIdGroup.setVisibility(View.GONE);
        edtGroupName.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        count=0;
//        us.clear(users);
        users=mDBHelper.getAllUser();
        edtIdUser.setText(""+ users[count].getIdUser());
        edtFio.setText(users[count].getFIO());
        edtIdUser.setHint(R.string.un_id);
        edtMail.setText(users[count].getMail());
        edtMail.setHint(R.string.prompt_email);
        edtFio.setHint(R.string.un_fio);
    }
    public void setModeSubj(){

        imgAdmins.setVisibility(View.GONE);
        edtPsw.setVisibility(View.GONE);
        edtIdGroup.setVisibility(View.VISIBLE);
        edtGroupName.setVisibility(View.GONE);
        edtMail.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        count=0;
//        us.clear(users);
        subj=mDBHelper.getAllSubjName_All();
        edtIdUser.setText(""+ subj[count].getId_subject());
        edtFio.setText(subj[count].getSubjectName());
        edtFio.setHint("Название предмета");
        edtIdUser.setHint(R.string.un_id);
        edtMail.setText(""+subj[count].getLab_count());
        edtMail.setHint("Количество лабораторных");
        edtIdGroup.setText(""+subj[count].getPract_count());
        edtIdGroup.setHint("Количество практических");

    }
    public void setModeSchedule(){
        edtIdUser.setVisibility(View.GONE);
        imgAdmins.setVisibility(View.GONE);
        edtPsw.setVisibility(View.GONE);
        edtIdGroup.setVisibility(View.GONE);
        edtGroupName.setVisibility(View.VISIBLE);
        edtMail.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        List<String> namesList = Arrays.asList(mDBHelper.getAllGroupName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_dropdown_item_1line, namesList);
        edtGroupName.setAdapter(adapter);
        edtGroupName.setThreshold(1);
        edtGroupName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(edtGroupName.getText().toString().equals("Admin Group"))
                    Toast.makeText(getActivity().getApplicationContext(), "Расписание не доступно для администраторов", Toast.LENGTH_SHORT).show();
                else if(edtGroupName.getText().toString().equals("42и")||edtGroupName.getText().toString().equals("32и"))
                    getResultsFromApi();
                else Toast.makeText(getActivity().getApplicationContext(), "Расписание данной группы пока недоступно", Toast.LENGTH_LONG).show();
            }
        });
        count=0;
        getResultsFromApi();

        //edtIdUser.setText(""+ subjects[count].get);


    }
    public void setImgAdmins(String pathImg){
        InputStream inputStream = null;
        try{
            inputStream = getActivity().getApplicationContext().getAssets().open(pathImg);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imgAdmins.setImageDrawable(drawable);
            imgAdmins.setScaleType(ImageView.ScaleType.FIT_XY);
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
   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    try {

                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                         imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        File outFile=null;
                        FileOutputStream out = null;
                        File storage = getActivity().getDataDir();
                        try {

                            File dir = new File(storage.getPath());
                            dir.mkdirs();
                            outFile = new File(dir, admins[count].getId_admins()+"_.jpg");

                            out = new FileOutputStream(outFile);
                            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                            // PNG is a lossless format, the compression factor (100) is ignored
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        imgAdmins.setImageURI(Uri.parse(storage.getPath()+admins[count].getId_admins()+"_.jpg"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }}*/




    @Override
    public void onDragStarted(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
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
    public String getDayRangeSubj(String day, int count){

        String Day="";
        switch (day){

            case "Понедельник": Day= "C"+count+":"+"D"+count; break;
            case "Mon": Day=  "C"+count+":"+"D"+count; break;
            case "Вторник": Day= "E"+count+":"+"F"+count;break;
            case "Tue": Day= "E"+count+":"+"F"+count; break;
            case "Среда": Day= "G"+count+":"+"H"+count;break;
            case "Wed": Day= "G"+count+":"+"H"+count; break;
            case "Четверг": Day= "I"+count+":"+"J"+count;break;
            case "Thu": Day= "I"+count+":"+"J"+count; break;
            case "Пятница": Day= "K"+count+":"+"L"+count;break;
            case "Fri": Day= "K"+count+":"+"L"+count; break;

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

            new AdminFragment.MakeRequestTask(mCredential).execute();
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
                if(edtGroupName.getText().toString().equals("32и"))
                    return getDataFromApi(SPREAD_SHEET_32I,TABLE_NAME_CH,getCurrentDay());

                else
                if(edtGroupName.getText().toString().equals("42и"))
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
                subjects=new Subject[results.size()];
                for (int i = 0; i < fin_res.length; i++){
                    fin_res[i] = results.get(i);
                    subjects[i]=fin_res[i];
                }
                schedules.put(k,fin_res);
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

                ScheduleRecycleListAdapterAdm adapterr = new ScheduleRecycleListAdapterAdm(subjects,count,getContext(),AdminFragment.this::onDragStarted);
                recyclerView.setAdapter(adapterr);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                /*ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapterr);
                mItemTouchHelper = new ItemTouchHelper(callback);
                mItemTouchHelper.attachToRecyclerView(recyclerView);*/

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




    /**
     * An asynchronous task that handles the Google Sheets API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTaskUpd extends AsyncTask<Void, Void, Map<String,Subject[]>> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        MakeRequestTaskUpd(GoogleAccountCredential credential) {
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
                if(edtGroupName.getText().toString().equals("32и"))
                    return getDataFromApi(SPREAD_SHEET_32I,TABLE_NAME_CH,getCurrentDay());

                else
                if(edtGroupName.getText().toString().equals("42и"))
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


           // recyclerView.get
            /*for(int i=0;i<10;i++) {//заполнение всех дней

                /*ScheduleRecycleListAdapterAdm adapterr =new ScheduleRecycleListAdapterAdm((ScheduleRecycleListAdapterAdm) recyclerView.getAdapter());
                schedules.remove(i);
                schedules.put(i,adapterr.getSubjects());*/
                //if(i==count){
                List<ValueRange> appendBody = new ArrayList<>();
                ScheduleRecycleListAdapterAdm adapterAdm=(ScheduleRecycleListAdapterAdm) recyclerView.getAdapter();
                schedules.remove(count);
                schedules.put(count,adapterAdm.getSubjects());
               // }
                for(int j=0;j<schedules.get(count).length;j++){//заполнение конкретноого дня

                ValueRange one = new ValueRange()
                        .setValues(Arrays.asList(
                                Arrays.asList(schedules.get(count)[j].getSubjectName(),schedules.get(count)[j].getCab())
                        ));
                if(count<=5)
                    one.setRange(TABLE_NAME_CH +"!"+ getDayRangeSubj(days[count],j+2));
                else
                    one.setRange( TABLE_NAME_ZN+"!"+ getDayRangeSubj(days[count],j+2));
                appendBody.add(one);
                BatchUpdateValuesRequest requestBody = new BatchUpdateValuesRequest()
                            .setValueInputOption("USER_ENTERED")
                            .setData(appendBody);
                    Sheets.Spreadsheets.Values.BatchUpdate request =
                            mService.spreadsheets().values().batchUpdate(spreadsheetId, requestBody);
                    BatchUpdateValuesResponse response = request.execute();
                }

           // }

           // int count=getCountDay(getNameDay(getCurrentDay()));
            for(int k=/*count*/0;k</*count+*/10;k++) {
                /*if(k<4){
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
                subjects=new Subject[results.size()];
                for (int i = 0; i < fin_res.length; i++){
                    fin_res[i] = results.get(i);
                    subjects[i]=fin_res[i];
                }*/
                //schedules.put(k,fin_res);
                //schedule.put(days[k],fin_res);
            }
            return null;
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
           /* if (output == null || output.size() == 0) {
                Toast.makeText(getContext(), "Расписание для данной группы пока не доступно", Toast.LENGTH_LONG).show();
            }/* else {
                scheldule=output;

                ScheduleRecycleListAdapterAdm adapterr = new ScheduleRecycleListAdapterAdm(subjects,count,getContext(),AdminFragment.this::onDragStarted);
                recyclerView.setAdapter(adapterr);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapterr);
                mItemTouchHelper = new ItemTouchHelper(callback);
                mItemTouchHelper.attachToRecyclerView(recyclerView);

            }*/

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


