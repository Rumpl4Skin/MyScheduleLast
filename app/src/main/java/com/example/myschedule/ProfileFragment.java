package com.example.myschedule;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myschedule.data.model.LoggedInUser;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    EditText fio,mail,pass;
    Button btnApply,btnCancel,btnEdit;
    CheckBox checkBox;
    private DbHelper mDBHelper;
    private SQLiteDatabase mDb;
    AutoCompleteTextView group;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_profile, container, false);

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

        fio=root.findViewById(R.id.edtFio);
        mail=root.findViewById(R.id.edtMail);
        pass=root.findViewById(R.id.edtPass);
        group=root.findViewById(R.id.edtIDGroup);

        btnApply=root.findViewById(R.id.btnApply);
        btnCancel=root.findViewById(R.id.btnCancel);
        btnEdit=root.findViewById(R.id.btnEdit);

        checkBox=root.findViewById(R.id.checkBox);

        LoggedInUser user=new LoggedInUser(
                getArguments().get("user_fio").toString(),
                getArguments().get("user_mail").toString(),
                getArguments().get("user_pass").toString(),
                getArguments().get("user_group_name").toString());

        setUserUI(user);

        List<String> namesList = Arrays.asList(mDBHelper.getAllGroupName());
        if(user.getGroupName()=="Admin Group")// админов добавляют только админы
            namesList.removeIf(x->x.equals("Admin Group"));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                root.getContext(), android.R.layout.simple_dropdown_item_1line, namesList);
        group.setAdapter(adapter);
        group.setThreshold(1);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fio.isEnabled()==false){
                fio.setEnabled(true);
                mail.setEnabled(true);
                pass.setEnabled(true);
                group.setEnabled(true);
                checkBox.setEnabled(true);
                btnApply.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                }
                else{
                    fio.setEnabled(false);
                    mail.setEnabled(false);
                    pass.setEnabled(false);
                    group.setEnabled(false);
                    checkBox.setEnabled(false);
                    btnApply.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                }
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    pass.setInputType(129);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fio.setEnabled(false);
                mail.setEnabled(false);
                pass.setEnabled(false);
                group.setEnabled(false);
                checkBox.setEnabled(false);
                btnApply.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                setUserUI(user);
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoggedInUser update_user=new LoggedInUser(
                        fio.getText().toString(),
                        mail.getText().toString(),
                        pass.getText().toString(),
                        group.getText().toString());
                if(mDBHelper.groupIsExist(update_user)) {
                    mDBHelper.userUpdate(user, update_user);
                    Snackbar.make(v, "Профиль изменен", Snackbar.LENGTH_LONG).setAnchorView(btnApply).show();
                    UserUpdUI(update_user);
                    fio.setEnabled(false);
                    mail.setEnabled(false);
                    pass.setEnabled(false);
                    group.setEnabled(false);
                    checkBox.setEnabled(false);
                    btnApply.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                }
                else Snackbar.make(v, "Набранная группа не существует", Snackbar.LENGTH_LONG).setAnchorView(btnApply).show();

            }
        });
        return root;

    }
    public void setUserUI(LoggedInUser user){
        fio.setText(user.getFIO());
        mail.setText(user.getMail());
        pass.setText(user.getPassword());
        group.setText(user.getGroupName());
    }

    public void UserUpdUI(LoggedInUser user){
        setUserUI(user);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
            View header=navigationView.getHeaderView(0);
            TextView user_fio=(TextView) header.findViewById(R.id.user_fio);
            TextView user_mail=(TextView) header.findViewById(R.id.user_mail);
            user_fio.setText(user.getFIO());
            user_mail.setText(user.getMail());
    }
}