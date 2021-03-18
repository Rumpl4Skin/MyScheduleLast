package com.example.myschedule.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.R;
import com.example.myschedule.data.RecyclerViewScheduleHolderAdm;
import com.example.myschedule.data.Subject;
import com.example.myschedule.helper.ItemTouchHelperAdapter;
import com.example.myschedule.ui.OnStartDragListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ScheduleRecycleListAdapterAdm extends RecyclerView.Adapter<RecyclerViewScheduleHolderAdm>implements ItemTouchHelperAdapter {

    private Subject[] subjects;
    private Context context;
    private ArrayList<Subject> sbj;
    private int count;
    private final OnStartDragListener mDragStartListener;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position) {

    }

    /*public ScheduleRecycleListAdapter(Subject[] subjects) {
        this.subjects = subjects;
    }*/
    public interface OnDragStartListener {
        void onDragStarted(RecyclerView.ViewHolder viewHolder);
    }
    public ScheduleRecycleListAdapterAdm(ArrayList<Subject> subjects, OnStartDragListener mDragStartListener) {
        this.sbj = subjects;
        this.mDragStartListener = mDragStartListener;
    }

    public ScheduleRecycleListAdapterAdm(Subject[] subjects, Context context, OnStartDragListener dragStartListener) {
        this.subjects = subjects;
        this.context = context;
        this.count=count;
        this.mDragStartListener = dragStartListener;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.frame_schedule_adm;
    }

    @NonNull
    @Override
    public RecyclerViewScheduleHolderAdm onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewScheduleHolderAdm(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewScheduleHolderAdm holder, int position) {
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
                                        SharedPreferences  sPref = context.getSharedPreferences("Comments", MODE_PRIVATE);

                                        SharedPreferences.Editor ed = sPref.edit();
                                        ed.putString(count++ +""+position+""+context.getSharedPreferences("MainActivity", MODE_PRIVATE)
                                                .getString("group",""), userInput.getText().toString());
                                        ed.commit();
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

        holder.getHandleView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjects.length;
    }
}
