package com.example.myschedule.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myschedule.R;
import com.example.myschedule.data.Admins;
import com.example.myschedule.data.RecyclerViewHolder;
import com.example.myschedule.data.RecyclerViewScheduleHolder;
import com.example.myschedule.data.Schedule;
import com.example.myschedule.data.Subject;

import java.util.ArrayList;

public class ScheduleRecycleListAdapter extends RecyclerView.Adapter<RecyclerViewScheduleHolder> {

    private Subject[] subjects;
    private Context context;
    private ArrayList<Subject> sbj;

    public ScheduleRecycleListAdapter(Subject[] subjects) {
        this.subjects = subjects;
    }

    public ScheduleRecycleListAdapter(ArrayList<Subject> subjects) {
        this.sbj = subjects;
    }

    public ScheduleRecycleListAdapter(Subject[] subjects, Context context) {
        this.subjects = subjects;
        this.context = context;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.frame_schedule;
    }

    @NonNull
    @Override
    public RecyclerViewScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewScheduleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewScheduleHolder holder, int position) {
        if(subjects!=null) {
            holder.getTxtTime().setText(String.valueOf(subjects[position].getTime()));
            if(subjects[position].getCab()=="")
                holder.getTxtCab().setText(String.valueOf(subjects[position].getCab() + "--"));
            else
            holder.getTxtCab().setText(String.valueOf(subjects[position].getCab() + " кб"));
            holder.getTxtSubject().setText(String.valueOf(subjects[position].getSubjectName()));
            holder.getTxtComm().setText(String.valueOf(subjects[position].getComm()));
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//Получаем вид с файла prompt.xml, который применим для диалогового окна:
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompt, null);

                //Создаем AlertDialog
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                //Настраиваем prompt.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);

                //Настраиваем отображение поля для ввода текста в открытом диалоге:
                final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);

                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Добавить",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        //Вводим текст и отображаем в строке ввода на основном экране:
                                        holder.getTxtComm().setText(userInput.getText());
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
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return subjects.length;
    }
}
