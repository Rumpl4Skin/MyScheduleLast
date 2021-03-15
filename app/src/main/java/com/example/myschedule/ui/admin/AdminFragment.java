package com.example.myschedule.ui.admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myschedule.DbHelper;
import com.example.myschedule.R;
import com.example.myschedule.data.Admins;
import com.example.myschedule.data.Docs;
import com.example.myschedule.data.Subject;
import com.example.myschedule.data.model.LoggedInUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AdminFragment extends Fragment {

    private DbHelper mDBHelper;
    private SQLiteDatabase mDb;

    private static final String MOD_USERS = "Редактирование пользователей";
    private static final String MOD_ADMIN = "Редактирование администрации";
    private static final String MOD_ZAYV = "Редактирование образцов заявлений";
    private static final String MOD_DISC = "Редактирование списка дисциплин";

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
    private static final String ADMINS_IMG = "admins_img";

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
    String[] mods = { MOD_USERS, MOD_ADMIN ,MOD_ZAYV,  MOD_DISC};
    String selected_mode=MOD_USERS;
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
/*
        us=new LoggedInUser();
        users = mDBHelper.getAllUser();
        adm=new Admins();
        admins = mDBHelper.getAllAdmins();*/


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
                        if(admins[count].getImg()!=null)
                        setImgAdmins(admins[count].getImg());
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
                                admins=mDBHelper.getAllAdmins();
                                edtIdUser.setText(""+ admins[count].getId_admins());
                                edtFio.setText(admins[count].getFio());
                                edtMail.setText(admins[count].getDiljn());
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
                                updateAdminsUI();
                                count--;
                            }
                        });
                        /*btnApply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ContentValues newValues = new ContentValues();
// Задайте значения для каждой строки.
                                if(mDBHelper.adminIsExistAny(admins[count])){
                                    newValues.put(ADMINS_FIO,  edtFio.getText().toString());
                                    newValues.put(ADMINS_DOLJN,  edtMail.getText().toString());
                                    newValues.put(ADMINS_IMG,  admins[count].getImg());
                                    mDb.update (TABLE_ADMINS, newValues, ID_ADMINS+"="+edtIdUser.getText().toString(), null);
                                }
                                Toast.makeText(getContext(), "Запись отредактирована!", Toast.LENGTH_SHORT).show();
                                updateAdminsUI();
                            }
                        });*/
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
                                    newValues.put(ADMINS_IMG, imageUri.toString());

// Добавление в бд
                                    mDb.insert("Docs", null, newValues);
                                    count++;
                                    Toast.makeText(getContext(), "Запись добавлена!", Toast.LENGTH_SHORT).show();
                                    updateUsersUI();
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
                        //edtIdUser.setText(""+ users[count].getIdUser());
                        edtFio.setText(subj[count].getSubjectName());
                        edtMail.setText(""+subj[count].getLab_count());
                        edtPsw .setText(""+subj[count].getPract_count());
                        edtIdGroup.setText(""+ subj[count].getId_subject());




                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                count=0;
                                subj=mDBHelper.getAllSubjName_All();
                                edtIdUser.setText(""+ subj[count].getId_subject());
                                edtFio.setText(subj[count].getSubjectName());
                                edtMail.setText(subj[count].getLab_count());
                                edtPsw .setText(subj[count].getPract_count());
                                edtIdGroup.setText(""+ users[count].getIdGroup());
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
                                edtPsw .setText(""+subj[count].getPract_count());
                                edtIdGroup.setText(""+ subj[count].getId_subject());
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
                                edtPsw .setText(""+subj[count].getPract_count());
                                edtIdGroup.setText(""+ subj[count].getId_subject());
                            }
                        });
                        btnDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDb.delete("subject",
                                        "id_subject = ?",
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
    }
    public boolean anyEdtEmpty(){
        if(edtFio.getText().toString()==""
                ||edtMail.getText().toString()==""
                ||edtPsw.getText().toString()==""
                ||edtGroupName.getText().toString()=="")
        return true;
        else return false;
    }
    public boolean anyEdtEmptyAdm(){
        if(edtFio.getText().toString()==""
                ||edtMail.getText().toString()=="")
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

        imgAdmins.setVisibility(View.VISIBLE);
        edtPsw.setVisibility(View.GONE);
        edtIdGroup.setVisibility(View.GONE);
        edtGroupName.setVisibility(View.GONE);
    }
    public void setModeZayvl(){

        count=0;
        //adm.clear(admins);
        docs=mDBHelper.getAllDocs();
        edtIdUser.setText(""+ docs[count].getId_doc());
        edtFio.setText(docs[count].getName());
        edtIdUser.setHint(R.string.un_id);
        edtMail.setVisibility(View.GONE);
        imgAdmins.setVisibility(View.VISIBLE);
        edtPsw.setVisibility(View.GONE);
        edtIdGroup.setVisibility(View.GONE);
        edtGroupName.setVisibility(View.GONE);
    }
    public void setModeUsers(){

        imgAdmins.setVisibility(View.GONE);
        edtPsw.setVisibility(View.VISIBLE);
        edtIdGroup.setVisibility(View.GONE);
        edtGroupName.setVisibility(View.VISIBLE);

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
        edtIdGroup.setVisibility(View.GONE);
        edtGroupName.setVisibility(View.VISIBLE);

        count=0;
//        us.clear(users);
        subj=mDBHelper.getAllSubjName_All();
        edtIdUser.setText(""+ subj[count].getId_subject());
        edtFio.setText(subj[count].getSubjectName());
        edtIdUser.setHint(R.string.un_id);
        edtMail.setText(""+subj[count].getLab_count());
        edtMail.setHint(R.string.prompt_email);
        edtFio.setHint(R.string.un_fio);
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
    @Override
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
        }}}


