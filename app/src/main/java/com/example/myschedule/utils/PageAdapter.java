package com.example.myschedule.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myschedule.PageFragment;
import com.example.myschedule.data.Subject;

import java.util.Map;

public class PageAdapter extends FragmentStateAdapter {
    Map<String,Subject[]> subjects;
    String[] days={"Понедельник","Вторник","Среда","Четверг","Пятница","Понедельник","Вторник","Среда","Четверг","Пятница"};
    public PageAdapter(FragmentActivity fragmentActivity, Map<String,Subject[]> subject) {
        super(fragmentActivity);
        subjects=subject;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return(PageFragment.newInstance(position,subjects.get(days[position])));
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
