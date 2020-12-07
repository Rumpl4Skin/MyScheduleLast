package com.example.myschedule.ui.admin;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

    EditText edtIdUser,edtFio,edtMail,edtPsw,edtIdGroup,edtGroupName;
    ImageView btnPrev,btnUpdate,btnDel,btnAplly,btnNext;
    int count=0;

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
        LoggedInUser[] users=mDBHelper.getAllUser();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //galleryViewModel.setAllUsers(users);

               edtIdUser.setText(""+users[0].getIdUser());
                edtFio.setText(users[0].getFIO());
                edtMail.setText(users[0].getMail());
                edtPsw .setText(users[0].getPassword());
                edtIdGroup.setText(""+users[0].getIdGroup());
                //edtGroupName.setText((int)users.get(0).getGroupName());
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
