package com.example.myschedule.ui.admin;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myschedule.DbHelper;
import com.example.myschedule.R;
import com.example.myschedule.data.model.LoggedInUser;
import com.example.myschedule.ui.gallery.GalleryViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminFragment extends Fragment {

    private AdminViewModel galleryViewModel;
    private DbHelper mDBHelper;
    private SQLiteDatabase mDb;

    private static final String TABLE_USERS = "users";
    private static final String ID_USER = "id_user";
    private static final String USER_FIO = "fio";
    private static final String MAIL = "mail";
    private static final String ID_GROUP = "id_group";
    private static final String PASSWORD = "password";

    private static final String TABLE_GROUPS = "groups";
    private static final String GROUP_NAME = "group_name";

    EditText edtIdUser,edtFio,edtMail,edtPsw,edtIdGroup,edtGroupName;
    ImageView btnPrev,btnUpdate,btnDel,btnAplly,btnNext;
    public int count=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*galleryViewModel =
                new ViewModelProvider(this).get(AdminViewModel.class);*/
        View root = inflater.inflate(R.layout.fragment_admin, container, false);

        edtIdUser = root.findViewById(R.id.id_user);
        edtFio = root.findViewById(R.id.fio);
        edtMail = root.findViewById(R.id.mail);
        edtPsw = root.findViewById(R.id.password);
        edtIdGroup = root.findViewById(R.id.id_group);
        edtGroupName = root.findViewById(R.id.group_name);

        btnPrev = root.findViewById(R.id.prev);
        btnUpdate = root.findViewById(R.id.update);
        btnAplly = root.findViewById(R.id.apply);
        btnNext = root.findViewById(R.id.next);
        btnDel=root.findViewById(R.id.delete);

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
      final LoggedInUser[] users=mDBHelper.getAllUser();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //galleryViewModel.setAllUsers(users);

               edtIdUser.setText(""+users[count].getIdUser());
                edtFio.setText(users[count].getFIO());
                edtMail.setText(users[count].getMail());
                edtPsw .setText(users[count].getPassword());
                edtIdGroup.setText(""+users[count].getIdGroup());
                //edtGroupName.setText((int)users.get(0).getGroupName());
                Toast.makeText(getContext(), "Обновлено", Toast.LENGTH_SHORT).show();
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0)
                    count=mDBHelper.getAllUser().length-1;
                else count=count-1;
                edtIdUser.setText(""+users[count].getIdUser());
                edtFio.setText(users[count].getFIO());
                edtMail.setText(users[count].getMail());
                edtPsw .setText(users[count].getPassword());
                edtIdGroup.setText(""+users[count].getIdGroup());
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==users.length-1)
                    count=0;
                else count=count+1;
                edtIdUser.setText(""+users[count].getIdUser());
                edtFio.setText(users[count].getFIO());
                edtMail.setText(users[count].getMail());
                edtPsw .setText(users[count].getPassword());
                edtIdGroup.setText(""+users[count].getIdGroup());
            }
        });
btnDel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mDb.delete("users",
                "id_user = ?",
                new String[] {edtIdUser.getText().toString()});
        Toast.makeText(getContext(), "Запись удалена, обновите админку", Toast.LENGTH_SHORT).show();
        count--;
    }
});
btnAplly.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        ContentValues newValues = new ContentValues();
// Задайте значения для каждой строки.
        newValues.put(ID_USER,  edtIdUser.getText().toString());
        newValues.put(USER_FIO,  edtFio.getText().toString());
        newValues.put(MAIL,  edtMail.getText().toString());
        newValues.put(PASSWORD,  edtPsw.getText().toString());
        newValues.put(ID_GROUP,  edtIdGroup.getText().toString());

// Вставьте строку в вашу базу данных.
        mDb.insert(TABLE_USERS, null, newValues);
        count++;
        //users=mDBHelper.getAllUser();
mDb.close();
        Toast.makeText(getContext(), "Запись отредактирована!", Toast.LENGTH_SHORT).show();
    }
});
/*galleryViewModel.getUserId().observe(getViewLifecycleOwner(), new Observer<Integer>() {
    @Override
    public void onChanged(Integer integer) {
        edtIdUser.setText(integer);
    }
});*/
       /* galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                edtIdUser.setText(s);
            }
        });*/
        return root;
    }
}
