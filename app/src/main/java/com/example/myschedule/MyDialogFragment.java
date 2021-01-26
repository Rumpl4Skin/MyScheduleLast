package com.example.myschedule;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.myschedule.ui.login.LoginActivity;

public class MyDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Выход";
        String message = "Куда вы хотите вернуться?";
        String button1String = "Вернуться к форме входа";
        String button2String = "Выйти из приложения";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);  // заголовок
        builder.setMessage(message); // сообщение
        builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().finishAffinity();
            }
        });
        builder.setCancelable(true);

        return builder.create();
    }

    public Dialog MyDialogFragment(String title, String mes, String btn1, String btn2, DialogInterface.OnClickListener listener1,DialogInterface.OnClickListener listener2) {
        String t = title;
        String message = mes;
        String button1String = btn1;
        String button2String = btn2;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(t);  // заголовок
        builder.setMessage(message); // сообщение
        builder.setPositiveButton(button1String, listener1);
        builder.setNegativeButton(button2String, listener2);
        builder.setCancelable(true);

        return builder.create();
    }
}
