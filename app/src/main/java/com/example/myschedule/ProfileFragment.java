package com.example.myschedule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.myschedule.data.model.LoggedInUser;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }
    EditText fio,mail,pass,group;
    Button btnApply,btnCancel,btnEdit;
    CheckBox checkBox;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_profile, container, false);
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

        fio.setText(user.getFIO());
        mail.setText(user.getMail());
        pass.setText(user.getPassword());
        group.setText(user.getGroupName());

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
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return root;
    }
}