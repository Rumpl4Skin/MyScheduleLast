package com.example.myschedule.ui.home;


import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.DbHelper;
import com.example.myschedule.R;
import com.example.myschedule.adapters.ScheduleRecycleListAdapter;
import com.example.myschedule.data.Subject;
import com.example.myschedule.data.model.LoggedInUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private DbHelper mDBHelper;
    private SQLiteDatabase mDb;
    AutoCompleteTextView GroupName;
    private LoggedInUser user;
    AssetManager assetManager;
    Subject[] subjects;
    TextView curentGroup;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mDBHelper = new DbHelper(root.getContext());
        Bundle arguments = getActivity().getIntent().getExtras();
        GroupName=root.findViewById(R.id.group_namesHome);
        curentGroup=root.findViewById(R.id.curent_group);
        recyclerView = root.findViewById(R.id.shedule_RecList);
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

        setAdapter(user.getGroupName());
        update(GroupName.getText().toString());
        assetManager = getActivity().getAssets();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(this.subjects != null)
            recyclerView.setAdapter(new ScheduleRecycleListAdapter(subjects));

        return root;
    }
    public void update(String groupName){

                if(!GroupName.getText().toString().equals(user.getGroupName()))
                    curentGroup.setVisibility(View.GONE);
                else curentGroup.setVisibility(View.VISIBLE);
                if(GroupName.getText().toString().equals("Admin Group"))
                    Toast.makeText(getActivity().getApplicationContext(), "Расписание не доступно для администраторов", Toast.LENGTH_SHORT).show();
                else if(groupName.equals("10"))
                {
                    Subject[] subjects={new Subject("8.00-8.45","Иностранный язык","","28,34,47"),
                            new Subject("8.55-9.40","Иностранный язык","","28,34,47"),
                            new Subject("9.50-10.35","Русский язык","","16"),
                            new Subject("10.45-11.30","Физическая культура и здоровье","",""),
                            new Subject("11.40-12.25","Обед","",""),
                            new Subject("12.35-13.20","Математика","","33"),
                            new Subject("13.30-14.15","Математика","","33")};
                    recyclerView.setAdapter(new ScheduleRecycleListAdapter(subjects,getContext()));
                }

                else if(groupName.equals("20"))
                {
                    Subject[] subjects={new Subject("8.00-8.45","Производственное обучение","","маст."),
                            new Subject("8.55-9.40","Производственное обучение","","маст."),
                            new Subject("9.50-10.35","Производственное обучение","","маст."),
                            new Subject("10.45-11.30","Производственное обучение","","маст."),
                            new Subject("11.40-12.25","Компьютерные сети","","31"),
                            new Subject("12.35-13.20","Обед","",""),
                            new Subject("13.30-14.15","Электронный офис","","32б,31"),
                            new Subject("14.25-15.10","Иностранный язык","","28,47"),
                            new Subject("15.20-16.05","Иностранный язык","","28,47"),};
                    recyclerView.setAdapter(new ScheduleRecycleListAdapter(subjects,getContext()));
                }

                else if(groupName.equals("30"))
                {
                    Subject[] subjects={new Subject("8.00-8.45","Основы экономики","","23"),
                            new Subject("8.55-9.40","Основы экономики","","23"),
                            new Subject("9.50-10.35","Офисное программирование","","21,22"),
                            new Subject("10.45-11.30","Офисное программирование","","21,22"),
                            new Subject("11.40-12.25","Основы экономики","","23"),
                            new Subject("12.35-13.20","Операционные системы","","31,21"),
                            new Subject("13.30-14.15","Обед","",""),
                            new Subject("14.25-15.10","Основы экономики","","23"),
                            new Subject("15.20-16.05","Физическая культура и здоровье","",""),
                            new Subject("15.20-16.05","Операционные системы","","31,21")};
                    recyclerView.setAdapter(new ScheduleRecycleListAdapter(subjects,getContext()));
                }

                else if(groupName.equals("10д"))
                {
                    Subject[] subjects={
                            new Subject("8.55-9.40","Подготовка учащихся к семейной жизни (ф)","","37"),
                            new Subject("9.50-10.35","Психология и этика деловых отношений","","17"),
                            new Subject("10.45-11.30","Психология и этика деловых отношений","","17"),
                            new Subject("11.40-12.25","Обед","",""),
                            new Subject("12.35-13.20","Производственное обучение","","маст."),
                            new Subject("13.30-14.15","Производственное обучение","","маст."),
                            new Subject("14.25-15.10","Производственное обучение","","маст."),
                            new Subject("15.20-16.05","Производственное обучение","","маст."),
                            new Subject("16.15-17.00","Воспитательный час","","36")};
                    recyclerView.setAdapter(new ScheduleRecycleListAdapter(subjects,getContext()));
                }
                else Toast.makeText(getActivity().getApplicationContext(), "Расписание для этой группы пока не доступно", Toast.LENGTH_SHORT).show();
    }
    public void setAdapter(String groupName){
        GroupName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!GroupName.getText().toString().equals(user.getGroupName()))
                    curentGroup.setVisibility(View.GONE);
                else curentGroup.setVisibility(View.VISIBLE);
                if(GroupName.getText().toString().equals("Admin Group"))
                    Toast.makeText(getActivity().getApplicationContext(), "Расписание не доступно для администраторов", Toast.LENGTH_SHORT).show();
                else if(GroupName.getText().toString().equals("10"))
                {
                    Subject[] subjects={new Subject("8.00-8.45","Иностранный язык","","28,34,47"),
                            new Subject("8.55-9.40","Иностранный язык","","28,34,47"),
                            new Subject("9.50-10.35","Русский язык","","16"),
                            new Subject("10.45-11.30","Физическая культура и здоровье","",""),
                            new Subject("11.40-12.25","Обед","",""),
                            new Subject("12.35-13.20","Математика","","33"),
                            new Subject("13.30-14.15","Математика","","33")};
                    recyclerView.setAdapter(new ScheduleRecycleListAdapter(subjects,getContext()));
                }

                else if(GroupName.getText().toString().equals("20"))
                {
                    Subject[] subjects={new Subject("8.00-8.45","Производственное обучение","","маст."),
                            new Subject("8.55-9.40","Производственное обучение","","маст."),
                            new Subject("9.50-10.35","Производственное обучение","","маст."),
                            new Subject("10.45-11.30","Производственное обучение","","маст."),
                            new Subject("11.40-12.25","Компьютерные сети","","31"),
                            new Subject("12.35-13.20","Обед","",""),
                            new Subject("13.30-14.15","Электронный офис","","32б,31"),
                            new Subject("14.25-15.10","Иностранный язык","","28,47"),
                            new Subject("15.20-16.05","Иностранный язык","","28,47"),};
                    recyclerView.setAdapter(new ScheduleRecycleListAdapter(subjects,getContext()));
                }

                else if(GroupName.getText().toString().equals("30"))
                {
                    Subject[] subjects={new Subject("8.00-8.45","Основы экономики","","23"),
                            new Subject("8.55-9.40","Основы экономики","","23"),
                            new Subject("9.50-10.35","Офисное программирование","","21,22"),
                            new Subject("10.45-11.30","Офисное программирование","","21,22"),
                            new Subject("11.40-12.25","Основы экономики","","23"),
                            new Subject("12.35-13.20","Операционные системы","","31,21"),
                            new Subject("13.30-14.15","Обед","",""),
                            new Subject("14.25-15.10","Основы экономики","","23"),
                            new Subject("15.20-16.05","Физическая культура и здоровье","",""),
                            new Subject("15.20-16.05","Операционные системы","","31,21")};
                    recyclerView.setAdapter(new ScheduleRecycleListAdapter(subjects,getContext()));
                }

                else if(GroupName.getText().toString().equals("10д"))
                {
                    Subject[] subjects={
                            new Subject("8.55-9.40","Подготовка учащихся к семейной жизни (ф)","","37"),
                            new Subject("9.50-10.35","Психология и этика деловых отношений","","17"),
                            new Subject("10.45-11.30","Психология и этика деловых отношений","","17"),
                            new Subject("11.40-12.25","Обед","",""),
                            new Subject("12.35-13.20","Производственное обучение","","маст."),
                            new Subject("13.30-14.15","Производственное обучение","","маст."),
                            new Subject("14.25-15.10","Производственное обучение","","маст."),
                            new Subject("15.20-16.05","Производственное обучение","","маст."),
                            new Subject("16.15-17.00","Воспитательный час","","36")};
                    recyclerView.setAdapter(new ScheduleRecycleListAdapter(subjects,getContext()));
                }
                else Toast.makeText(getActivity().getApplicationContext(), "Расписание для этой группы пока не доступно", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setSubjects(Subject[] subjects) {
        this.subjects = subjects;
    }

    public void setSchelduleAdapter(Subject[] subjects){
        this.subjects=subjects;
        if(this.subjects != null){
            //Subject[] subjects=new Subject[arguments.getInt("Schl_size")];
           // subjects= (Subject[]) arguments.getParcelableArray("SchelduleList");
            this.subjects = (Subject[]) getArguments().getParcelableArray("SchelduleList");

            //Subject[] subjects={new Subject("9.00-10.40","Ботаника","10 лаб","26"),
            //       new Subject("8.55-9.40","Контроль качества продукции в сфере д/о и призводства мебели","fdkjgflkgjdjdlgjfdlkgjldjglfdfgjd","26")};

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new ScheduleRecycleListAdapter(subjects));}
        /*else*/ Toast.makeText(getActivity().getApplicationContext(), "Расписание не доступно", Toast.LENGTH_SHORT).show();
    }
   /* public void readExcelFileFromAssets(Subject[] subjects) {


        try {
            InputStream stream = assetManager.open("1.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(stream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            right right = new right("10",4,3);
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            /*for (int r = 0; r<rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount =row.getPhysicalNumberOfCells();
                for (int c = 0; c<cellsCount; c++) {
                    String value = getCellAsString(row, c, formulaEvaluator);
                    if(value==user.getGroupName()) {
                        right.add(new right(value, r + 1, c));
                        break;
                    }
                    //String cellInfo = "r:"+r+"; c:"+c+"; v:"+value;
                    //Toast.makeText(getActivity().getApplicationContext(), cellInfo, Toast.LENGTH_SHORT).show();
                }
            }*/
            //right.add(new right("10",4,3));
           /* if(right!=null)
            for(int i=0;i<1/*right.size()*/;/*i++){
                Row row = sheet.getRow(right.row);
                    String value = getCellAsString(row,right.count, formulaEvaluator);
                    if(value==user.getGroupName()) {
                        for(int j=0;j<14;j++)//заполнение ячеек расписания
                        {
                            Subject s=new Subject();
                            row = sheet.getRow(right.row+1);
                            s.setCab(getCellAsString(row,right.count+1, formulaEvaluator));

                            row = sheet.getRow(right.row+1);
                            s.setTime(getCellAsString(row,right.count-1, formulaEvaluator));

                             row = sheet.getRow(right.row+1);
                            s.setSubjectName(getCellAsString(row,right.count, formulaEvaluator));
                            subjects[j]=new Subject(s);
                        }
                    }

                }

        } catch (Exception e) {
            /* proper exception handling to be here */
          /*  e.printStackTrace();
        }

        return;
    }
    protected String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("dd/MM/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = ""+numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return value;
    }
public class right{
        public String value;
        public int row;
    public int count;

    public right(String value, int row, int count) {
        this.value = value;
        this.row = row;
        this.count = count;
    }
}*/
}